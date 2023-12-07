package com.canevi.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.canevi.maths.Vector3f;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Material {
	private final Texture diffusemap;
	private final Texture normalmap;
	private Vector3f color = new Vector3f(1, 1, 1);

	public Material(String diffuseDir, boolean shouldFlip) {
		diffusemap = TextureLoader.getTexture(diffuseDir, GL11.GL_NEAREST, shouldFlip);
		normalmap = null;
	}

	public Material(String diffuseDir, String normalDir, boolean shouldFlip) {
		diffusemap = TextureLoader.getTexture(diffuseDir, GL11.GL_NEAREST, shouldFlip);
		normalmap = TextureLoader.getTexture(normalDir, GL11.GL_NEAREST, shouldFlip);
	}

	public void destroy() {
		GL13.glDeleteTextures(diffusemap.getTextureID());
		if (normalmap != null) {
			GL13.glDeleteTextures(normalmap.getTextureID());
		}
	}

	public static Material defaultMaterial() {
		return new Material("textures/image.png", null, false);
	}
}
