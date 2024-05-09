package com.canevi.engine;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;

import com.canevi.engine.io.Input;
import com.canevi.engine.io.Window;
import com.canevi.engine.objects.Camera;
import com.canevi.engine.objects.RenderableObject;
import com.canevi.engine.objects.ThreadObject;
import com.canevi.game.controller.ModelImport;
import com.canevi.game.controller.PlayerController;

public class Playground extends ThreadObject implements Runnable {
    public Playground() {
        super("Playground");
    }

    private Window window;
    private ModelImport modelImport;

    public static List<ThreadObject> objects = new ArrayList<>();
    public static List<RenderableObject> renderables = new ArrayList<>();

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
    }

    @Override
    public void onStart() {
        int WIDTH = 1280;
        int HEIGHT = 760;
        window = new Window(WIDTH, HEIGHT, name);
        window.setBackgroundColor(0.1f, 0.2f, 0.3f);
        window.create();

        modelImport = new ModelImport();
        modelImport.importModels();

        objects.add(new PlayerController());
        objects.forEach(ThreadObject::onStart);
    }

    @Override
    public void onUpdate() {
        if (window.shouldClose()) {
            interrupt();
            objects.forEach(ThreadObject::interrupt);
            return;
        }
        window.update();
        renderables.forEach(renderableObject -> renderableObject.render(Camera.getMainCamera()));
        objects.forEach(ThreadObject::loop);
        window.swapBuffers();
        playgroundControls();
    }

    @Override
    public void onDestroy() {
        renderables.forEach(RenderableObject::destroy);
        objects.forEach(ThreadObject::destroy);
        objects.clear();
        renderables.clear();
        window.destroy();
        modelImport.destroy();
    }

    public void playgroundControls() {
        if (Input.isKeyDown(GLFW.GLFW_KEY_F11))
            window.setFullscreen(!window.isFullscreen());
    }

}
