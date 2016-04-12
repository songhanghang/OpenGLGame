package com.android.songhang.opengl.model;

import com.android.songhang.opengl.Constants;
import com.android.songhang.opengl.data.VertexArray;
import com.android.songhang.opengl.programs.ColorShaderProgram;
import static android.opengl.GLES20.*;

/**
 * Created by songhang on 16/4/12.
 * 木椎
 */
public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2; // 坐标方向数量
    private static final int COLOR_COMPONENT_COUNT = 3; // 颜色纬度数量
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = {
          // X     Y       R     G     B
            0f,  -0.4f,    0f,   0f,  1f,
            0f,   0.4f,    1f,   0f,  0f
    };
    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        //顶点
        vertexArray.setVertexAttribPointer(0, colorShaderProgram.getPositionLocation(), POSITION_COMPONENT_COUNT, STRIDE);
        //颜色
        vertexArray.setVertexAttribPointer(POSITION_COMPONENT_COUNT, colorShaderProgram.getColorLocation(), COLOR_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        //画点
        glDrawArrays(GL_POINTS, 0, 2);
    }
}
