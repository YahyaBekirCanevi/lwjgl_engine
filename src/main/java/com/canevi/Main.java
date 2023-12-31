package com.canevi;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.canevi.graphics.Mesh;
import com.canevi.graphics.Shader;
import com.canevi.io.Input;
import com.canevi.io.Window;
import com.canevi.maths.Transform;
import com.canevi.maths.Vector3f;
import com.canevi.objects.Camera;
import com.canevi.objects.GameObject;
import com.canevi.utils.MeshExamples;

public class Main implements Runnable {
	public Thread game;
	public Window window;
	public final int WIDTH = 1280, HEIGHT = 760;

	public Shader shader;

	public GameObject object;

	public Camera camera;

	public void start() {
		game = new Thread(this, "game");
		game.start();
	}

	public void init() {
		window = new Window(WIDTH, HEIGHT, "Game");
		window.setBackgroundColor(0.1f, 0.2f, 0.3f);
		camera = new Camera(new Transform(new Vector3f(0f, 0f, -50f), new Vector3f(0f, 90f, 0f)));
		camera.setMoveSpeed(.1f);

		window.create();

		shader = new Shader("shaders/mainVertex.glsl", "shaders/mainFragment.glsl");
		shader.create();
		List<Mesh> meshList = new ArrayList<>();
		// meshList.add(MeshExamples.generatePlane("sf/sea-keep-lonely-watcher/textures/texture_building.jpeg"));
		// meshList.add(MeshExamples.watcherPart(0, 0));
		meshList.addAll(MeshExamples.watcher());
		// meshList.add(MeshExamples.dragon());
		// meshList.add(MeshExamples.generateCube("textures/wooden-box.png"));
		object = new GameObject(new Transform(Vector3f.zero(), new Vector3f(90f, 0f, 0f)), meshList, window, shader);
		object.getTransform().scale(.1f);
		object.create();
	}

	public void run() {
		init();
		while (!window.shouldClose()) {
			update();
			render();
			if (Input.isKeyDown(GLFW.GLFW_KEY_F11))
				window.setFullscreen(!window.isFullscreen());
			if (Input.isButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
				window.mouseState(true);
				camera.setOnFocus(true);
			} else if (Input.isKeyDown(GLFW.GLFW_KEY_ESCAPE)) {
				window.mouseState(false);
				camera.setOnFocus(false);
			}
		}
		close();
	}

	private void update() {
		window.update();
		camera.update();

		if (Input.isKeyDown(GLFW.GLFW_KEY_L)) {
			object.getTransform().scale(95f / 100);
		} else if (Input.isKeyDown(GLFW.GLFW_KEY_M)) {
			object.getTransform().scale(100f / 95);
		}
	}

	private void render() {
		object.renderObject(camera);
		window.swapBuffers();
	}

	private void close() {
		window.destroy();
		object.destroy();
		shader.destroy();
	}

	public static void main(String[] args) {
		new Main().start();
	}
}