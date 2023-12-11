package com.canevi.engine.objects;

import com.canevi.engine.maths.Matrix4f;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.maths.Vector3f;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Camera extends TObject {
	private boolean onFocus = false;
	private float fieldOfView = 70.0f;
	private float aspect = 16.0f / 9.0f;
	private float near = 0.1f;
	private float far = 1000.0f;

	private static Camera mainCamera = null;

	/**
	 * @return main camera
	 */
	public static Camera main() {
		if (mainCamera == null) {
			mainCamera = new Camera(null,
					new Transform(new Vector3f(0f, 0f, -50f), new Vector3f(0f, 90f, 0f)));
		}
		return mainCamera;
	}

	public Camera(TObject parent, Transform transform) {
		super(transform);
	}

	public void setAspectOfView(float width, float height) {
		this.aspect = width / height;
	}

	public Matrix4f getProjectionMatrix() {
		return Matrix4f.projection(fieldOfView, aspect, near, far);
	}

}
