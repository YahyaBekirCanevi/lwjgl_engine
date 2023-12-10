package com.canevi.engine.graphics;

import org.lwjgl.opengl.GL13;

import com.canevi.engine.enums.TextureFilterType;
import com.canevi.engine.enums.TextureWrapType;
import com.canevi.engine.io.TextureLoader;
import com.canevi.engine.maths.Vector3f;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Material {
	private Texture diffusemap;
	private Texture normalmap;
	private Texture emmisionmap;
	private Vector3f color = new Vector3f(1, 1, 1);

	public Material(Texture diffusemap, Texture normalmap, Texture emmisionmap) {
		this.diffusemap = diffusemap;
		this.normalmap = normalmap;
		this.emmisionmap = emmisionmap;
	}

	public void destroy() {
		if (diffusemap != null) {
			GL13.glDeleteTextures(diffusemap.getTextureID());
		}
		if (normalmap != null) {
			GL13.glDeleteTextures(normalmap.getTextureID());
		}
		if (emmisionmap != null) {
			GL13.glDeleteTextures(emmisionmap.getTextureID());
		}
	}

	public static Material defaultMaterial() {
		Texture diffuseTexture = TextureLoader.getTexture("textures/image.png", false,
				TextureWrapType.REPEAT, TextureFilterType.LINEAR);
		return new Material(diffuseTexture, null, null);
	}
}
