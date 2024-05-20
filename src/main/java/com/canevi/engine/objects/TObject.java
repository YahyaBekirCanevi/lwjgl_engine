package com.canevi.engine.objects;

import com.canevi.engine.Component;
import com.canevi.engine.maths.Transform;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TObject {
    protected Transform transform;
    protected Map<String, Component> componentMap;
    // protected TObject parent;
    public TObject(Transform transform) {
        this.transform = transform;
        componentMap = new HashMap<>();
    }
    public String getName() {
        return this.getClass().getName();
    }
    public abstract void onStart();

    public abstract void onUpdate();

    public abstract void onDestroy();
}
