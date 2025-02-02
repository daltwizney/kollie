//
// Created by daltw on 2/2/2025.
//
#include <GLES3/gl31.h>

#include "collie/log.h"

#include "collie/renderer.h"

void Renderer::init() {

    glClearColor(0.0f, 0.0f, 1.0f, 1.0f);

    _frameCounter = 0;

    LOGI("OpenGL initialized!");
}

void Renderer::resize(int width, int height) {
    glViewport(0, 0, width, height);
    LOGI("Surface resized to %dx%d", width, height);
}

void Renderer::draw() {

    glClear(GL_COLOR_BUFFER_BIT);

    _frameCounter++;
}

int Renderer::frameCounter() {

    return _frameCounter;
}