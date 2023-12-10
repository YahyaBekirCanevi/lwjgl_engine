package com.canevi.engine.enums;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TextureWrapType {
    REPEAT(GL11.GL_REPEAT), MIRRORED_REPEAT(GL14.GL_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE), CLAMP_TO_BORDER(GL13.GL_CLAMP_TO_BORDER);

    private int value;
}
