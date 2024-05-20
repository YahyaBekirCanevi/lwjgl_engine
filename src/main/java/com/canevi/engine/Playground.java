package com.canevi.engine;

import com.canevi.engine.controller.LightController;
import com.canevi.engine.graphics.Renderer;
import com.canevi.engine.graphics.Shader;
import com.canevi.engine.io.Input;
import com.canevi.engine.io.Window;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.GameObject;
import com.canevi.engine.objects.RenderableObject;
import com.canevi.engine.objects.TObject;
import com.canevi.engine.objects.ThreadObject;
import com.canevi.engine.utils.MeshExamples;
import com.canevi.game.controller.ModelImport;
import com.canevi.game.controller.PlayerController;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class Playground extends ThreadObject implements Runnable {
    public Playground() {
        super("Playground");
    }

    private Window window;
    private ModelImport modelImport;
    private List<TObject> objectList = new ArrayList<>();
    private GameObject player;
    private Shader shader;
    private LightController lightController;
    public void start() {
        Thread thread = new Thread(this, name);
        thread.start();
    }

    @Override
    public void run() {
        onStart();
        loop();
    }

    @Override
    public void loop() {
        while (!shouldDestroy && isEnabled) {
            onUpdate();
        }
        onDestroy();
    }

    @Override
    public void onStart() {
        int WIDTH = 1280;
        int HEIGHT = 760;
        window = new Window(WIDTH, HEIGHT, name);
        window.setBackgroundColor(0.1f, 0.2f, 0.3f);
        window.create();

        lightController = new LightController();
        lightController.initialize();

        shader = new Shader("shaders/mainVertex.glsl", "shaders/mainFragment.glsl");
        shader.create();

        modelImport = new ModelImport();

        //objectList.addAll(modelImport.importModels(shader));
        RenderableObject cube = new RenderableObject(new Transform(Vector3f.one().scale(20)),
                MeshExamples.generateCube("textures/wooden-box.png"),new Renderer(shader));
        objectList.add(cube);
        RenderableObject cube2 = new RenderableObject(new Transform(Vector3f.right().scale(20)),
                MeshExamples.generateCube("textures/wooden-box.png"),new Renderer(shader));
        objectList.add(cube2);

        player = new GameObject(new Transform(new Vector3f(0f, 0f, 0f)));
        player.addComponent(new PlayerController());

        objectList.add(player);

        objectList.forEach(TObject::onStart);
    }

    @Override
    public void onUpdate() {
        if (window.shouldClose()) {
            interrupt();
            return;
        }
        window.update();
        objectList.forEach(TObject::onUpdate);
        window.swapBuffers();
        playgroundControls();
    }

    @Override
    public void onDestroy() {
        objectList.forEach(TObject::onDestroy);
        objectList.clear();
        window.destroy();
        shader.destroy();
    }

    public void playgroundControls() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_F11))
            window.setFullscreen(!window.isFullscreen());
    }

}
