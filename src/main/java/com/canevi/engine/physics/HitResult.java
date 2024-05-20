package com.canevi.engine.physics;


import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.GameObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HitResult {
    private boolean intersects = false;
    private Vector3f hitPoint = null;
    private Vector3f origin = null;
    private Vector3f normal = null;
    private GameObject gameObject = null;
    public float getDistance() {
        return Vector3f.subtract(origin, hitPoint).length();
    }

    @Override
    public String toString() {
        return "HitResult{" +
                "intersects=" + intersects +
                ", hitPoint=" + hitPoint +
                ", origin=" + origin +
                ", normal=" + normal +
                ", gameObject=" + gameObject +
                '}';
    }
}