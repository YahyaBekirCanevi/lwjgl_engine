package com.canevi.engine.objects;

import com.canevi.engine.maths.Transform;

public abstract class TObject {
    protected Transform transform;
    // protected TObject parent;

    public TObject(Transform transform) {
        this.transform = transform;
    }

    public Transform getTransform() {
        return transform;
    }
}
