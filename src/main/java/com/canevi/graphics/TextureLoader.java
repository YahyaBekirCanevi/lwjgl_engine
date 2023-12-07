package com.canevi.graphics;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import com.canevi.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextureLoader {
    private static final List<Integer> mFilters = Arrays.asList(GL11.GL_NEAREST, GL11.GL_LINEAR);
    private static final List<Integer> mWraps = Arrays.asList(GL11.GL_REPEAT, GL14.GL_MIRRORED_REPEAT,
            GL12.GL_CLAMP_TO_EDGE,
            GL13.GL_CLAMP_TO_BORDER);
    private static final int target = GL11.GL_TEXTURE_2D;

    public static Texture getTexture(String path, int filtering, boolean shouldFlip) {
        try {
            return getTextureEx(path, filtering, shouldFlip);
        } catch (IOException e) {
            log.info("Failed to load texture: " + path);
        }
        return null;
    }

    public static Texture getTextureEx(String path, int filtering, boolean shouldFlip) throws IOException {
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

            STBImage.stbi_set_flip_vertically_on_load(shouldFlip);
            ByteBuffer image = STBImage.stbi_load_from_memory(imageBuffer, w, h, comp, 4);
            if (image == null) {
                throw new RuntimeException("Failed to load image: " + path);
            }

            textureID = createTextureID();
            GL11.glBindTexture(target, textureID);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, mWraps.get(0));
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, mWraps.get(0));
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, filtering);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, filtering);

            width = w.get();
            height = h.get();
            channel = comp.get();

            GL11.glTexImage2D(target, 0, GL11.GL_RGBA, width, height, 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);
            GL30.glGenerateMipmap(target);
            GL11.glBindTexture(target, 0);

            return new Texture(textureID, width, height, channel, image);
        }
    }

    private static int createTextureID() {
        return GL11.glGenTextures();
    }

}
