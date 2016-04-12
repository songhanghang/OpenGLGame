package com.android.songhang.opengl.model;

import com.android.songhang.opengl.Constants;
import com.android.songhang.opengl.data.VertexArray;
import com.android.songhang.opengl.programs.TextureShaderProgram;

import static android.opengl.GLES20.*;

/**
 * Created by songhang on 16/4/12.
 * 桌子
 */
public class Table {
    private static final int POSITION_COMPONENT_COUNT = 2; // 坐标方向数量
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2; // 纹理坐标纬度
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
            //order of coordinates :
            //    顶点                 纹理
            // X       Y           S       T
               0f,    0f,       0.5f,   0.5f,
            -0.5f, -0.8f,         0f,   0.9f,
             0.5f, -0.8f,         1f,   0.9f,
             0.5f,  0.8f,         1f,   0.1f,
            -0.5f,  0.8f,         0f,   0.1f,
            -0.5f, -0.8f,         0f,   0.9f
    };

    private final VertexArray vertexArray;

    public Table() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureShaderProgram) {
        //定点
        vertexArray.setVertexAttribPointer(0, textureShaderProgram.getPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        //纹理
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, textureShaderProgram.getTextureCoordinatesLocation(), TEXTURE_COORDINATES_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        //画三角扇
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);
    }
}
