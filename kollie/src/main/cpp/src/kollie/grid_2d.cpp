//
// Created by daltw on 2/8/2025.
//

#include <GLES3/gl31.h>

#include "kollie/grid_2d.h"

static int cellSize = 100;

void Grid2D::setPerInstanceAttributes() {

    float positions[] = {
    cellSize * 1.0f, cellSize * 1.0f, 0.0f,
        400.0f, 300.0f, 0.0f
    };

    glBindVertexArray(_VAO);
    glBindBuffer(GL_ARRAY_BUFFER, _instanceVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(positions), positions, GL_STATIC_DRAW);

    glEnableVertexAttribArray(1);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, sizeof(float) * 3, (void*)0);

    glVertexAttribDivisor(1, 1);
}


Grid2D::Grid2D() {

    _vertices = new float[12] {
            // positions
            -cellSize / 2.0f,  -cellSize / 2.0f, 0.0f, // top left
            -cellSize / 2.0f, cellSize / 2.0f, 0.0f,    // bottom left
            cellSize / 2.0f,  -cellSize / 2.0f, 0.0f,     // top right
            cellSize / 2.0f, cellSize / 2.0f, 0.0f,   // bottom right
    };

    // create & bind VAO and VBO
    glGenVertexArrays(1, &_VAO);
    glGenBuffers(1, &_VBO);
    glGenBuffers(1, &_instanceVBO);

    glBindVertexArray(_VAO);
    glBindBuffer(GL_ARRAY_BUFFER, _VBO);

    glBufferData(GL_ARRAY_BUFFER, sizeof(float) * 12, _vertices, GL_STATIC_DRAW);

    // Position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    this->setPerInstanceAttributes();
}

void Grid2D::draw() {

    glBindVertexArray(_VAO);
    glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 4, 2);
}

void Grid2D::destroy(bool freeGLResources) {

    if (_vertices)
    {
        delete[] _vertices;
        _vertices = nullptr;
    }

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
