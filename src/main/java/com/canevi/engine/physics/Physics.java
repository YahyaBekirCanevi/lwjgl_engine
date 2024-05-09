package com.canevi.engine.physics;

import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.physics.collider.Collider;

public class Physics {
    public static HitResult raycast(Vector3f origin, Vector3f direction, float maxDistance) {
        for (Collider collider : Collider.getColliderList()) {
            return collider.intersectsRay(origin, direction, maxDistance);
        }
        return new HitResult(); // No intersection found
    }

    // SphereCast method
    public static HitResult sphereCast(Vector3f origin, float radius, Vector3f direction, float maxDistance) {
        for (Collider collider : Collider.getColliderList()) {
            return collider.intersectsSphere(origin, radius, direction, maxDistance);
        }
        return new HitResult(); // No intersection found
    }
}
