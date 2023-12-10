package com.canevi.engine.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.canevi.engine.maths.Matrix4f;
import com.canevi.engine.maths.Transform;
import com.canevi.engine.objects.Camera;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Renderer {
	private Shader shader;

	public void renderMesh(Mesh mesh, Transform transform, Camera camera) {
		if (mesh == null)
			return;
		GL30.glBindVertexArray(mesh.getVao());
		GL30.glEnableVertexAttribArray(0);
		GL30.glEnableVertexAttribArray(1);
		GL30.glEnableVertexAttribArray(2);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mesh.getIbo());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL13.glBindTexture(GL11.GL_TEXTURE_2D, mesh.getMaterial().getDiffusemap().getTextureID());
		shader.bind();
		shader.setUniform("model", Matrix4f.transform(transform));
		shader.setUniform("view", Matrix4f.view(camera));
		shader.setUniform("projection", camera.getProjectionMatrix());
		GL11.glDrawElements(GL11.GL_TRIANGLES, mesh.getIndices().length, GL11.GL_UNSIGNED_INT, 0);
		shader.unbind();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL30.glDisableVertexAttribArray(0);
		GL30.glDisableVertexAttribArray(1);
		GL30.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
}