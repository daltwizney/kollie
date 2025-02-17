//
// Created by daltw on 2/17/2025.
//

#include <GLES3/gl31.h>

#include "glm/ext.hpp"

#include "kollie/cube.h"

#include "kollie/log.h"

Cube::Cube()
    : _vertices {

        // position and RGB color per-vertex

        // Front face (red)
        -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f,
        0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 0.0f,

        // Back face (green)
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,
        0.5f, -0.5f, -0.5f,  0.0f, 1.0f, 0.0f,
        0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0.0f
    },

    // indices for drawing cube using triangles
    _indices {

        // Front face
        0, 1, 2,
        2, 3, 0,
        // Back face
        4, 5, 6,
        6, 7, 4,
        // Left face
        4, 0, 3,
        3, 7, 4,
        // Right face
        1, 5, 6,
        6, 2, 1,
        // Top face
        3, 2, 6,
        6, 7, 3,
        // Bottom face
        0, 1, 5,
        5, 4, 0
    },

    _model(glm::mat4(1.0))
{
    // create VAO and buffer vertex + element index data
    glGenVertexArrays(1, &_VAO);
    glGenBuffers(1, &_VBO);
    glGenBuffers(1, &_EBO);

    glBindVertexArray(_VAO);

    glBindBuffer(GL_ARRAY_BUFFER, _VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(_vertices), _vertices, GL_STATIC_DRAW);

    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(_indices), _indices, GL_STATIC_DRAW);

    // position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    // color attribute
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 6 * sizeof(float), (void*)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);
}

void Cube::draw(ShaderProgram *program) {

    // TODO:
    glEnable(GL_DEPTH_TEST);
//    glDisable(GL_CULL_FACE);

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

//    GLint modelLoc = glGetUniformLocation(program->id(), "model");
//    GLint viewLoc = glGetUniformLocation(program->id(), "view");
//    GLint projLoc = glGetUniformLocation(program->id(), "projection");
//    if (modelLoc == -1 || viewLoc == -1 || projLoc == -1) {
//        LOGE("Failed to get uniform locations!");
//    }

    // Update model matrix to rotate cube
//    _model = glm::scale(glm::mat4(1.0), glm::vec3(2.0f, 2.0f, 2.0f));
    _model = glm::rotate(_model, glm::radians(1.0f), glm::vec3(0.0f, 1.0f, 0.0f));

//    glm::mat4 view = glm::translate(glm::mat4(1.0f), glm::vec3(0.0f, 0.0f, -5.0f));
//    glm::mat4 projection = glm::perspective(glm::radians(45.0f), 2000.0f/900.0f, 0.1f, 100.0f);

    program->setUniformMatrix4fv("model", 1, false, glm::value_ptr(_model));
//    program->setUniformMatrix4fv("view", 1, false, glm::value_ptr(view));
//    program->setUniformMatrix4fv("projection", 1, false, glm::value_ptr(projection));

    glBindVertexArray(_VAO);
    glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
}

void Cube::destroy(bool freeGLResources) {

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

        if (_EBO != GL_INVALID_VALUE)
        {
            glDeleteBuffers(1, &_EBO);
            _EBO = GL_INVALID_VALUE;
        }
    }
}
