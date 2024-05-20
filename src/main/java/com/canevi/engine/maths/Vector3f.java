package com.canevi.engine.maths;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Vector3f {
	private float x, y, z;

	public static Vector3f min(Vector3f minExtents, Vector3f position) {
		return new Vector3f(
				Math.min(minExtents.getX(), position.getX()),
				Math.min(minExtents.getY(), position.getY()),
				Math.min(minExtents.getZ(), position.getZ())
		);
	}

	public static Vector3f max(Vector3f maxExtents, Vector3f position) {
		return new Vector3f(
				Math.max(maxExtents.getX(), position.getX()),
				Math.max(maxExtents.getY(), position.getY()),
				Math.max(maxExtents.getZ(), position.getZ())
		);
	}

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
	 * @return new Vector3f(0, -1, 0);
	 */
	public static Vector3f down() {
		return new Vector3f(0, -1, 0);
	}

	/**
	 * @return new Vector3f(-1, 0, 0);
	 */
	public static Vector3f left() {
		return new Vector3f(-1, 0, 0);
	}

	/**
	 * @return new Vector3f(0, 0, -1);
	 */
	public static Vector3f back() {
		return new Vector3f(0, 0, -1);
	}

	/**
	 * The method creates a new Vector3f object with the negation of the input
	 * vector's x, y, and z components.
	 * 
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

	public Vector3f normalized() {
		float len = length();
		return divide(Vector3f.one().scale(len));
	}

	public float dot(Vector3f other) {
		return x * other.x + y * other.y + z * other.z;
	}

	public Vector3f scale(float length) {
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
	public static Vector3f cross(Vector3f vector1, Vector3f vector2) {
		float x = vector1.getY() * vector2.getZ() - vector1.getZ() * vector2.getY();
		float y = vector1.getZ() * vector2.getX() - vector1.getX() * vector2.getZ();
		float z = vector1.getX() * vector2.getY() - vector1.getY() * vector2.getX();
		return new Vector3f(x, y, z);
	}
	public static Vector3f projectOnPlane(Vector3f vector, Vector3f normal) {
		float dotProduct = vector.dot(normal);
		return Vector3f.subtract(vector, normal.scale(dotProduct));
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
		float range = magnitude + magnitude + 1;
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

	@Override
	public String toString() {
		return "Vector3f{x=" + x +", y=" + y +", z=" + z +"}";
	}
}