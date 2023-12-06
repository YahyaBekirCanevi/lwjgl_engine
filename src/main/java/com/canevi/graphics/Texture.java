package com.canevi.graphics;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import lombok.Getter;

@Getter
public class Texture {
    private int textureID;
    private int width, height, channel;
    private ByteBuffer imageData;

    public Texture(int textureID, int width, int height, int channel, ByteBuffer imageData) {
        this.textureID = textureID;
        this.width = width;
        this.height = height;
        this.channel = channel;
        this.imageData = imageData;
    }
    

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    }

    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
}
