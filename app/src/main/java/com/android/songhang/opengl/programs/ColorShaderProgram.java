package com.android.songhang.opengl.programs;

import android.content.Context;
import android.support.annotation.NonNull;
import static android.opengl.GLES20.*;

import com.android.songhang.opengl.R;

/**
 * Created by songhang on 16/4/12.
 */
public class ColorShaderProgram extends ShaderProgram{
    private final int uMatrixLocation;

    private final int aPositionLocation;
    private final int aColorLocation;

    public ColorShaderProgram(@NonNull Context context) {
        super(context, R.raw.smiple_vertex_shader, R.raw.smiple_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aColorLocation =- glGetAttribLocation(program, A_COLOR);
    }

    public void setUniforms(float[] matrix) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
    }

    public int getPositionLocation() {
        return aPositionLocation;
    }

    public int getColorLocation(){
        return aColorLocation;
    }
}
