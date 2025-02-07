//
// Created by daltw on 2/2/2025.
//
#include <string>

#include <GLES3/gl31.h>

#include "collie/log.h"

#include "collie/renderer.h"

void Renderer::init() {

}

void Renderer::drawFullScreenQuad(long shaderProgramID) {

    // Clear the screen
    glClear(GL_COLOR_BUFFER_BIT);

    // Draw quad
    glUseProgram(shaderProgramID);

    glUniform2f(_resolutionLocation, (float) _width, (float) _height);

    glBindVertexArray(_screenQuadVAO);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void Renderer::initGrid() {

    float vertices[] = {
        -0.5f, 0.0f, 0.0f,
        0.5f, 0.0f, 0.0f
    };

    // create and bind VAO
    glGenVertexArrays(1, &_gridVAO);
    glBindVertexArray(_gridVAO);

    // create and bind VBO
    glGenBuffers(1, &_gridVBO);
    glBindBuffer(GL_ARRAY_BUFFER, _gridVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    // set vertex attributes
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*) 0);
    glEnableVertexAttribArray(0);
}

void Renderer::drawGrid() {


}

void Renderer::resize(int width, int height) {

    glViewport(0, 0, width, height);

    _width = width;
    _height = height;

    LOGI("Surface resized to %dx%d", width, height);
}

void Renderer::destroy() {

}