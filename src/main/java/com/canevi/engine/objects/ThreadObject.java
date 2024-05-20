package com.canevi.engine.objects;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class ThreadObject {
    @Getter
    private static List<ThreadObject> threadList = new ArrayList<>();

    protected final String name;
    protected boolean isEnabled = true;
    protected boolean shouldDestroy = false;

    public ThreadObject(String name) {
        this.name = name;
        threadList.add(this);
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
