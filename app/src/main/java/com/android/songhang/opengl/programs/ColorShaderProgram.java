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
    private final int uColorLocation;
    private final int aPositionLocation;

    public ColorShaderProgram(@NonNull Context context) {
        super(context, R.raw.smiple_vertex_shader, R.raw.smiple_fragment_shader);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
    }

    public void setUniforms(float[] matrix, float r, float g, float b) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        glUniform4f(uColorLocation, r, g, b, 1f);
    }

    public int getPositionLocation() {
        return aPositionLocation;
    }

    public int getColorLocation(){
        return uColorLocation;
    }
}
