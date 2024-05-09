package com.canevi.engine.objects;

import com.canevi.engine.maths.Transform;
import lombok.Getter;

@Getter
public abstract class TObject {
    protected Transform transform;
    // protected TObject parent;
    public TObject(Transform transform) {
        this.transform = transform;
    }
}
