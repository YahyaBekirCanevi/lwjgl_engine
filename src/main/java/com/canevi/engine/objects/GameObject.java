package com.canevi.engine.objects;

import com.canevi.engine.Component;
import com.canevi.engine.maths.Transform;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class GameObject extends TObject {

	public GameObject(Transform transform) {
		super(transform);
	}

	@Override
	public void onStart() {
		componentMap.values().forEach(Component::onStart);
	}

	@Override
	public void onUpdate() {
		componentMap.values().forEach(Component::onUpdate);
	}

	@Override
	public void onDestroy() {
		componentMap.values().forEach(Component::onDestroy);
	}
	public void addComponent(Component component) {
		componentMap.put(getName(), component);
	}

}
