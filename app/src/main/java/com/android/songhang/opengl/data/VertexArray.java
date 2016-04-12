package com.android.songhang.opengl.data;

import com.android.songhang.opengl.Constants;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;


import static android.opengl.GLES20.*;

/**
 * Created by songhang on 16/4/12.
 */
public class VertexArray {
    private final FloatBuffer floatBuffer;

    public VertexArray(float[] vertextData) {
        floatBuffer = ByteBuffer
                .allocateDirect(vertextData.length * Constants.BYTES_PER_FLOAT) //申请内存大小 单位字节
                .order(ByteOrder.nativeOrder()) //本地顺序排序
                .asFloatBuffer()
                .put(vertextData); //把数据从寄存器写入本地内存
    }

    /**
     * 关联float[] 到opengl
     * @param dataOffset
     * @param attributeLocation
     * @param componentCount
     * @param stride
     */
    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        floatBuffer.position(dataOffset);
        //关联坐标属性与顶点数据的数组
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
        //使能坐标顶点数组，告诉opengl可以使用顶点数据
        glEnableVertexAttribArray(attributeLocation);
        //重置缓存区指针到开始位置
        floatBuffer.position(0);
    }
}
