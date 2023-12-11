package com.canevi.engine.maths;

import java.util.Arrays;

import org.lwjgl.assimp.AIMatrix4x4;

import com.canevi.engine.objects.Camera;

public class Matrix4f extends Matrixf {
	public static final int SIZE = 4;

	public Matrix4f() {
		super(SIZE, SIZE);
	}

	public static Matrix4f identity() {
		Matrix4f result = Matrix4f.filled(0);
		result.set(0, 0, 1);
		result.set(1, 1, 1);
		result.set(2, 2, 1);
		result.set(3, 3, 1);
		return result;
	}

	public static Matrix4f translate(Vector3f translate) {
		Matrix4f result = Matrix4f.identity();

		result.set(3, 0, translate.getX());
		result.set(3, 1, translate.getY());
		result.set(3, 2, translate.getZ());

		return result;
	}

	public static Matrix4f rotate(float angle, Vector3f axis) {
		Matrix4f result = Matrix4f.identity();

		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));
		float C = 1 - cos;

		result.set(0, 0, cos + axis.getX() * axis.getX() * C);
		result.set(0, 1, axis.getX() * axis.getY() * C - axis.getZ() * sin);
		result.set(0, 2, axis.getX() * axis.getZ() * C + axis.getY() * sin);
		result.set(1, 0, axis.getY() * axis.getX() * C + axis.getZ() * sin);
		result.set(1, 1, cos + axis.getY() * axis.getY() * C);
		result.set(1, 2, axis.getY() * axis.getZ() * C - axis.getX() * sin);
		result.set(2, 0, axis.getZ() * axis.getX() * C - axis.getY() * sin);
		result.set(2, 1, axis.getZ() * axis.getY() * C + axis.getX() * sin);
		result.set(2, 2, cos + axis.getZ() * axis.getZ() * C);

		return result;
	}

	public static Matrix4f rotate(Vector3f rotation) {
		Matrix4f rotationX = rotateX(rotation.getX());
		Matrix4f rotationY = rotateY(rotation.getY());
		Matrix4f rotationZ = rotateZ(rotation.getZ());

		return rotationZ.multiply(rotationY).multiply(rotationX);
	}

	public static Matrix4f rotateX(float angle) {
		Matrix4f result = Matrix4f.identity();

		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));

		result.set(1, 1, cos);
		result.set(1, 2, -sin);
		result.set(2, 1, sin);
		result.set(2, 2, cos);

		return result;
	}

	public static Matrix4f rotateY(float angle) {
		Matrix4f result = Matrix4f.identity();

		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));

		result.set(0, 0, cos);
		result.set(0, 2, sin);
		result.set(2, 0, -sin);
		result.set(2, 2, cos);

		return result;
	}

	public static Matrix4f rotateZ(float angle) {
		Matrix4f result = Matrix4f.identity();

		float cos = (float) Math.cos(Math.toRadians(angle));
		float sin = (float) Math.sin(Math.toRadians(angle));

		result.set(0, 0, cos);
		result.set(0, 1, -sin);
		result.set(1, 0, sin);
		result.set(1, 1, cos);

		return result;
	}

	public static Matrix4f scale(Vector3f scalar) {
		Matrix4f result = Matrix4f.identity();

		result.set(0, 0, scalar.getX());
		result.set(1, 1, scalar.getY());
		result.set(2, 2, scalar.getZ());

		return result;
	}

	public static Matrix4f transform(Transform transform) {
		Matrix4f result = Matrix4f.identity();

		Matrix4f translationMatrix = Matrix4f.translate(transform.getPosition());
		Matrix4f rotationMatrix = Matrix4f.rotate(transform.getRotation());
		Matrix4f scaleMatrix = Matrix4f.scale(transform.getScale());

		result = Matrix4f.multiply(translationMatrix, Matrix4f.multiply(rotationMatrix, scaleMatrix));

		return result;
	}

	public Vector3f translateVector3f(Vector3f vector) {
		return new Vector3f(
				get(0, 0) * vector.getX() + get(0, 1) * vector.getY() + get(0, 2) * vector.getZ() + get(0, 3),
				get(1, 0) * vector.getX() + get(1, 1) * vector.getY() + get(1, 2) * vector.getZ() + get(1, 3),
				get(2, 0) * vector.getX() + get(2, 1) * vector.getY() + get(2, 2) * vector.getZ() + get(2, 3));
	}

	public static Matrix4f projection(float fov, float aspect, float near, float far) {
		Matrix4f result = Matrix4f.identity();

		float tanFOV = (float) Math.tan(Math.toRadians(fov / 2));
		float range = far - near;

		result.set(0, 0, 1.0f / (aspect * tanFOV));
		result.set(1, 1, 1.0f / tanFOV);
		result.set(2, 2, -((far + near) / range));
		result.set(2, 3, -1.0f);
		result.set(3, 2, -((2 * far * near) / range));
		result.set(3, 3, 0.0f);

		return result;
	}

	public static Matrix4f view(Camera camera) {
		Matrix4f result = Matrix4f.identity();

		Matrix4f translationMatrix = Matrix4f.translate(camera.getTransform().getPosition().inverse());

		Matrix4f rotationMatrix = Matrix4f.rotate(camera.getTransform().getRotation());

		result = Matrix4f.multiply(translationMatrix, rotationMatrix);

		return result;
	}

	public static Matrix4f multiply(Matrix4f matrix, Matrix4f other) {
		Matrix4f result = Matrix4f.identity();

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				result.set(i, j, matrix.get(i, 0) * other.get(0, j) +
						matrix.get(i, 1) * other.get(1, j) +
						matrix.get(i, 2) * other.get(2, j) +
						matrix.get(i, 3) * other.get(3, j));
			}
		}

		return result;
	}

	public Matrix4f multiply(Matrix4f other) {
		return Matrix4f.multiply(this, other);
	}

	public static Matrix4f filled(float fill) {
		Matrix4f result = new Matrix4f();

		float[] array = new float[SIZE * SIZE];
		Arrays.fill(array, fill);
		result.setAll(array);
		return result;
	}

	public Vector3f getDirectionVector(Vector3f direction) {
		return direction == null ? null
				: new Vector3f(
						get(0, 0) * direction.getX() + get(0, 1) * direction.getY() + get(0, 2) * direction.getZ(),
						get(1, 0) * direction.getX() + get(1, 1) * direction.getY() + get(1, 2) * direction.getZ(),
						get(2, 0) * direction.getX() + get(2, 1) * direction.getY() + get(2, 2) * direction.getZ());
	}

	public static Matrix4f fromAssimpMatrix4x4(AIMatrix4x4 mTransformation) {
		Matrix4f result = new Matrix4f();

		result.set(0, 0, mTransformation.a1());
		result.set(1, 0, mTransformation.b1());
		result.set(2, 0, mTransformation.c1());
		result.set(3, 0, mTransformation.d1());
		result.set(0, 1, mTransformation.a2());
		result.set(1, 1, mTransformation.b2());
		result.set(2, 1, mTransformation.c2());
		result.set(3, 1, mTransformation.d2());
		result.set(0, 2, mTransformation.a3());
		result.set(1, 2, mTransformation.b3());
		result.set(2, 2, mTransformation.c3());
		result.set(3, 2, mTransformation.d3());
		result.set(0, 3, mTransformation.a4());
		result.set(1, 3, mTransformation.b4());
		result.set(2, 3, mTransformation.c4());
		result.set(3, 3, mTransformation.d4());

		return result;
	}
}