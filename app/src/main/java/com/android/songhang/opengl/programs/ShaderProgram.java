package com.android.songhang.opengl.programs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.songhang.opengl.util.ShaderHelper;
import com.android.songhang.opengl.util.TextResReader;
import static android.opengl.GLES20.*;

/**
 * Created by songhang on 16/4/12.
 */
public class ShaderProgram {
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    protected final int program;

    protected ShaderProgram(@NonNull Context context, int vertexShaderResId, int fragmentShaderResId) {
        program = ShaderHelper.buildProgram(
                TextResReader.readTextFileFromRes(context, vertexShaderResId),
                TextResReader.readTextFileFromRes(context, fragmentShaderResId)
        );
    }

    public void useProgram() {
        glUseProgram(program);
    }
}
