package com.canevi.game.controller;

import com.canevi.engine.controller.CameraController;
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
    }


    private void look() {
        newMouse = Input.getMouse();

        float dx = newMouse.getX() - oldMouse.getX();
        float dy = newMouse.getY() - oldMouse.getY();
        Vector3f rotation = new Vector3f(-dy, -dx, 0);
        rotation = rotation.scale(mouseSensitivity);
        camera.getTransform().getRotation().add(rotation);

        oldMouse = newMouse;
    }

}
