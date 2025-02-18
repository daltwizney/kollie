//
// Created by daltw on 2/6/2025.
//

#include <GLES3/gl31.h>

#include "kollie/log.h"

#include "kollie/full_screen_quad.h"


FullScreenQuad::FullScreenQuad()
    : _vertices {
        // positions        // texture coords
        -1.0f,  1.0f, 0.0f, 0.0f, 1.0f,   // top left
        -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,   // bottom left
        1.0f,  1.0f, 0.0f, 1.0f, 1.0f,   // top right
        1.0f, -1.0f, 0.0f, 1.0f, 0.0f    // bottom right
    }
{
    // create & bind VAO and VBO
    glGenVertexArrays(1, &_VAO);
    glGenBuffers(1, &_VBO);

    glBindVertexArray(_VAO);
    glBindBuffer(GL_ARRAY_BUFFER, _VBO);

    glBufferData(GL_ARRAY_BUFFER, sizeof(_vertices), _vertices, GL_STATIC_DRAW);

    // Position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    // Texture coordinate attribute
    glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    glBindVertexArray(0);
}

void FullScreenQuad::draw() {

    glClear(GL_COLOR_BUFFER_BIT);

    glBindVertexArray(_VAO);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
}

void FullScreenQuad::destroy(bool freeGLResources) {

    if (freeGLResources)
    {
        if (_VAO != GL_INVALID_VALUE)
        {
            glDeleteVertexArrays(1, &_VAO);
            _VAO = GL_INVALID_VALUE;
        }

        if (_VBO != GL_INVALID_VALUE)
        {
            glDeleteBuffers(1, &_VBO);
            _VBO = GL_INVALID_VALUE;
        }
    }
}

void FullScreenQuad::resize(int screenWidth, int screenHeight) {

    // update bottom left
    _vertices[6] = screenHeight;

    // update top right
    _vertices[10] = screenWidth;

    // update bottom right
    _vertices[15] = screenWidth;
    _vertices[16] = screenHeight;

    // update VBO
    glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(_vertices), _vertices);
}