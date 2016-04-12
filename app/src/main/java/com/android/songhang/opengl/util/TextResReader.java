package com.android.songhang.opengl.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by songhang on 16/4/8.
 * opengl代码文件读入
 */
public class TextResReader {
    public static String readTextFileFromRes(Context context, int resId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String nextLine;

            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine);
                body.append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not open resource : " + resId, e);
        } catch (Resources.NotFoundException nfe) {
            throw new RuntimeException("Resource not dounf : " + resId, nfe);
        }
        return body.toString();
    }
}
