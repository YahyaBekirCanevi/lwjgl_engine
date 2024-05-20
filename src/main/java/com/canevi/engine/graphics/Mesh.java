package com.canevi.engine.graphics;

import com.canevi.engine.maths.Vector3f;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

@Getter
@RequiredArgsConstructor
public class Mesh {
	private final Vertex[] vertices;
	private final int[] indices;
	private final Material material;
	private int vao, pbo, ibo, cbo, tbo;
	private Vector3f minExtents;
	private Vector3f maxExtents;

	public void create() {
		minExtents = new Vector3f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);
		maxExtents = new Vector3f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

		vao = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vao);

		FloatBuffer positionBuffer = MemoryUtil.memAllocFloat(vertices.length * 3);
		float[] positionData = new float[vertices.length * 3];
		for (int i = 0; i < vertices.length; i++) {
			Vector3f position = vertices[i].getPosition();
			positionData[i * 3] = position.getX();
			positionData[i * 3 + 1] = position.getY();
			positionData[i * 3 + 2] = position.getZ();

			// Update extents
			minExtents = Vector3f.min(minExtents, position);
			maxExtents = Vector3f.max(maxExtents, position);
		}
		positionBuffer.put(positionData);
		positionBuffer.flip();

		pbo = storeData(positionBuffer, 0, 3);

		FloatBuffer textureBuffer = MemoryUtil.memAllocFloat(vertices.length * 2);
		float[] textureData = new float[vertices.length * 2];
		for (int i = 0; i < vertices.length; i++) {
			textureData[i * 2] = vertices[i].getTextureCoord().getX();
			textureData[i * 2 + 1] = vertices[i].getTextureCoord().getY();
		}
		textureBuffer.put(textureData).flip();

		tbo = storeData(textureBuffer, 2, 2);

		IntBuffer indicesBuffer = MemoryUtil.memAllocInt(indices.length);
		indicesBuffer.put(indices).flip();

		ibo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	private int storeData(FloatBuffer buffer, int index, int size) {
		int bufferID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, bufferID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return bufferID;
	}

	public void destroy() {
		GL15.glDeleteBuffers(pbo);
		GL15.glDeleteBuffers(cbo);
		GL15.glDeleteBuffers(ibo);
		GL15.glDeleteBuffers(tbo);
		GL30.glDeleteVertexArrays(vao);
		material.destroy();
	}
}