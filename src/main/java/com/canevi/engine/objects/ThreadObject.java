package com.canevi.engine.objects;

import com.canevi.engine.maths.Transform;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ThreadObject {
    protected final String name;
    protected boolean isEnabled = true;
    protected boolean shouldDestroy = false;
    protected GameObject gameObject;

    public ThreadObject(String name) {
        this.name = name;
    }

    protected Transform getTransform() {
        return gameObject.getTransform();
    }

    public abstract void onStart();

    public abstract void onUpdate();

    public abstract void onDestroy();

    public void loop() {
        if (!shouldDestroy && isEnabled) {
            onUpdate();
        }
    }

    public void interrupt() {
        shouldDestroy = true;
    }

    public void destroy() {
        isEnabled = false;
        onDestroy();
    }
}
