package com.canevi.engine.physics.collider;

import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.physics.HitResult;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class Collider {
    @Getter
    protected static List<Collider> colliderList = new ArrayList<>();

    public Collider() {
        colliderList.add(this);
    }

    public abstract HitResult intersectsRay(Vector3f origin, Vector3f direction, float maxDistance);

    public abstract HitResult intersectsSphere(Vector3f origin, float radius, Vector3f direction, float maxDistance);
}