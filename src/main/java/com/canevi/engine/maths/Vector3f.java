package com.canevi.engine.maths;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Vector3f {
	private float x, y, z;

	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	/**
	 * @return new Vector3f(0, 0, 0);
	 */
	public static Vector3f zero() {
		return new Vector3f(0, 0, 0);
	}

	/**
	 * @return new Vector3f(1, 1, 1);
	 */
	public static Vector3f one() {
		return new Vector3f(1, 1, 1);
	}

	/**
	 * @return new Vector3f(0, 1, 0);
	 */
	public static Vector3f up() {
		return new Vector3f(0, 1, 0);
	}

	/**
	 * @return new Vector3f(1, 0, 0);
	 */
	public static Vector3f right() {
		return new Vector3f(1, 0, 0);
	}

	/**
	 * @return new Vector3f(0, 0, 1);
	 */
	public static Vector3f forward() {
		return new Vector3f(0, 0, 1);
	}

	/**
	 * The method creates a new Vector3f object with the negation of the input
	 * vector's x, y, and z components.
	 * 
	 * @param vector
	 * @return new Vector3f(-vector.getX(), -vector.getY(), -vector.getZ());
	 */
	public Vector3f inverse() {
		return new Vector3f(-x, -y, -z);
	}

	public Vector3f add(Vector3f other) {
		return set(x + other.x, y + other.y, z + other.z);
	}

	public Vector3f subtract(Vector3f other) {
		return set(x - other.x, y - other.y, z - other.z);
	}

	public Vector3f multiply(Vector3f other) {
		return set(x * other.x, y * other.y, z * other.z);
	}

	public Vector3f divide(Vector3f other) {
		return set(x / other.x, y / other.y, z / other.z);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z);
	}

	public Vector3f normalize() {
		float len = length();
		return divide(Vector3f.one().scale(len));
	}

	public float dot(Vector3f other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public Vector3f scale(Float length) {
		return new Vector3f(x * length, y * length, z * length);
	}

	public Vector3f scale(Vector3f scaler) {
		return new Vector3f(x * scaler.x, y * scaler.y, z * scaler.z);
	}

	public static Vector3f add(Vector3f vector1, Vector3f vector2) {
		if (vector1 == null || vector2 == null)
			return null;
		return new Vector3f(vector1.getX() + vector2.getX(), vector1.getY() + vector2.getY(),
				vector1.getZ() + vector2.getZ());
	}

	public static Vector3f subtract(Vector3f vector1, Vector3f vector2) {
		if (vector1 == null || vector2 == null)
			return null;
		return new Vector3f(vector1.getX() - vector2.getX(), vector1.getY() - vector2.getY(),
				vector1.getZ() - vector2.getZ());
	}

	public static Vector3f multiply(Vector3f vector1, Vector3f vector2) {
		if (vector1 == null || vector2 == null)
			return null;
		return new Vector3f(vector1.getX() * vector2.getX(), vector1.getY() * vector2.getY(),
				vector1.getZ() * vector2.getZ());
	}

	public static Vector3f divide(Vector3f vector1, Vector3f vector2) {
		if (vector1 == null || vector2 == null)
			return null;
		return new Vector3f(vector1.getX() / vector2.getX(), vector1.getY() / vector2.getY(),
				vector1.getZ() / vector2.getZ());
	}

	public static float dot(Vector3f vector1, Vector3f vector2) {
		if (vector1 == null || vector2 == null)
			return 0f;
		return vector1.getX() * vector2.getX() + vector1.getY() * vector2.getY() + vector1.getZ() * vector2.getZ();
	}

	public static Vector3f nonNull(Vector3f vector) {
		return vector == null ? Vector3f.zero() : vector;
	}

	public static Vector3f randomPositionInsideSphere(Vector3f center, float radius) {
		Vector3f random = randomPosition(radius);
		return new Vector3f(center.getX() + random.getX() * radius, center.getY() + random.getY() * radius,
				center.getZ() + random.getZ() * radius);
	}

	public static Vector3f randomPosition(float magnitude) {
		float range = magnitude - (-magnitude) + 1;
		float x = -magnitude + (float) Math.random() * range;
		float y = -magnitude + (float) Math.random() * range;
		float z = -magnitude + (float) Math.random() * range;
		return new Vector3f(x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3f other = (Vector3f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
}