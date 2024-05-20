package com.canevi.engine.utils;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.io.ModelLoader;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MeshExamples {
    public static List<Mesh> watcher() {
        String path = "sf/sea-keep-lonely-watcher";
        List<String> textures = Stream.of("/textures/texture_building.jpeg",
                "/textures/texture_environment.jpg", "/textures/texture_sand.jpeg",
                "/textures/texture_sea.jpeg", "/textures/texture_sky.jpeg")
                .map(e -> path + e).toList();
        return ModelLoader.loadModel(path + "/source/Stronghold.fbx", textures, true);
    }
    public static Mesh generateCube(String texturePath) {
        return ModelLoader.loadModel("models/cube.fbx", Collections.singletonList(texturePath), false).get(0);
    }
    public static Mesh generateCapsule(String texturePath) {
        return ModelLoader.loadModel("models/capsule.fbx", Collections.singletonList(texturePath), false).get(0);
    }

}
