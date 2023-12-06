package com.canevi.graphics;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Material {
	private Texture texture;
	@Getter
	private float width, height;
	@Getter
	private int textureID;
	
	public Material(String texturePath) {
		try {
			texture = TextureLoader.getTexture(texturePath, GL11.GL_NEAREST);
			width = texture.getWidth();
			height = texture.getHeight();
			textureID = texture.getTextureID();
		} catch (IOException e) {
			log.info("Can't find texture at " + texturePath);
		}
	}
	
	public Material(Texture Texture) {
		this.texture = Texture;
		this.width = texture.getWidth();
		this.height = texture.getHeight();
		this.textureID = texture.getTextureID();
	}
	
	public void destroy() {
		GL13.glDeleteTextures(textureID);
	}

	public static Material defaultMaterial() {
		return new Material("textures/image.png");
	}
}
