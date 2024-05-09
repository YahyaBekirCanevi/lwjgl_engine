package com.canevi.engine.maths;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Transform {
    private Vector3f position, rotation, scale;
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

    public Vector3f forward() {
        return Matrix4f.transform(this).getDirectionVector(Vector3f.forward()).normalized();
    }

    public Vector3f right() {
        return Matrix4f.transform(this).getDirectionVector(Vector3f.right()).normalized();
    }

    public Vector3f up() {
        return Matrix4f.transform(this).getDirectionVector(Vector3f.up()).normalized();
    }

    public void scale(float f) {
        scale = scale.scale(f);
    }
}
