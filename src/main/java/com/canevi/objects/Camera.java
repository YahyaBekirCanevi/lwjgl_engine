package com.canevi.objects;

import org.lwjgl.glfw.GLFW;

import com.canevi.io.Input;
import com.canevi.maths.Transform;
import com.canevi.maths.Vector3f;

import lombok.Getter;
import lombok.Setter;

public class Camera {
	@Getter
	private Transform transform;
	@Getter
	@Setter
	private float moveSpeed = 0.05f, mouseSensitivity = 0.15f, distance = 2.0f;
	private float horizontalAngle = 0, verticalAngle = 0;
	private double oldMouseX = 0, oldMouseY = 0, newMouseX, newMouseY;

	public Camera(Transform transform) {
		this.transform = transform;
	}

	public void update() {
		newMouseX = Input.getMouseX();
		newMouseY = Input.getMouseY();

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
			movement = Vector3f.add(transform.forward().scale(ver), transform.right().scale(hor));
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
			transform.getPosition().add(movement);

		float dx = (float) (newMouseX - oldMouseX);
		float dy = (float) (newMouseY - oldMouseY);

		transform.getRotation().add(new Vector3f(-dy, -dx, 0).scale(mouseSensitivity));

		oldMouseX = newMouseX;
		oldMouseY = newMouseY;
	}

	public void update(GameObject object) {
		newMouseX = Input.getMouseX();
		newMouseY = Input.getMouseY();

		Transform objTransform = object.getTransform();

		float dx = (float) (newMouseX - oldMouseX);
		float dy = (float) (newMouseY - oldMouseY);

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

		transform.setPosition(objTransform.getPosition().add(movement));
		transform.setRotation(rotation);

		oldMouseX = newMouseX;
		oldMouseY = newMouseY;
	}
}
