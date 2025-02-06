//
// Created by daltw on 2/6/2025.
//

#include <GLES3/gl31.h>

#include "collie/log.h"

#include "collie/full_screen_quad.h"

void FullScreenQuad::draw() {
    glClear(GL_COLOR_BUFFER_BIT);

    glBindVertexArray(_VAO);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void FullScreenQuad::init() {
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
    glGenVertexArrays(1, &_VAO);
    glGenBuffers(1, &_VBO);

    glBindVertexArray(_VAO);
    glBindBuffer(GL_ARRAY_BUFFER, _VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    // Position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    // Texture coordinate attribute
    glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    LOGI("OpenGL initialized!");
}
