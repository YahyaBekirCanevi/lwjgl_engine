package com.canevi.game.controller;

import com.canevi.engine.controller.CameraController;
import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.Camera;

public class FirstPersonCameraController extends CameraController {
    private final PlayerController playerController;
    public FirstPersonCameraController(Camera camera, PlayerController playerController) {
        super(camera);
        this.playerController = playerController;
    }

    @Override
    public void update() {
        if (!camera.isOnFocus())
            return;
        look();
        moveToPlayer();
    }

    private void moveToPlayer() {
    }

    private void look() {
        newMouse = Input.getMouse();

        float dx = (float) (newMouse.getX() - oldMouse.getX());
        float dy = (float) (newMouse.getY() - oldMouse.getY());
        Vector3f rotation = new Vector3f(-dy, -dx, 0);
        rotation = rotation.scale(mouseSensitivity);
        camera.getTransform().getRotation().add(rotation);

        oldMouse = newMouse;
    }

}
