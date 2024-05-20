package com.canevi.engine.objects;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Renderer;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
public class RenderableObject extends GameObject {
    @Getter
    private static List<RenderableObject> list = new ArrayList<>();
    private final Mesh mesh;
    private final Renderer renderer;

    private Vector3f previousPosition;

    public RenderableObject(Transform transform, Mesh mesh, Renderer renderer) {
        super(transform);
        this.mesh = mesh;
        this.renderer = renderer;
        list.add(this);
    }
    public void render(Camera camera) {
        renderer.renderMesh(mesh, transform, camera);
    }
    @Override
    public void onStart() {
        super.onStart();
        mesh.create();
        getTransform().scale(.1f);
        previousPosition = getTransform().getPosition();
    }
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (Vector3f.subtract(previousPosition, getTransform().getPosition()).length() > 0.1f) {
            log.info("Location changed, {}->{}",previousPosition, getTransform().getPosition());
            previousPosition = getTransform().getPosition();
        }
        render(Camera.getMainCamera());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mesh.destroy();
    }

    public Vector3f getMinExtents() {
        return mesh.getMinExtents().scale(getTransform().getScale());
    }
    public Vector3f getMaxExtents() {
        return mesh.getMaxExtents().scale(getTransform().getScale());
    }

    @Override
    public String toString() {
        return "RenderableObject{" +
                "previousPosition=" + previousPosition +
                ", min=" + getMinExtents() +
                ", max=" + getMaxExtents() +
                ", transform=" + transform +
                '}';
    }
}
