package com.canevi.game.controller;

import com.canevi.engine.Component;
import com.canevi.engine.controller.CameraController;
import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.Camera;
import com.canevi.engine.physics.HitResult;
import com.canevi.engine.physics.Physics;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@Slf4j
public class PlayerController extends Component {
    private Camera camera;
    private CameraController cameraController;
    private float moveSpeed = 0.1f;
    public PlayerController() {

    }

    @Override
    public void onStart() {
        camera = Camera.getMainCamera();
        cameraController = new FirstPersonCameraController(camera);
    }

    boolean isPressed = false;

    @Override
    public void onUpdate() {
        if (!isPressed && Input.isKeyDown(GLFW.GLFW_KEY_L)) {
            isPressed = true;
            HitResult hitResult = Physics.raycast(Vector3f.zero(),Vector3f.right(), 100);
            log.info("Hit Result: {}",hitResult);
        }
        if (!Input.isKeyDown(GLFW.GLFW_KEY_L)) isPressed = false;
        if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
            Input.mouseState(true);
            cameraController.setOnFocus(true);
        } else if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
            Input.mouseState(false);
            cameraController.setOnFocus(false);
        }
        cameraController.update();
        firstPersonMovement();
    }

    @Override
    public void onDestroy() {
    }

    private void freeCameraMovement() {
        // TODO edit

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
            movement = Vector3f.add(camera.getTransform().forward().scale(ver), camera.getTransform().right().scale(hor));
            movement.setY(0);
            movement = movement.normalized().scale(moveSpeed);
            if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                movement = movement.scale(3f);
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE))
            movement.add(new Vector3f(0, moveSpeed, 0));
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
            movement.add(new Vector3f(0, -moveSpeed, 0));

        if (movement != null) {
            camera.getTransform().getPosition().add(movement);
        }
    }

    private void firstPersonMovement() {
        float ver = 0.0f, hor = 0.0f;
        Vector3f velocity = Vector3f.zero();

        if (Input.isKeyDown(GLFW.GLFW_KEY_A) || Input.isKeyDown(GLFW.GLFW_KEY_UP))
            hor -= 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_D) || Input.isKeyDown(GLFW.GLFW_KEY_DOWN))
            hor += 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_W) || Input.isKeyDown(GLFW.GLFW_KEY_LEFT))
            ver -= 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_S) || Input.isKeyDown(GLFW.GLFW_KEY_RIGHT))
            ver += 1;
        if (ver != 0 || hor != 0) {
            velocity = Vector3f.add(camera.getTransform().forward().scale(ver), camera.getTransform().right().scale(hor));
            velocity.setY(0);
            velocity = velocity.normalized().scale(moveSpeed);
            if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_SHIFT)) {
                velocity = velocity.scale(3f);
            }
        }
        if (Input.isKeyDown(GLFW.GLFW_KEY_SPACE))
            velocity.add(new Vector3f(0, moveSpeed, 0));
        if (Input.isKeyDown(GLFW.GLFW_KEY_LEFT_CONTROL))
            velocity.add(new Vector3f(0, -moveSpeed, 0));

        if (Objects.equals(velocity, Vector3f.zero())) return;
        camera.getTransform().getPosition().add(velocity);
    }

}
