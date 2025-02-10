//
// Created by daltw on 2/8/2025.
//

#include <GLES3/gl31.h>

#include "kollie/grid_2d.h"

Grid2D::Grid2D() {

    _vertices = new float[12] {
            // positions
            100.0f,  100.0f, 0.0f, // top left
            100.0f, 200.0f, 0.0f,    // bottom left
            200.0f,  100.0f, 0.0f,     // top right
            200.0f, 200.0f, 0.0f,   // bottom right
    };

//    _vertices = new float[12] {
//            // positions
//            0.5f,  0.5f, 0.0f, // top left
//            0.5f, 1.0f, 0.0f,    // bottom left
//            0.5f,  1.0f, 0.0f,     // top right
//            1.0f, 1.0f, 0.0f,   // bottom right
//    };

    // create & bind VAO and VBO
    glGenVertexArrays(1, &_VAO);
    glGenBuffers(1, &_VBO);

    glBindVertexArray(_VAO);
    glBindBuffer(GL_ARRAY_BUFFER, _VBO);

    glBufferData(GL_ARRAY_BUFFER, sizeof(float) * 12, _vertices, GL_STATIC_DRAW);

    // Position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 3 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);
}

void Grid2D::draw() {

//    glClear(GL_COLOR_BUFFER_BIT);

    glBindVertexArray(_VAO);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
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
