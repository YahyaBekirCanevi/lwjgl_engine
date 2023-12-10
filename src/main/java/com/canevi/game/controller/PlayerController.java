package com.canevi.game.controller;

import org.lwjgl.glfw.GLFW;

import com.canevi.engine.controller.CameraController;
import com.canevi.engine.controller.FirstPersonCameraController;
import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.Camera;
import com.canevi.engine.objects.GameObject;
import com.canevi.engine.objects.ThreadObject;

public class PlayerController extends ThreadObject {

    private Camera camera;
    private CameraController cameraController;

    public PlayerController() {
        super("PlayerController");
        gameObject = new GameObject(null,
                new Transform(Vector3f.zero(), new Vector3f(0f, 0f, 0f)));
    }

    @Override
    public void onStart() {
        camera = Camera.main();
        cameraController = new FirstPersonCameraController(camera);
    }

    @Override
    public void onUpdate() {
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            Input.mouseState(true);
            cameraController.setOnFocus(true);
        } else if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            Input.mouseState(false);
            cameraController.setOnFocus(false);
        }
        cameraController.update();
    }

    @Override
    public void onDestroy() {
    }

}
