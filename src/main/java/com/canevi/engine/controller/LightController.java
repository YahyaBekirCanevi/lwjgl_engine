package com.canevi.engine.controller;

import static org.lwjgl.opengl.GL11.*;

public class LightController {
    public void initialize() {

        // create some floats for our ambient, diffuse, specular and position
        float[] ambient = { 0.2f, 0.2f, 0.2f, 1.0f }; // dark grey
        float[] diffuse = { 1.0f, 1.0f, 1.0f, 1.0f }; // white
        float[] specular = { 1.0f, 1.0f, 1.0f, 1.0f }; // white
        float[] position = { 2, 2, 1.0f, 0.0f };

        // set the ambient, diffuse, specular and position for LIGHT0
        glLightfv(GL_LIGHT0, GL_AMBIENT, ambient);
        glLightfv(GL_LIGHT0, GL_DIFFUSE, diffuse);
        glLightfv(GL_LIGHT0, GL_SPECULAR, specular);
        glLightfv(GL_LIGHT0, GL_POSITION, position);

        glEnable(GL_LIGHTING); // enables lighting
        glEnable(GL_LIGHT0); // enables the 0th light
        glEnable(GL_COLOR_MATERIAL); // colors materials when lighting is enabled

        // enable specular lighting via materials
        glMaterialfv(GL_FRONT, GL_SPECULAR, specular);
        glMateriali(GL_FRONT, GL_SHININESS, 15);

        // enable smooth shading
        glShadeModel(GL_SMOOTH);

        // set polygon mode
        glPolygonMode(GL_FRONT, GL_FILL);
        glPolygonMode(GL_BACK, GL_POINT);

        // enable depth testing to be 'less than'
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LESS);

        // set the backbuffer clearing color to a lightish blue
        glClearColor(0.6f, 0.65f, 0.85f, 0);
    }
}
