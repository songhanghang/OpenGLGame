package com.android.songhang.opengl.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
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
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
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
    private static final int BYTES_PER_FLOAT = 4; //每个浮点数占4个字节
    private final FloatBuffer vertexData;
    private Context context;
    private int program;
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";
    private int uColorLocation;
    private int aPositionLocation;

    public GameRenderer(@NonNull Context context) {
        this.context = context;
        //顶点位置
        float[] tableVertices = {
                //第一个三角
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
                //第二个三角
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                //中间线
                -0.5f, 0f,
                0.5f, 0f,
                //中间两个点
                0f, -0.25f,
                0f, 0.25f

        };
        vertexData = ByteBuffer
                .allocateDirect(tableVertices.length * BYTES_PER_FLOAT) //申请内存大小 单位字节
                .order(ByteOrder.nativeOrder()) //本地顺序排序
                .asFloatBuffer();
        vertexData.put(tableVertices); //把数据从寄存器写入本地内存
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
            uColorLocation = glGetUniformLocation(program, U_COLOR);
            aPositionLocation = glGetAttribLocation(program, A_POSITION);
            //重置缓存区指针到开始位置
            vertexData.position(0);
            //关联属性与顶点数据的数组
            glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
            //使能顶点数组，告诉opengl可以使用顶点数据
            glEnableVertexAttribArray(aPositionLocation);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        //绘制桌面
        //设置统一颜色，白色
        glUniform4f(uColorLocation, 1f, 1f, 1f, 1f);
        //画两个三角形
        glDrawArrays(GL_TRIANGLES, 0 , 6);

        //绘制中间线
        glUniform4f(uColorLocation, 1f, 0f, 0f, 1f );
        glDrawArrays(GL_LINES, 6, 2);

        //绘制两个点
        glUniform4f(uColorLocation, 1f, 0f, 1f, 1f );
        glDrawArrays(GL_POINTS, 8, 1);

        glUniform4f(uColorLocation, 1f, 0f, 0f, 1f );
        glDrawArrays(GL_POINTS, 9, 1);

    }
}
