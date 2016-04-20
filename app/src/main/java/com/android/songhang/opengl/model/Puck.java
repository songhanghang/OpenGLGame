package com.android.songhang.opengl.model;

import com.android.songhang.opengl.data.Geometry;
import com.android.songhang.opengl.data.VertexArray;
import com.android.songhang.opengl.programs.ColorShaderProgram;
import com.android.songhang.opengl.util.ObjectBuilder;

import java.util.List;

/**
 * Created by songhang on 16/4/20.
 */
public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3; // 坐标方向数量
    public final float raduis, height;
    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float raduis, float height, int numPointsAroundPuck) {
        ObjectBuilder.GenerateData generateData = ObjectBuilder.createPuck(new Geometry.Cylinder(new Geometry.Point(0f,0f,0f), raduis, height), numPointsAroundPuck);
        this.raduis = raduis;
        this.height = height;
        vertexArray = new VertexArray(generateData.vertexData);
        drawList = generateData.drawList;
    }

    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexArray.setVertexAttribPointer(0, colorShaderProgram.getPositionLocation(), POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
