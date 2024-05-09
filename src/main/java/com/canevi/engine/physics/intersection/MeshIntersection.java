package com.canevi.engine.physics.intersection;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Vertex;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.physics.HitResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class MeshIntersection {
    private Vector3f min;
    private Vector3f max;
    private final List<Triangle> triangles;

    public MeshIntersection(Mesh mesh) {
        this.triangles = generateTriangles(mesh);
    }
    private List<Triangle> generateTriangles(Mesh mesh) {
        List<Triangle> triangles = new ArrayList<>();
        for (int i = 0; i < mesh.getIndices().length; i += 3) {
            int i0 = mesh.getIndices()[i];
            int i1 = mesh.getIndices()[i + 1];
            int i2 = mesh.getIndices()[i + 2];

            Vertex v0 = mesh.getVertices()[i0];
            Vertex v1 = mesh.getVertices()[i1];
            Vertex v2 = mesh.getVertices()[i2];

            triangles.add(new Triangle(v0, v1, v2));
        }
        return triangles;
    }
    public static MeshIntersection generateBoundingBox(Mesh mesh) {
        Vector3f min = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
        Vector3f max = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

        Arrays.stream(mesh.getVertices()).map(Vertex::getPosition).forEach(position -> {
            min.set(Math.min(min.getX(), position.getX()), Math.min(min.getY(), position.getY()), Math.min(min.getZ(), position.getZ()));
            max.set(Math.max(max.getX(), position.getX()), Math.max(max.getY(), position.getY()), Math.max(max.getZ(), position.getZ()));
        });
        MeshIntersection meshIntersection = new MeshIntersection(mesh);
        meshIntersection.setMax(max);
        meshIntersection.setMin(min);
        return meshIntersection;
    }
    public HitResult getRaycast(Vector3f origin, Vector3f direction, float maxDistance) {
        HitResult closestHit = new HitResult();
        for (Triangle triangle : triangles) {
            HitResult hitResult = triangle.intersectsRay(origin, direction, maxDistance);
            if (hitResult.isIntersects() && (!closestHit.isIntersects() || hitResult.getDistance() < closestHit.getDistance())) {
                closestHit = hitResult;
            }
        }
        return closestHit;
    }
    @AllArgsConstructor
    private static class Triangle {
        private final Vertex v0;
        private final Vertex v1;
        private final Vertex v2;

        public HitResult intersectsRay(Vector3f origin, Vector3f direction, float maxDistance) {
            Vector3f edge1 = Vector3f.subtract(v1.getPosition(), v0.getPosition());
            Vector3f edge2 = Vector3f.subtract(v2.getPosition(), v0.getPosition());
            Vector3f h = Vector3f.cross(direction, edge2);
            float a = Vector3f.dot(edge1, h);

            if (a > -0.00001f && a < 0.00001f) {
                return new HitResult(); // Ray is parallel to the triangle
            }

            float f = 1.0f / a;
            Vector3f s = Vector3f.subtract(origin, v0.getPosition());
            float u = f * Vector3f.dot(s, h);

            if (u < 0.0f || u > 1.0f) {
                return new HitResult();
            }

            Vector3f q = Vector3f.cross(s, edge1);
            float v = f * Vector3f.dot(direction, q);

            if (v < 0.0f || u + v > 1.0f) {
                return new HitResult();
            }

            float t = f * Vector3f.dot(edge2, q);

            if (t > 0.00001f && t < maxDistance) {
                Vector3f hitPoint = Vector3f.add(origin, direction.scale(t));
                Vector3f normal = Vector3f.cross(edge1, edge2).normalized(); // Triangle normal
                return new HitResult(true, hitPoint, origin, normal);
            }

            return new HitResult(); // No intersection within maxDistance
        }
    }
}
