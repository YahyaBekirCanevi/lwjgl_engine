package com.canevi.engine.utils;

import java.util.List;
import java.util.stream.Stream;

import com.canevi.engine.enums.TextureFilterType;
import com.canevi.engine.enums.TextureWrapType;
import com.canevi.engine.graphics.Material;
import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Vertex;
import com.canevi.engine.io.ModelLoader;
import com.canevi.engine.io.TextureLoader;
import com.canevi.engine.maths.Vector2f;
import com.canevi.engine.maths.Vector3f;

public class MeshExamples {
    public static List<Mesh> watcher() {
        String path = "sf/sea-keep-lonely-watcher";
        List<String> textures = Stream.of("/textures/texture_building.jpeg",
                "/textures/texture_environment.jpg", "/textures/texture_sand.jpeg",
                "/textures/texture_sea.jpeg", "/textures/texture_sky.jpeg")
                .map(e -> path + e).collect(java.util.stream.Collectors.toList());
        return ModelLoader.loadModel(path + "/source/Stronghold.fbx", textures, true);
    }

    /*
     * public static Mesh dragon() {
     * return ModelLoader.loadModel("models/dragon.obj", "textures/image.png", 0,
     * true);
     * }
     */

    /*
     * public static Mesh generatePlane(String texturePath) {
     * EnumMap<TextureType, String> enumMap = new EnumMap<>(TextureType.class);
     * enumMap.put(TextureType.DIFFUSE, texturePath);
     * return new Mesh(new Vertex[] {
     * new Vertex(new Vector3f(-0.5f, 0.5f, 0.0f), new Vector3f(1.0f, 0.0f, 0.0f),
     * new Vector2f(0.0f, 0.0f)),
     * new Vertex(new Vector3f(-0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 1.0f, 0.0f),
     * new Vector2f(0.0f, 1.0f)),
     * new Vertex(new Vector3f(0.5f, -0.5f, 0.0f), new Vector3f(0.0f, 0.0f, 1.0f),
     * new Vector2f(1.0f, 1.0f)),
     * new Vertex(new Vector3f(0.5f, 0.5f, 0.0f), new Vector3f(1.0f, 1.0f, 0.0f),
     * new Vector2f(1.0f, 0.0f))
     * }, new int[] {
     * 0, 1, 2,
     * 0, 3, 2
     * }, new Material(false, enumMap));
     * }
     */
    public static Mesh generateCube(String texturePath) {
        return new Mesh(new Vertex[] {
                // Back face
                new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(1.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f)),

                // Front face
                new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f)),

                // Right face
                new Vertex(new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f)),

                // Left face
                new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f)),

                // Top face
                new Vertex(new Vector3f(-0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f, 0.5f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, 0.5f, -0.5f), new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, 0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f)),

                // Bottom face
                new Vertex(new Vector3f(-0.5f, -0.5f, 0.5f), new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f)),
                new Vertex(new Vector3f(-0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, -0.5f, -0.5f), new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 1.0f)),
                new Vertex(new Vector3f(0.5f, -0.5f, 0.5f), new Vector3f(1.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f)),
        }, new int[] {
                // Back face
                0, 1, 3,
                3, 1, 2,

                // Front face
                4, 5, 7,
                7, 5, 6,

                // Right face
                8, 9, 11,
                11, 9, 10,

                // Left face
                12, 13, 15,
                15, 13, 14,

                // Top face
                16, 17, 19,
                19, 17, 18,

                // Bottom face
                20, 21, 23,
                23, 21, 22
        }, new Material(
                TextureLoader.getTexture("textures/wooden-box.png", false,
                        TextureWrapType.REPEAT, TextureFilterType.LINEAR),
                null, null));
    }

}
