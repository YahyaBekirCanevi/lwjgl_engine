package com.canevi.engine.graphics;

import com.canevi.engine.maths.Vector2f;
import com.canevi.engine.maths.Vector3f;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vertex {
	private Vector3f position;
	private Vector3f normal;
	private Vector2f textureCoord;
}
