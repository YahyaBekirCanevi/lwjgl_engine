package com.canevi.graphics;

import java.util.EnumMap;
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
	public static enum TextureType {
		DIFFUSE, NORMAL, EMMISION
	}

	private final Texture diffusemap;
	private final Texture normalmap;
	private final Texture emmisionmap;
	private Vector3f color = new Vector3f(1, 1, 1);
	private TextureLoader.FilterType filterType = TextureLoader.FilterType.LINEAR;
	private TextureLoader.WrapType wrapType = TextureLoader.WrapType.REPEAT;

	public Material(boolean shouldFlip, EnumMap<TextureType, String> properties) {
		String diffuse = properties.getOrDefault(TextureType.DIFFUSE, null);
		diffusemap = diffuse == null ? null : TextureLoader.getTexture(diffuse, shouldFlip, wrapType, filterType);
		String normal = properties.getOrDefault(TextureType.NORMAL, null);
		normalmap = normal == null ? null : TextureLoader.getTexture(normal, shouldFlip, wrapType, filterType);
		String emmision = properties.getOrDefault(TextureType.EMMISION, null);
		emmisionmap = emmision == null ? null : TextureLoader.getTexture(emmision, shouldFlip, wrapType, filterType);
	}

	public void destroy() {
		GL13.glDeleteTextures(diffusemap.getTextureID());
		if (normalmap != null) {
			GL13.glDeleteTextures(normalmap.getTextureID());
		}
	}

	public static Material defaultMaterial() {
		EnumMap<TextureType, String> enumMap = new EnumMap<>(TextureType.class);
		enumMap.put(TextureType.DIFFUSE, "textures/image.png");
		return new Material(false, new EnumMap<>(TextureType.class));
	}
}
