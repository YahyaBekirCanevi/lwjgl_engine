package com.canevi.engine.controller;

import org.lwjgl.glfw.GLFW;

import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.Camera;

public class ThirdPersonCameraController extends CameraController {
    private Transform targetTransform;

    public ThirdPersonCameraController(Camera camera, Transform targetTransform) {
        super(camera);
        this.targetTransform = targetTransform;
    }

    @Override
    public void update() {
        if (!camera.isOnFocus())
            return;
        newMouse = Input.getMouse();

        float dx = (float) (newMouse.getX() - oldMouse.getX());
        float dy = (float) (newMouse.getY() - oldMouse.getY());

        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            verticalAngle -= dy * mouseSensitivity;
            horizontalAngle += dx * mouseSensitivity;
        }
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_RIGHT)) {
            if (distance > 0) {
                distance += dy * mouseSensitivity / 4;
            } else {
                distance = 0.1f;
            }
        }

        float horizontalDistance = (float) (distance * Math.cos(Math.toRadians(verticalAngle)));
        float verticalDistance = (float) (distance * Math.sin(Math.toRadians(verticalAngle)));

        float xOffset = (float) (horizontalDistance * Math.sin(Math.toRadians(-horizontalAngle)));
        float zOffset = (float) (horizontalDistance * Math.cos(Math.toRadians(-horizontalAngle)));

        Vector3f movement = new Vector3f(xOffset, -verticalDistance, zOffset);

        Vector3f rotation = new Vector3f(verticalAngle, -horizontalAngle, 0);

        cam.setPosition(targetTransform.getPosition().add(movement));
        cam.setRotation(rotation);

        oldMouse = newMouse;
    }

}
