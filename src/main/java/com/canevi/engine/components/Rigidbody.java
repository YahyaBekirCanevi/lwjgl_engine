package com.canevi.engine.components;

import com.canevi.engine.Component;
import com.canevi.engine.maths.Vector3f;
import com.canevi.engine.objects.RenderableObject;
import com.canevi.engine.physics.HitResult;
import com.canevi.engine.physics.Physics;

public class Rigidbody extends Component {
    private static final Vector3f GRAVITY = new Vector3f(0, -9.81f, 0);
    private static final float TIME_STEP = 0.016f; // 60 FPS
    private static final float SKIN_WIDTH = 0.1f;

    private Vector3f velocity;
    private Vector3f acceleration;
    private RenderableObject attachedObject;

    public Rigidbody(RenderableObject attachedObject) {
        this.attachedObject = attachedObject;
        this.velocity = new Vector3f(0, 0, 0);
        this.acceleration = new Vector3f(0, 0, 0);
    }
    public void applyGravity() {
        this.acceleration = GRAVITY;
    }

    @Override
    public void onUpdate() {
        // Update velocity based on acceleration
        velocity = velocity.add(acceleration.scale(TIME_STEP));
        Vector3f scaledVelocity = velocity.scale(TIME_STEP);
        // Update position based on velocity
        Vector3f newPosition = attachedObject.getTransform().getPosition().add(scaledVelocity);

        HitResult hitResult = Physics.raycast(attachedObject.getTransform().getPosition(),
                scaledVelocity, scaledVelocity.length() + SKIN_WIDTH);
        // Check for collision with the ground
        if (!hitResult.isIntersects()) {
            attachedObject.getTransform().setPosition(newPosition);
            applyGravity();
        } else {
            // Stop the character on the ground
            velocity.setY(0);
            acceleration.setY(0);
        }
    }
}
