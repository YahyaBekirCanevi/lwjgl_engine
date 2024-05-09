package com.canevi.engine.physics.collider;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.RenderableObject;
import com.canevi.engine.physics.intersection.AABB;
import com.canevi.engine.physics.HitResult;
import lombok.Getter;

public class BoxCollider extends Collider {
    @Getter
    private AABB boundingBox;
    private final Mesh mesh;

    public BoxCollider(Mesh mesh) {
        this.boundingBox = AABB.generateBoundingBox(mesh);
        this.mesh = mesh;
        colliderList.add(this);
    }

    public HitResult intersectsRay(Vector3f origin, Vector3f direction, float maxDistance) {
        if (mesh == null) {
            return new HitResult();
        }
        if(boundingBox == null) {
            boundingBox = AABB.generateBoundingBox(mesh);
        }
        return boundingBox.getRaycast(origin, direction, maxDistance);
    }

    public HitResult intersectsSphere(Vector3f origin, float radius, Vector3f direction, float maxDistance) {
        if (mesh == null) {
            return new HitResult();
        }
        if(boundingBox == null) {
            boundingBox = AABB.generateBoundingBox(mesh);
        }
        float distanceSquared = AABB.getDistanceSquared(boundingBox, origin);
        if (distanceSquared > radius * radius) {
            return new HitResult(); // No intersection
        }
        return intersectsRay(origin, direction, maxDistance);
    }
}