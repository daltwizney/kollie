//
// Created by daltw on 2/2/2025.
//
#include <string>

#include <GLES3/gl31.h>

#include "kollie/log.h"

#include "kollie/renderer.h"

void Renderer::resize(int width, int height) {

    glViewport(0, 0, width, height);

    LOGI("Surface resized to %dx%d", width, height);
}

void Renderer::clearColorBuffer() {

    glClear(GL_COLOR_BUFFER_BIT);
}

bool Renderer::isContextValid() {
    return glGetError() == GL_NO_ERROR;
}
