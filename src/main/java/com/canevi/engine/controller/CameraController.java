package com.canevi.engine.controller;

import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Vector2f;
import com.canevi.engine.objects.Camera;

public abstract class CameraController {
    protected Camera camera;
    protected float horizontalAngle = 0, verticalAngle = 0;
    protected Vector2f oldMouse = new Vector2f(0, 0);
    protected Vector2f newMouse = new Vector2f(0, 0);
    protected float moveSpeed = 0.1f, mouseSensitivity = 0.15f, distance = 2.0f;

    public CameraController(Camera camera) {
        this.camera = camera;
    }

    public void setOnFocus(boolean onFocus) {
        camera.setOnFocus(onFocus);
        oldMouse = Input.getMouse();
    }

    public abstract void update();
}
