package com.canevi.engine.physics;

import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.physics.collider.BoxCollider;

public class CharacterCollision {
    private int maxBounces = 5;
    private Float skinWidth = 0.015f;
    private final BoxCollider collider;

    public CharacterCollision(BoxCollider collider) {
        this.collider = collider;
    }
    public Vector3f collideAndSlide(Vector3f velocity, Vector3f position, int depth) {
        if (depth >= maxBounces) {
            return Vector3f.zero();
        }
        Float distance = velocity.length() + skinWidth;

        HitResult hit = Physics.sphereCast(position, collider.getBoundingBox().getExtents().getX(), velocity.normalized(), distance);
        if(hit.isIntersects()) {
            Vector3f snapToSurface = velocity.normalized().scale(hit.getDistance() - skinWidth);
            Vector3f leftOver = velocity.subtract(snapToSurface);

            Float magnitude = leftOver.length();
            leftOver = Vector3f.projectOnPlane(leftOver, hit.getNormal()).normalized();
            leftOver = leftOver.scale(magnitude);

            return snapToSurface.add(collideAndSlide(leftOver,position.add(snapToSurface), depth+1));
        }
        return velocity;
    }
}
