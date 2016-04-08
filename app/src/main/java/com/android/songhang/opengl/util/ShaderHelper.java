package com.android.songhang.opengl.util;

import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * Created by songhang on 16/4/8.
 */
public class ShaderHelper {
    private static final String TAG = ShaderHelper.class.getSimpleName() + " : ";

    public static int complieVertexShader(String shaderCode) {
        return complieShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int complieFragmentShader(String shaderCode) {
        return complieShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    /**
     * 编译shader源代码
     * @param type
     * @param shaderCode
     * @return
     */
    public static int complieShader(int type, String shaderCode) {
        //创建对应类型的shader着色器，返回这个对象的id
        final int shaderObjectId = glCreateShader(type);
        //创建失败
        if (shaderObjectId == 0) {
            Log.e(TAG, TAG + "创建此type的shader失败");
            return 0;
        }
        //上传shader 源代码，关联shader对象和源代码
        glShaderSource(shaderObjectId, shaderCode);
        //编译已经上传过的shader源代码
        glCompileShader(shaderObjectId);
        //检查opengl是否已经成功编译了这个shader
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0); //获取编译状态
        //打印获取shader的详细日志
        Log.v(TAG, TAG + "编译shader源码的结果： \n" + shaderCode + "\n" + glGetShaderInfoLog(shaderObjectId));
        if (compileStatus[0] == 0) {
            //失败后删除shader对象
            glDeleteShader(shaderObjectId);
            Log.e(TAG, TAG + "编译shader源代码failed");
            return 0;
        }
        return shaderObjectId;
    }

    /**
     * 连接shader到program并且返回program id
     * @param vertextShaderId
     * @param fragmentShaderId
     * @return
     */
    public static int linkProgram(int vertextShaderId, int fragmentShaderId) {
        //创建opengl程序对象并返回对象id
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.e(TAG, TAG + "创建新的openGl程序对象失败");
            return 0;
        }
        //绑定shader
        glAttachShader(programObjectId, vertextShaderId);
        glAttachShader(programObjectId, fragmentShaderId);
        //连接shader
        glLinkProgram(programObjectId);
        final int[] linkStatus = new int[1];
        //查询link状态
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);
        //打印获取程序的详细日志
        Log.v(TAG, TAG + "连接opengl程序的结果：\n" + glGetProgramInfoLog(programObjectId));
        if (linkStatus[0] == 0) {
            //连接失败删除程序
            glDeleteProgram(programObjectId);
            Log.e(TAG, TAG + "连接oepngl程序failed");
        }
        return programObjectId;
    }

    /**
     * 验证程序是否有效
     * @param programObjectId
     * @return
     */
    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, TAG + "验证opengl程序的结果： \n" + validateStatus + "\nLOG: " + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

}
