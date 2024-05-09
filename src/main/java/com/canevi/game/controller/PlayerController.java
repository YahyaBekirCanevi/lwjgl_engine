package com.canevi.game.controller;

import com.canevi.engine.physics.CharacterCollision;
import com.canevi.engine.physics.collider.BoxCollider;
import com.canevi.engine.utils.MeshExamples;
import org.lwjgl.glfw.GLFW;

import com.canevi.engine.controller.CameraController;
import com.canevi.engine.io.Input;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.Camera;
import com.canevi.engine.objects.GameObject;
import com.canevi.engine.objects.ThreadObject;

public class PlayerController extends ThreadObject {

    private BoxCollider boxCollider;
    private CharacterCollision characterCollision;
    private Camera camera;
    private CameraController cameraController;

    private float moveSpeed = 0.1f;

    public PlayerController() {
        super("PlayerController");
        gameObject = new GameObject(null,
                new Transform(Vector3f.zero(), new Vector3f(0f, 0f, 0f)));
        boxCollider = new BoxCollider(MeshExamples.generateCube("textures/wooden-box.png"));
        characterCollision = new CharacterCollision(boxCollider);
    }

    @Override
    public void onStart() {
        camera = Camera.getMainCamera();
        cameraController = new FirstPersonCameraController(camera, this);
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
        firstPersonMovement();
    }

    @Override
    public void onDestroy() {
    }

    private void firstPersonMovement() {

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

        if (movement != null)
            camera.getTransform().getPosition().add(movement);
    }
    private void freeCameraMovement() {
        float ver = 0.0f, hor = 0.0f;
        Vector3f movement = Vector3f.zero();

        if (Input.isKeyDown(GLFW.GLFW_KEY_A) || Input.isKeyDown(GLFW.GLFW_KEY_UP))
            hor -= 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_D) || Input.isKeyDown(GLFW.GLFW_KEY_DOWN))
            hor += 1;
        if (Input.isKeyDown(GLFW.GLFW_KEY_W) || Input.isKeyDown(GLFW.GLFW_KEY_LEFT))
            ver -= 1;
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

        if (movement != null)
            camera.getTransform().getPosition().add(movement);
    }

}
