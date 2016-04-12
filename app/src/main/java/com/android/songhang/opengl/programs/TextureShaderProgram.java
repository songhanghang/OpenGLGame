package com.android.songhang.opengl.programs;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.songhang.opengl.R;
import static android.opengl.GLES20.*;


/**
 * Created by songhang on 16/4/12.
 * 纹理着色器程序
 */
public class TextureShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(@NonNull Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    public void setUniforms(float[] matrix, int textureId){
        //设置矩阵
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        //初始化纹理单元
        glActiveTexture(GL_TEXTURE0);
        //绑定到这个单元
        glBindTexture(GL_TEXTURE_2D, textureId);
        //将选定的纹理单元传递给片段着色器中的u_TextureUnit
        glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesLocation() {
        return aTextureCoordinatesLocation;
    }
}
