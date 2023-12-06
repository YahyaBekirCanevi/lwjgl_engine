package com.canevi.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AITexel;
import org.lwjgl.assimp.AITexture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import com.canevi.utils.FileUtils;

public class TextureLoader {
    public static Texture getTexture(String path, int filtering) throws IOException {
        int textureID, width, height, channel;

        ByteBuffer imageBuffer;

        byte[] imageBytes = FileUtils.loadImageFile(path);
        imageBuffer = BufferUtils.createByteBuffer(imageBytes.length);
        imageBuffer.put(imageBytes);
        imageBuffer.flip();

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + path);
            }

            textureID = createTextureID();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filtering);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filtering);

            width = w.get();
            height = h.get();
            channel = comp.get();

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            return new Texture(textureID, width, height, channel, image);
        }
    }
    public static Texture convertAITexture(AITexture aiTexture) {
        int width = aiTexture.mWidth();
        int height = aiTexture.mHeight();
        int size = width * height * AITexel.SIZEOF;
        ByteBuffer imageData = MemoryUtil.memByteBuffer(aiTexture.pcData(size).address(), size);
        int textureID = createTextureID();

        return new Texture(textureID, width, height, 4, imageData);
    }
    private static int createTextureID() {
        return GL11.glGenTextures();
    }
}
