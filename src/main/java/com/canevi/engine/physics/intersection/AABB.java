package com.canevi.engine.physics.intersection;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Vertex;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.physics.HitResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AABB {
    private Vector3f min;
    private Vector3f max;
    public static AABB generateBoundingBox(Mesh mesh) {
        Vector3f min = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        for (Vertex vertex : mesh.getVertices()) {
            Vector3f position = vertex.getPosition();
            if (position.getX() < min.getX()) min.setX(position.getX());
            if (position.getY() < min.getY()) min.setY(position.getY());
            if (position.getZ() < min.getZ()) min.setZ(position.getZ());
            if (position.getX() > max.getX()) max.setX(position.getX());
            if (position.getY() > max.getY()) max.setY(position.getY());
            if (position.getZ() > max.getZ()) max.setZ(position.getZ());
        }

        return new AABB(min, max);
    }

    public Vector3f getExtents() {
        return Vector3f.subtract(max, min).scale(0.5f);
    }

    public static float getDistanceSquared(AABB boundingBox, Vector3f origin) {
        Vector3f closest = origin.projectOnPlane(boundingBox.getMin(), boundingBox.getMax());
        Vector3f extents = boundingBox.getExtents();

        float dx = Math.max(closest.getX() - boundingBox.getMin().getX(), 0);
        float dy = Math.max(closest.getY() - boundingBox.getMin().getY(), 0);
        float dz = Math.max(closest.getZ() - boundingBox.getMin().getZ(), 0);

        dx = Math.min(dx, extents.getX());
        dy = Math.min(dy, extents.getY());
        dz = Math.min(dz, extents.getZ());

        return dx * dx + dy * dy + dz * dz;
    }
    public HitResult getRaycast(Vector3f origin, Vector3f direction, float maxDistance) {
        float tMin = (min.getX() - origin.getX()) / direction.getX();
        float tMax = (max.getX() - origin.getX()) / direction.getX();

        if (tMin > tMax) {
            float temp = tMin;
            tMin = tMax;
            tMax = temp;
        }

        float tYMin = (min.getY() - origin.getY()) / direction.getY();
        float tYMax = (max.getY() - origin.getY()) / direction.getY();


        if (tYMin > tYMax) {
            float temp = tYMin;
            tYMin = tYMax;
            tYMax = temp;
        }

        if (tMin > tYMax || tYMin > tMax) {
            return new HitResult(); // No intersection
        }

        if (tYMin > tMin) {
            tMin = tYMin;
        }

        if (tYMax < tMax) {
            tMax = tYMax;
        }

        float tZMin = (min.getZ() - origin.getZ()) / direction.getZ();
        float tZMax = (max.getZ() - origin.getZ()) / direction.getZ();

        if (tZMin > tZMax) {
            float temp = tZMin;
            tZMin = tZMax;
            tZMax = temp;
        }

        if (tMin > tZMax || tZMin > tMax) {
            return new HitResult(); // No intersection
        }

        if (tZMin > tMin) {
            tMin = tZMin;
        }

        if (tZMax < tMax) {
            tMax = tZMax;
        }

        float tHit = Math.max(tMin, 0); // Ensuring tHit is not negative
        if (tHit < maxDistance && tMax > 0) {
            Vector3f hitPoint = Vector3f.add(origin, direction.scale(tHit));
            Vector3f normal;
            if (tMin == tHit) {
                normal = Vector3f.left();
            } else if (tYMin == tHit) {
                normal = Vector3f.down();
            } else if (tZMin == tHit) {
                normal = Vector3f.back();
            } else if (tMax == tHit) {
                normal = Vector3f.right();
            } else if (tYMax == tHit) {
                normal = Vector3f.up();
            } else {
                normal = Vector3f.forward();
            }
            return new HitResult(true, hitPoint, origin, normal.normalized());
        }
        return new HitResult(); // No intersection within maxDistance
    }
}