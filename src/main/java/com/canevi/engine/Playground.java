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
    private Thread thread;
    private ModelImport modelImport;
    private final int WIDTH = 1280, HEIGHT = 760;

    public static List<ThreadObject> objects = new ArrayList<>();
    public static List<RenderableObject> renderables = new ArrayList<>();

    public void start() {
        thread = new Thread(this, name);
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
            objects.forEach(threadObject -> threadObject.interrupt());
            return;
        }
        window.update();
        renderables.forEach(renderableObject -> renderableObject.render(Camera.main()));
        objects.forEach(ThreadObject::loop);
        window.swapBuffers();
        playgroundControls();
    }

    @Override
    public void onDestroy() {
        renderables.forEach(RenderableObject::destroy);
        objects.stream().forEach(ThreadObject::destroy);
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
