package com.android.songhang.opengl.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * Created by songhang on 16/4/12.
 */
public class TextureHelper {
    private static final String TAG = TextureHelper.class.getSimpleName();

    public static int loadTexture(Context context, int resId) {
        final int[] texttureObjectIds = new int[1];
        glGenTextures(1, texttureObjectIds, 0);
        if (texttureObjectIds[0] == 0) {
            Log.w(TAG, "不支持构建openGl texture对象");
            return 0;
        }
        //加载位图数据并与纹理绑定
        //----1加载
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap bitmap = new BitmapFactory().decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            Log.w(TAG, "图片资源 res： " + resId + "不能解码");
            glDeleteTextures(1, texttureObjectIds, 0);
            return 0;
        }
        //----2绑定
        glBindTexture(GL_TEXTURE_2D, texttureObjectIds[0]);

        //设置默认的纹理过滤参数
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR); //缩小三线过滤
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR); //放大双线性过滤
        //加载到opengl
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        //生成mip所有必要级别
        glGenerateMipmap(GL_TEXTURE_2D);
        //接触绑定，防止意外改变这个纹理
        glBindTexture(GL_TEXTURE_2D, 0);
        return texttureObjectIds[0];
    }
}
