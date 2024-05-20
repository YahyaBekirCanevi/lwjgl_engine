package com.canevi.engine.controller;

import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Vector2f;
import com.canevi.engine.objects.Camera;

public abstract class CameraController {
    protected Camera camera;
    protected Vector2f oldMouse = new Vector2f(0, 0);
    protected Vector2f newMouse = new Vector2f(0, 0);
    protected float mouseSensitivity = 0.15f;

    public CameraController(Camera camera) {
        this.camera = camera;
    }

    public void setOnFocus(boolean onFocus) {
        camera.setOnFocus(onFocus);
        oldMouse = Input.getMouse();
    }

    public abstract void update();
}
