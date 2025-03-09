//
// Created by daltw on 3/6/2025.
//

#include <GLES3/gl31.h>

#include "kollie/uv_sphere.h"

#include "glm/ext.hpp"

UvSphere::UvSphere() {

    // generate sphere vertex data
    _generateSphere();

    // create VAO, VBOs and EBO
    glGenVertexArrays(1, &_VAO);

    glGenBuffers(1, &_positionVBO);
    glGenBuffers(1, &_colorVBO);
    glGenBuffers(1, &_normalVBO);
    glGenBuffers(1, &_EBO);

    // bind VAO
    glBindVertexArray(_VAO);

    // bind EBO and buffer data
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER,
                 nIndices * sizeof(unsigned int),
                 _indices, GL_STATIC_DRAW);

    // bind position VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _positionVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 nPositions * sizeof(float),
                 _positions, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(0);

    // bind color VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _normalVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 nColors * sizeof(float),
                 _normals, GL_STATIC_DRAW);

    glVertexAttribPointer(2, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(2);

    // bind normal VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _normalVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 nNormals * sizeof(float),
                 _normals, GL_STATIC_DRAW);

    glVertexAttribPointer(2, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(2);

    // TODO: we shouldn't need to unbind GL_ARRAY_BUFFER, but this won't work without it,
    // so there may be a bug in our code upstream/downstream of this, or in the driver...
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    // Unbind VAO
    glBindVertexArray(0);
}

void UvSphere::draw(ShaderProgram *program, PerspectiveCamera *camera) {

}

void UvSphere::destroy(bool freeGLResources) {

}

void UvSphere::_generateSphere() {

    // initialize array sizes

    // allocate _positions, _colors, _normals arrays
}
