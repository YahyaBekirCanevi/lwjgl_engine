package com.canevi.engine.objects;

import com.canevi.engine.maths.Transform;

public class GameObject extends TObject {

	public GameObject(TObject parent, Transform transform) {
		super(transform);
	}

}
