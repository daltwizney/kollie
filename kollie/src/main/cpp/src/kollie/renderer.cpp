//
// Created by daltw on 2/2/2025.
//
#include <string>

#include <GLES3/gl31.h>

#include "kollie/log.h"

#include "kollie/renderer.h"

static unsigned int arCoreTextureId = -1;

void Renderer::init() {

    // set clear color
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    GLint samples;
    glGetIntegerv(GL_SAMPLES, &samples);
    LOGI("GLES31 - Number of samples: %d", samples);

    // just creating a dummy texture to get a texture ID for ar core to be happy for now
    glGenTextures(1, &arCoreTextureId);

    glBindTexture(GL_TEXTURE_2D, arCoreTextureId);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 1, 1, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

    glBindTexture(GL_TEXTURE_2D, 0);
}

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

unsigned int Renderer::getArCoreTextureId() {
    return arCoreTextureId;
}

