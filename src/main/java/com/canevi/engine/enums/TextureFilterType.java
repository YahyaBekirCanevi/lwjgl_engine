package com.canevi.engine.enums;

import org.lwjgl.opengl.GL11;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TextureFilterType {
    NEAREST(GL11.GL_NEAREST), LINEAR(GL11.GL_LINEAR);

    private int value;
}