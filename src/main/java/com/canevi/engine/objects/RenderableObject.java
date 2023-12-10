package com.canevi.engine.objects;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Renderer;
import com.canevi.engine.maths.Transform;

import lombok.Getter;

@Getter
public class RenderableObject extends TObject {
    private Mesh mesh;
    private Renderer renderer;

    public RenderableObject(TObject parent, Transform transform, Mesh mesh, Renderer renderer) {
        super(transform);
        this.mesh = mesh;
        this.renderer = renderer;
    }

    public void create() {
        // Playground.getInstance().addRenderableObject(this);
        mesh.create();
    }

    public void destroy() {
        mesh.destroy();
    }

    public void render(Camera camera) {
        renderer.renderMesh(mesh, transform, camera);
    }

}
