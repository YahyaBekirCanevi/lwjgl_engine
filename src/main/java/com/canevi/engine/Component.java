package com.canevi.engine;

public abstract class Component {
    public String getName() {
        return this.getClass().getName();
    }
    public void onStart() { }

    public void onUpdate() { }

    public void onDestroy() { }
}
