package com.android.songhang.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.songhang.opengl.renderer.GameRenderer;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet; //是否已经设置渲染

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        genOpenGLEsView();
        setContentView(glSurfaceView);
    }

    private void genOpenGLEsView() {
        glSurfaceView = new GLSurfaceView(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        boolean supportES2 = configurationInfo.reqGlEsVersion >= 0X20000;
        if (supportES2) {
            glSurfaceView.setEGLContextClientVersion(2);
            glSurfaceView.setRenderer(new GameRenderer(this));
            rendererSet = true;
        } else {
            Toast.makeText(this, "该设备不支持OpenGLES", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }
}
