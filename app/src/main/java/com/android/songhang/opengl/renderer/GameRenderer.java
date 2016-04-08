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
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by songhang on 16/4/7.
 */
public class GameRenderer implements GLSurfaceView.Renderer {
    private static final int POSITION_COUNT = 2; // 坐标方向数量
    private static final int BYTES_PER_FLOAT = 4; //每个浮点数占4个字节
    private final FloatBuffer vertexData;
    private Context context;
    private int program;

    public GameRenderer(@NonNull Context context) {
        this.context = context;
        //顶点位置
        float[] tableVertices = {
                //第一个三角
                0f, 0f,
                9f, 14f,
                0f, 14f,
                //第二个三角
                0f, 0f,
                9f, 0f,
                9f, 14f,
                //中间线
                0f, 7f,
                9f, 7f,
                //中间两个点
                4.5f, 2f,
                4.5f, 12f

        };
        vertexData = ByteBuffer
                .allocateDirect(tableVertices.length * BYTES_PER_FLOAT) //申请内存大小 单位字节
                .order(ByteOrder.nativeOrder()) //本地顺序排序
                .asFloatBuffer();
        vertexData.put(tableVertices); //把数据从寄存器写入本地内存
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(1.0f, 0, 0, 0);
        String vertexShaderSrc = TextResReader.readTextFileFromRes(context, R.raw.smiple_vertex_shader);
        String fragmentShaderSrc = TextResReader.readTextFileFromRes(context, R.raw.smiple_fragment_shader);
        int verTexShader = ShaderHelper.complieVertexShader(vertexShaderSrc);
        int fragmentShader = ShaderHelper.complieFragmentShader(fragmentShaderSrc);
        program = ShaderHelper.linkProgram(verTexShader, fragmentShader);
        //验证程序有效再使用
        if (ShaderHelper.validateProgram(program)) {
            glUseProgram(program);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glClearColor(0, 0, 0.7f, 0);

    }
}
