package com.android.songhang.opengl.util;

import android.util.FloatMath;

import com.android.songhang.opengl.data.Geometry;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.*;

/**
 * Created by songhang on 16/4/19.
 */
public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;
    private final float[] vertextData;
    private final List<DrawCommand> drawList = new ArrayList<>();
    private int offset = 0;

    private ObjectBuilder(int sizeInVertices) {
        vertextData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    /**
     * 计算圆柱体顶部顶点数量
     *
     * @param numPoints
     * @return
     */
    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (numPoints + 1);
    }

    /**
     * 计算圆柱体侧面顶点数量
     *
     * @param numPoints
     * @return
     */
    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    /**
     * 创建冰球
     *
     * @param puck
     * @param numPoints
     * @return
     */
    public static GenerateData createPuck(Geometry.Cylinder puck, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);
        Geometry.Circle puckTop = new Geometry.Circle(puck.center.translateY(puck.height / 2), puck.radius);
        builder.appendCircle(puckTop, numPoints);
        builder.appendOpenCyliner(puck, numPoints);
        return builder.bulid();
    }

    /**
     * 创建木追&冰球
     *
     * @param center
     * @param radius
     * @param height
     * @param numPoints
     * @return
     */
    public static GenerateData createMallet(Geometry.Point center, float radius, float height, int numPoints) {
        //定点数
        int size = (sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints)) * 2;
        ObjectBuilder builder = new ObjectBuilder(size);

        //-----底部冰球
        //底部冰球的高度
        float baseHeight = height * 0.25f;
        //底部冰球圆 & 圆柱
        Geometry.Circle baseCircle = new Geometry.Circle(center.translateY(-baseHeight), radius);
        Geometry.Cylinder baseCyliner = new Geometry.Cylinder(baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight);
        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCyliner(baseCyliner, numPoints);

        //----手柄
        //手柄高度 & 半径
        float handlerHeight = height * 0.75f;
        float handleRadius = radius / 3f;
        Geometry.Circle handlerCircle = new Geometry.Circle(center.translateY(height / 2f), handleRadius);
        Geometry.Cylinder handleCylinder = new Geometry.Cylinder(handlerCircle.center.translateY( - handlerHeight / 2), handleRadius, handlerHeight);
        builder.appendCircle(handlerCircle, numPoints);
        builder.appendOpenCyliner(handleCylinder, numPoints);
        return builder.bulid();
    }

    private void appendCircle(Geometry.Circle circle, int numPoints) {
        //起始点
        final int starVertext = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);
        //圆心
        vertextData[offset++] = circle.center.x;
        vertextData[offset++] = circle.center.y;
        vertextData[offset++] = circle.center.z;
        for (int i = 0; i <= numPoints; i++) {
            //角度
            float angleInRadians = ((float) i / numPoints) * ((float) Math.PI * 2f);
            vertextData[offset++] = circle.center.x + circle.radius * (float) Math.cos(angleInRadians);
            vertextData[offset++] = circle.center.y;
            vertextData[offset++] = circle.center.z + circle.radius * (float) Math.sin(angleInRadians);
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_FAN, starVertext, numVertices);
            }
        });
    }

    private void appendOpenCyliner(Geometry.Cylinder cyliner, int numPoints) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);
        final float yStart = cyliner.center.y - (cyliner.height / 2f);
        final float yEnd = cyliner.center.y + (cyliner.height / 2f);
        for (int i = 0; i <= numPoints; i++) {
            //角度
            float angleInRadians = ((float) i / numPoints) * ((float) Math.PI * 2f);
            float xPosition = cyliner.center.x + cyliner.radius * (float) Math.cos(angleInRadians);
            float zPosition = cyliner.center.z + cyliner.radius * (float) Math.sin(angleInRadians);

            //开始顶点
            vertextData[offset++] = xPosition;
            vertextData[offset++] = yStart;
            vertextData[offset++] = zPosition;
            //结束顶点
            vertextData[offset++] = xPosition;
            vertextData[offset++] = yEnd;
            vertextData[offset++] = zPosition;
        }
        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    public static class GenerateData {
        public final float[] vertexData;
        public final List<DrawCommand> drawList;

        public GenerateData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private GenerateData bulid() {
        return new GenerateData(vertextData, drawList);
    }

    public interface DrawCommand {
        void draw();
    }
}
