package com.canevi.engine.physics;

import com.canevi.engine.graphics.Mesh;
import com.canevi.engine.graphics.Vertex;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.RenderableObject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class Physics {
    public static HitResult raycast(Vector3f rayOrigin, Vector3f rayDirection, float maxDistance) {
        float closestDistance = Float.POSITIVE_INFINITY;

        // Iterate through game items
        for (RenderableObject gameObject : RenderableObject.getList()) {
            Vector3f min = gameObject.getTransform().getPosition();
            Vector3f max = gameObject.getTransform().getPosition();

            min = Vector3f.add(min,gameObject.getMinExtents());
            max = Vector3f.add(max,gameObject.getMaxExtents());
            log.info("Ray(origin={}, direction={}) \ngameObject={}", rayOrigin, rayDirection, gameObject);

            // Check if the AABB of the game item is within the ray's reach
            if (isAABBWithinRayReach(rayOrigin, rayDirection, maxDistance, min, max)) {
                log.info("AABB in reach ");
                // Perform SAT test between ray and game item
                return detailedMeshCollision(gameObject, rayOrigin, rayDirection, closestDistance);
            }
        }

        return new HitResult();
    }
    public static boolean isAABBWithinRayReach(Vector3f rayOrigin, Vector3f rayDirection, float maxDistance, Vector3f min, Vector3f max) {
        // Avoid division by zero by ensuring direction components are non-zero
        float invDirX = 1.0f / (rayDirection.getX() != 0.0f ? rayDirection.getX() : Float.MIN_VALUE);
        float invDirY = 1.0f / (rayDirection.getY() != 0.0f ? rayDirection.getY() : Float.MIN_VALUE);
        float invDirZ = 1.0f / (rayDirection.getZ() != 0.0f ? rayDirection.getZ() : Float.MIN_VALUE);

        float t1 = (min.getX() - rayOrigin.getX()) * invDirX;
        float t2 = (max.getX() - rayOrigin.getX()) * invDirX;
        float t3 = (min.getY() - rayOrigin.getY()) * invDirY;
        float t4 = (max.getY() - rayOrigin.getY()) * invDirY;
        float t5 = (min.getZ() - rayOrigin.getZ()) * invDirZ;
        float t6 = (max.getZ() - rayOrigin.getZ()) * invDirZ;

        float tMin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
        float tMax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));

        // If tMax < 0, ray is intersecting AABB but the whole AABB is behind the ray
        // If tMin > tMax, ray doesn't intersect AABB
        if (tMax < 0 || tMin > tMax) {
            return false;
        }

        // Check if the intersection point is within the maxDistance
        return tMin <= maxDistance;
    }
    public static HitResult detailedMeshCollision(RenderableObject renderableObject, Vector3f origin, Vector3f direction, Float radius) {
        HitResult closestHit = new HitResult();
        float closestDistance = Float.MAX_VALUE;
        Mesh mesh = renderableObject.getMesh();
        Vector3f position = renderableObject.getTransform().getPosition();
        Vector3f scale = renderableObject.getTransform().getScale();
        // Iterate through each triangle in the mesh
        for (int i = 0; i < mesh.getVertices().length - 2; i++) {
            Vertex vertex0 = mesh.getVertices()[i];
            Vertex vertex1 = mesh.getVertices()[i+1];
            Vertex vertex2 = mesh.getVertices()[i + 2];
            if(areParallelOrSameDirection(Stream.of(vertex0,vertex1,vertex2).map(Vertex::getNormal).toList(), direction)) continue;
            Vector3f v0 = Vector3f.add(position, vertex0.getPosition().scale(scale));
            Vector3f v1 = Vector3f.add(position, vertex1.getPosition().scale(scale));
            Vector3f v2 = Vector3f.add(position, vertex2.getPosition().scale(scale));
// Calculate the edges of the triangle
            Vector3f edge1 = Vector3f.subtract(v1, v0);
            Vector3f edge2 = Vector3f.subtract(v2, v0);

            // Calculate the normal of the triangle
            Vector3f normal = Vector3f.cross(edge1, edge2).normalized();

            // Compute the determinant
            float determinant = Vector3f.dot(direction, normal);

            // Check if the ray and the triangle are parallel
            if (Math.abs(determinant) < 0.00001f)
                continue; // Ray and triangle are parallel, check next triangle

            // Calculate the distance from the ray origin to the plane of the triangle
            float t = Vector3f.dot(Vector3f.subtract(v0, origin), normal) / determinant;

            // Check if the intersection point is behind the ray origin
            if (t < 0.0f)
                continue; // Intersection point is behind the ray origin, check next triangle

            // Calculate the intersection point
            Vector3f intersectionPoint = Vector3f.add(origin, direction.scale(t));

            // Calculate barycentric coordinates
            float u = Vector3f.dot(Vector3f.cross(Vector3f.subtract(intersectionPoint, v0), edge1), normal);
            float v = Vector3f.dot(Vector3f.cross(Vector3f.subtract(intersectionPoint, v0), edge2), normal);

            // Check if the intersection point is inside the triangle
            if (Math.abs(u) >= 0.0f && Math.abs(v) >= 0.0f && u + v <= 1.0f) {
                // Calculate the distance from the ray origin to the intersection point
                float distance = Vector3f.subtract(intersectionPoint, origin).length();

                // Check if the intersection point is within the radius
                if (distance <= radius) {
                    // If the distance is closer than the closest hit so far, update the closest hit
                    if (distance < closestDistance) {
                        closestHit = new HitResult(true, intersectionPoint, origin, normal, renderableObject);
                        closestDistance = distance;
                    }
                }
            }
        }
        // Return the closest hit result
        return closestHit; // Placeholder, replace with actual implementation
    }
    public static boolean areParallelOrSameDirection(List<Vector3f> normals, Vector3f rayDirection) {
        float threshold = 0.998f;
        for (Vector3f normal : normals) {
            // Normalize the vectors to ensure accurate dot product
            // Calculate the dot product between the normal and the ray direction
            float dotProduct = Vector3f.dot(normal.normalized(), rayDirection.normalized());

            // Check if the dot product is close to 1 or -1 (indicating parallelism)
            if(Math.abs(dotProduct) > threshold || dotProduct >= threshold)
                return true;
        }
        return false;
    }

}
