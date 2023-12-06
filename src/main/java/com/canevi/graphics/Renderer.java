package com.canevi.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.canevi.io.Window;
import com.canevi.maths.Matrix4f;
import com.canevi.maths.Transform;
import com.canevi.objects.Camera;
import com.canevi.objects.GameObject;


public class Renderer {
	private Shader shader;
	private Window window;
	
	public Renderer(Window window, Shader shader) {
		this.shader = shader;
		this.window = window;
	}
	
	public void renderMesh(Mesh mesh, Transform transform, Camera camera) {
		if (mesh == null) return; 
		GL30.glBindVertexArray(mesh.getVao());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getMaterial().getTextureID());
		shader.bind();
		shader.setUniform("model", Matrix4f.transform(transform));
		shader.setUniform("view", Matrix4f.view(camera));
		shader.setUniform("projection", window.getProjectionMatrix());
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
		shader.unbind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}