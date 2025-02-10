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

    // TODO: move the glClearColor() call to a static 'init()' method for renderer
    // set clear color
    glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

    glClear(GL_COLOR_BUFFER_BIT);
}

bool Renderer::isContextValid() {
    return glGetError() == GL_NO_ERROR;
}
