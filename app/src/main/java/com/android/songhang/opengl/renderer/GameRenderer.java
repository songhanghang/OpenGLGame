package com.android.songhang.opengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.support.annotation.NonNull;

import com.android.songhang.opengl.R;
import com.android.songhang.opengl.util.ShaderHelper;
import com.android.songhang.opengl.util.TextResReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by songhang on 16/4/7.
 */
public class GameRenderer implements GLSurfaceView.Renderer {
    private static final int POSITION_COMPONENT_COUNT = 2; // 坐标方向数量
    private static final int COLOR_COMPONENT_COUNT = 3; // 颜色纬度数量
    private static final int BYTES_PER_FLOAT = 4; //每个浮点数占4个字节
    private static final String U_MATRIX = "u_Matrix";
    private static final String A_COLOR = "a_Color";
    private static final String A_POSITION = "a_Position";
    private final FloatBuffer vertexData;
    private final float[] projectionMatrix = new float[16];//正交投影矩阵
    private final float[] modelMatrix = new float[16];//移动模型矩阵
    private Context context;
    private int program;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    public GameRenderer(@NonNull Context context) {
        this.context = context;
        //带有颜色信息的顶点数组
        float[] tableVerVerticesWithTriangles = {
                //三角扇 x    y      R      G    B
                0f, 0f, 1f, 1f, 1f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
                // 中间线
                -0.5f, 0f, 1f, 0f, 0f,
                0.5f, 0f, 1f, 0f, 0f,
                // 两点
                0f, -0.4f, 0f, 0f, 1f,
                0f, 0.4f, 1f, 0f, 0f
        };
        vertexData = ByteBuffer
                .allocateDirect(tableVerVerticesWithTriangles.length * BYTES_PER_FLOAT) //申请内存大小 单位字节
                .order(ByteOrder.nativeOrder()) //本地顺序排序
                .asFloatBuffer();
        vertexData.put(tableVerVerticesWithTriangles); //把数据从寄存器写入本地内存
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0f, 0, 0, 0);
        String vertexShaderSrc = TextResReader.readTextFileFromRes(context, R.raw.smiple_vertex_shader);
        String fragmentShaderSrc = TextResReader.readTextFileFromRes(context, R.raw.smiple_fragment_shader);
        int verTexShader = ShaderHelper.complieVertexShader(vertexShaderSrc);
        int fragmentShader = ShaderHelper.complieFragmentShader(fragmentShaderSrc);
        program = ShaderHelper.linkProgram(verTexShader, fragmentShader);
        //验证程序有效再使用
        if (ShaderHelper.validateProgram(program)) {
            glUseProgram(program);
            aColorLocation = glGetAttribLocation(program, A_COLOR);
            aPositionLocation = glGetAttribLocation(program, A_POSITION);
            uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
            //重置缓存区指针到开始位置
            vertexData.position(0);
            // ---- 坐标属性
            //关联坐标属性与顶点数据的数组
            glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
            //使能坐标顶点数组，告诉opengl可以使用顶点数据
            glEnableVertexAttribArray(aPositionLocation);
            // ----- 颜色属性
            vertexData.position(POSITION_COMPONENT_COUNT);
            glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);
            glEnableVertexAttribArray(aColorLocation);

        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        //透视矩阵
        Matrix.perspectiveM(projectionMatrix, 0,  45, (float) width / height, 1f, 10f);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        //投影矩阵乘以模型矩阵
        final float[] temp = new float[16];
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //绘制桌面
        //设置统一颜色，白色
        //画三角形扇
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        //绘制中间线
        glDrawArrays(GL_LINES, 6, 2);

        //绘制两个点
        glDrawArrays(GL_POINTS, 8, 1);
        glDrawArrays(GL_POINTS, 9, 1);
        //应用矩阵
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

    }
}
