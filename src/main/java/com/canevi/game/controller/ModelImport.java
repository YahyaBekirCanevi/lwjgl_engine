package com.canevi.game.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.canevi.engine.Playground;
import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Renderer;
import com.canevi.engine.graphics.Shader;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.RenderableObject;
import com.canevi.engine.utils.MeshExamples;

public class ModelImport {
    private Shader shader;
    private Renderer renderer;

    public void importModels() {
        shader = new Shader("shaders/mainVertex.glsl", "shaders/mainFragment.glsl");
        shader.create();
        renderer = new Renderer(shader);

        List<Mesh> meshList = new ArrayList<>();
        // meshList.add(MeshExamples.generatePlane("sf/sea-keep-lonely-watcher/textures/texture_building.jpeg"));
        // meshList.add(MeshExamples.watcherPart(0, 0));
        meshList.addAll(MeshExamples.watcher());
        // meshList.add(MeshExamples.dragon());
        // meshList.add(MeshExamples.generateCube("textures/wooden-box.png"));
        Playground.renderables.addAll(meshList.stream().map(mesh -> {
            return new RenderableObject(null,
                    new Transform(Vector3f.zero(), new Vector3f(90f, 0f, 0f)), mesh, renderer);
        }).collect(Collectors.toList()));

        for (int i = 0; i < 100; i++) {
            Transform boxTransform = new Transform(Vector3f.randomPosition(40));
            boxTransform.scale(50);
            Playground.renderables.add(new RenderableObject(null, boxTransform,
                    MeshExamples.generateCube("textures/wooden-box.png"), renderer));
        }

        Playground.renderables.forEach(mesh -> {
            mesh.create();
            mesh.getTransform().scale(.1f);
        });
    }

    public void destroy() {
        shader.destroy();
    }

}
