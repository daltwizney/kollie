//
// Created by daltw on 2/2/2025.
//
#include <string>

#include <GLES3/gl31.h>

#include "collie/log.h"

#include "collie/renderer.h"

void Renderer::init() {

    // set clear color
    glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

    // Vertex data for fullscreen quad
    float vertices[] = {
            // positions        // texture coords
            -1.0f,  1.0f, 0.0f, 0.0f, 1.0f,   // top left
            -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,   // bottom left
            1.0f,  1.0f, 0.0f, 1.0f, 1.0f,   // top right
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f    // bottom right
    };

    // Create VAO and VBO
    glGenVertexArrays(1, &_screenQuadVAO);
    glGenBuffers(1, &_screenQuadVBO);

    glBindVertexArray(_screenQuadVAO);
    glBindBuffer(GL_ARRAY_BUFFER, _screenQuadVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    // Position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    // Texture coordinate attribute
    glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    LOGI("OpenGL initialized!");
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

    if (_screenQuadVAO != GL_INVALID_VALUE)
    {
        glDeleteVertexArrays(1, &_screenQuadVAO);
        _screenQuadVAO = GL_INVALID_VALUE;
    }

    if (_screenQuadVBO != GL_INVALID_VALUE)
    {
        glDeleteBuffers(1, &_screenQuadVBO);
        _screenQuadVBO = GL_INVALID_VALUE;
    }

    if (_shaderProgram == 0)
    {
        glDeleteProgram(_shaderProgram);
        _shaderProgram = 0;
    }
}