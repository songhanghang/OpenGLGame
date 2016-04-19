package com.android.songhang.opengl.util;

/**
 * Created by songhang on 16/4/19.
 */
public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;
    private final float[] vertextData;
    private int offset = 0;

    private ObjectBuilder(int sizeInVertices) {
        vertextData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    /**
     * 计算圆柱体顶部顶点数量
     * @param numPoints
     * @return
     */
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    /**
     * 计算圆柱体侧面顶点数量
     * @param numPoints
     * @return
     */
    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }
}
