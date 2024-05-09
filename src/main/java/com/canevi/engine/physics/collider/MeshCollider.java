package com.canevi.engine.physics.collider;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Vertex;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.RenderableObject;
import com.canevi.engine.physics.HitResult;
import com.canevi.engine.physics.intersection.MeshIntersection;
import lombok.Getter;

public class MeshCollider extends Collider{
    @Getter
    private MeshIntersection boundingBox;
    private final RenderableObject renderableObject;

    public MeshCollider(RenderableObject renderableObject) {
        this.boundingBox = MeshIntersection.generateBoundingBox(renderableObject.getMesh());
        this.renderableObject = renderableObject;
        colliderList.add(this);
    }

    @Override
    public HitResult intersectsRay(Vector3f origin, Vector3f direction, float maxDistance) {
        Mesh mesh = renderableObject.getMesh();

        HitResult closestHit = new HitResult();

        // Iterate through each triangle in the mesh
        for (int i = 0; i < mesh.getIndices().length; i += 3) {
            int i0 = mesh.getIndices()[i];
            int i1 = mesh.getIndices()[i + 1];
            int i2 = mesh.getIndices()[i + 2];

            Vertex v0 = mesh.getVertices()[i0];
            Vertex v1 = mesh.getVertices()[i1];
            Vertex v2 = mesh.getVertices()[i2];

            // Perform ray-triangle intersection test
            Vector3f e1 = Vector3f.subtract(v1.getPosition(), v0.getPosition());
            Vector3f e2 = Vector3f.subtract(v2.getPosition(), v0.getPosition());
            Vector3f p = Vector3f.cross(direction, e2);
            float det = Vector3f.dot(e1, p);

            // Check if ray and triangle are parallel
            if (det == 0)
                continue;

            float invDet = 1.0f / det;

            Vector3f t = Vector3f.subtract(origin, v0.getPosition());
            float u = Vector3f.dot(t, p) * invDet;
            if (u < 0 || u > 1)
                continue;

            Vector3f q = Vector3f.cross(t, e1);
            float v = Vector3f.dot(direction, q) * invDet;
            if (v < 0 || u + v > 1)
                continue;

            float tHit = Vector3f.dot(e2, q) * invDet;

            // Check if intersection is within maxDistance
            if (tHit > 0 && tHit < maxDistance) {
                Vector3f hitPoint = Vector3f.add(origin, direction.scale(tHit));
                Vector3f normal = Vector3f.cross(e1, e2).normalized(); // Triangle normal
                closestHit = new HitResult(true, hitPoint, origin, normal);
                maxDistance = tHit; // Update maxDistance for closer intersection
            }
        }

        return closestHit;
    }

    @Override
    public HitResult intersectsSphere(Vector3f origin, float radius, Vector3f direction, float maxDistance) {
        Vector3f oc = Vector3f.subtract(origin, renderableObject.getTransform().getPosition());
        float a = Vector3f.dot(direction, direction);
        float b = 2.0f * Vector3f.dot(oc, direction);
        float c = Vector3f.dot(oc, oc) - radius * radius;
        float discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            // No intersection
            return new HitResult();
        } else {
            float t = (-b - (float) Math.sqrt(discriminant)) / (2.0f * a);
            if (t < 0 || t > maxDistance) {
                // Intersection point is behind the ray origin or beyond max distance
                return new HitResult();
            } else {
                Vector3f hitPoint = Vector3f.add(origin, direction.scale(t));
                Vector3f normal = Vector3f.subtract(hitPoint, renderableObject.getTransform().getPosition()).normalized();
                return new HitResult(true, hitPoint, origin, normal);
            }
        }
    }
}
