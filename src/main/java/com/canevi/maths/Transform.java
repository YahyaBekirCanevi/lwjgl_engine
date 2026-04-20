package com.canevi.maths;

public class Transform {
    private Vector3f position, rotation, scale;

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3f getPosition() { return position; }
    public Vector3f getRotation() { return rotation; }
    public Vector3f getScale() { return scale; }

    public void setPosition(Vector3f position) { this.position = position; }
    public void setRotation(Vector3f rotation) { this.rotation = rotation; }
    public void setScale(Vector3f scale) { this.scale = scale; }

    public Transform(Vector3f position) {
        this.position = position;
        this.rotation = Vector3f.zero();
        this.scale = Vector3f.one();
    }

    public Transform(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
        this.scale = Vector3f.one();
    }

    public void update() {
        position.setZ(position.getZ() - 0.05f);
    }

    public Vector3f forward() {
        return Matrix4f.transform(this).getDirectionVector(Vector3f.forward()).normalize();
    }

    public Vector3f right() {
        return Matrix4f.transform(this).getDirectionVector(Vector3f.right()).normalize();
    }

    public Vector3f up() {
        return Matrix4f.transform(this).getDirectionVector(Vector3f.up()).normalize();
    }

    public void scale(float f) {
        scale = scale.scale(f);
    }
}
