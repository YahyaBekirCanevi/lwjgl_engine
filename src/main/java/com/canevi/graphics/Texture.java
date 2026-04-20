package com.canevi.graphics;

import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
public class Texture {
    private int textureID;
    private int width, height, channel;
    private ByteBuffer imageData;

    public int getTextureID() { return textureID; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getChannel() { return channel; }
    public ByteBuffer getImageData() { return imageData; }

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
