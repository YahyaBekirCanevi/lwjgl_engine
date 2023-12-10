package com.canevi.engine.controller;

import org.lwjgl.glfw.GLFW;

import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.Camera;

public class FirstPersonCameraController extends CameraController {
    public FirstPersonCameraController(Camera camera) {
        super(camera);
    }

    @Override
    public void update() {
        if (!camera.isOnFocus())
            return;
        look();
        movement();
    }

    private void look() {
        newMouse = Input.getMouse();

        float dx = (float) (newMouse.getX() - oldMouse.getX());
        float dy = (float) (newMouse.getY() - oldMouse.getY());
        Vector3f rotation = new Vector3f(-dy, -dx, 0);
        rotation = rotation.scale(mouseSensitivity);
        cam.getRotation().add(rotation);

        oldMouse = newMouse;
    }

    private void movement() {
        float ver = 0.0f, hor = 0.0f;
        Vector3f movement = Vector3f.zero();

        if (Input.isKeyDown(GLFW.GLFW_KEY_A) || Input.isKeyDown(GLFW.GLFW_KEY_UP))
            hor += -1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_D) || Input.isKeyDown(GLFW.GLFW_KEY_DOWN))
            hor += 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_W) || Input.isKeyDown(GLFW.GLFW_KEY_LEFT))
            ver += -1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_S) || Input.isKeyDown(GLFW.GLFW_KEY_RIGHT))
            ver += 1;
        if (ver != 0 || hor != 0) {
            movement = Vector3f.add(cam.forward().scale(ver), cam.right().scale(hor));
            movement.setY(0);
            movement = movement.normalize().scale(moveSpeed);
            if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                movement = movement.scale(3f);
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE))
            movement.add(new Vector3f(0, moveSpeed, 0));
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
            movement.add(new Vector3f(0, -moveSpeed, 0));

        if (movement != null)
            cam.getPosition().add(movement);
    }

}
