package com.canevi.game.controller;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Renderer;
import com.canevi.engine.graphics.Shader;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.RenderableObject;
import com.canevi.engine.utils.MeshExamples;

import java.util.ArrayList;
import java.util.List;

public class ModelImport {
    public List<RenderableObject> importModels(Shader shader) {
        Renderer renderer = new Renderer(shader);

        List<Mesh> meshList = MeshExamples.watcher();
        List<RenderableObject> objectList = new ArrayList<>(meshList.stream()
                .map(mesh -> new RenderableObject(new Transform(Vector3f.zero()), mesh, renderer)).toList());

        for (int i = 0; i < 100; i++) {
            Vector3f pos = Vector3f.randomPosition(40);
            // pos.setY(0f);
            Transform boxTransform = new Transform(pos);
            boxTransform.scale(.2f);
            RenderableObject object = new RenderableObject(boxTransform,
                    MeshExamples.generateCube("textures/wooden-box.png"), renderer);
            objectList.add(object);
        }
        return objectList;
    }
}
