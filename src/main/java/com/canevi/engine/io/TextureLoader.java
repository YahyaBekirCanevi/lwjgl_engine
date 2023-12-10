package com.canevi.engine.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import com.canevi.engine.enums.TextureFilterType;
import com.canevi.engine.enums.TextureWrapType;
import com.canevi.engine.graphics.Texture;
import com.canevi.engine.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TextureLoader {

    private static final int target = GL11.GL_TEXTURE_2D;

    public static Texture getTexture(String path, boolean shouldFlip,
            TextureWrapType wrapType, TextureFilterType filterType) {
        try {
            return getTextureEx(path, shouldFlip, wrapType, filterType);
        } catch (IOException e) {
            log.info("Failed to load texture: " + path);
        }
        return null;
    }

    public static Texture getTextureEx(String path, boolean shouldFlip,
            TextureWrapType wrapType, TextureFilterType filterType) throws IOException {
        int textureID, width, height, channel;

        ByteBuffer imageBuffer;

        byte[] imageBytes = FileUtils.loadImageFile(path);
        imageBuffer = BufferUtils.createByteBuffer(imageBytes.length);
        imageBuffer.put(imageBytes).flip();

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
            int wrap = wrapType.getValue();
            int filter = filterType.getValue();
            GL11.glBindTexture(target, textureID);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_S, wrap);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_WRAP_T, wrap);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MIN_FILTER, filter);
            GL11.glTexParameteri(target, GL11.GL_TEXTURE_MAG_FILTER, filter);

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
