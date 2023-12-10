package com.canevi.engine.graphics;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Texture {
    private int textureID;
    private int width, height, channel;
    private ByteBuffer imageData;

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
