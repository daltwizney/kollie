//
// Created by daltw on 2/17/2025.
//

#include <GLES3/gl31.h>

#include <sstream>
#include <iomanip>

#include "glm/ext.hpp"

#include "kollie/cube.h"

#include "kollie/log.h"
//
//std::string mat4ToString(const glm::mat4& matrix, int precision = 6) {
//    std::stringstream ss;
//    ss << std::fixed << std::setprecision(precision) << "\n";
//
//    // Get raw data pointer
//    const float* data = glm::value_ptr(matrix);
//
//    // GLM matrices are column-major, but we'll print in row-major for readability
//    for (int row = 0; row < 4; row++) {
//        ss << "| ";
//        for (int col = 0; col < 4; col++) {
//            // Convert from column-major to row-major index
//            float value = data[col * 4 + row];
//            ss << std::setw(precision + 4) << value;
//            if (col < 3) ss << "  ";
//        }
//        ss << " |\n";
//    }
//
//    return ss.str();
//}

Cube::Cube()
    : _positions {

        -0.5f, -0.5f,  0.5f, // bottom left
        0.5f, -0.5f,  0.5f, // bottom right
        0.5f,  0.5f,  0.5f, // top right
        -0.5f,  0.5f,  0.5f, // top left

        // Back face
        -0.5f, -0.5f, -0.5f, // bottom left
        0.5f, -0.5f, -0.5f, // bottom right
        0.5f,  0.5f, -0.5f, // top right
        -0.5f,  0.5f, -0.5f, // top left
    },

    _colors {

        // Front face (purple)
        (128 / 255.0f), (0/255.0f), (128/255.0f),
        (128 / 255.0f), (0/255.0f), (128/255.0f),
        (128 / 255.0f), (0/255.0f), (128/255.0f),
        (128 / 255.0f), (0/255.0f), (128/255.0f),

        // Back face (green)
        (144 / 255.0f), (213/255.0f), (1.0f),
        (144 / 255.0f), (213/255.0f), (1.0f),
        (144 / 255.0f), (213/255.0f), (1.0f),
        (144 / 255.0f), (213/255.0f), (1.0f),
    },

    // indices for drawing cube using triangles
    _indices {

            // Front face
            0, 1, 2,
            2, 3, 0,
            // Back face
            4, 6, 5,
            6, 4, 7,
            // Top face
            3, 2, 6,
            6, 7, 3,
            // Bottom face
            0, 5, 1,
            5, 0, 4,
            // Right face
            1, 5, 6,
            6, 2, 1,
            // Left face
            0, 7, 4,
            7, 0, 3
    },

    _model(glm::mat4(1.0))
{
    // Create VAO
    glGenVertexArrays(1, &_VAO);

    glGenBuffers(1, &_positionVBO);
    glGenBuffers(1, &_colorVBO);
    glGenBuffers(1, &_EBO);

    glBindVertexArray(_VAO);

    // Create and bind EBO
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _EBO);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(_indices), _indices, GL_STATIC_DRAW);

    // Create and bind position VBO
    glBindBuffer(GL_ARRAY_BUFFER, _positionVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(_positions), _positions, GL_STATIC_DRAW);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(0);

    // Create and bind color VBO
    glBindBuffer(GL_ARRAY_BUFFER, _colorVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(_colors), _colors, GL_STATIC_DRAW);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(1);

    // TODO: we shouldn't need to unbind GL_ARRAY_BUFFER, but this won't work without it,
    // so there may be a bug in our code upstream/downstream of this, or in the driver...
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    // Unbind VAO
    glBindVertexArray(0);
}

void Cube::draw(ShaderProgram *program) {

    glEnable(GL_DEPTH_TEST);

    glEnable(GL_CULL_FACE);
//    glDisable(GL_CULL_FACE);

    glBindVertexArray(_VAO);

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    GLint modelLoc = glGetUniformLocation(program->id(), "model");
    GLint viewLoc = glGetUniformLocation(program->id(), "view");
    GLint projLoc = glGetUniformLocation(program->id(), "projection");
    if (modelLoc == -1 || viewLoc == -1 || projLoc == -1) {
        LOGE("Failed to get uniform locations!");
    }

    // Update model matrix to rotate cube
    _model = glm::rotate(_model, glm::radians(1.0f), glm::vec3(0.0f, 1.0f, 0.0f));

    program->setUniformMatrix4fv("model", 1, false, glm::value_ptr(_model));

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

        if (_positionVBO != GL_INVALID_VALUE)
        {
            glDeleteBuffers(1, &_positionVBO);
            _positionVBO = GL_INVALID_VALUE;
        }

        if (_colorVBO != GL_INVALID_VALUE)
        {
            glDeleteBuffers(1, &_colorVBO);
            _colorVBO = GL_INVALID_VALUE;
        }

        if (_EBO != GL_INVALID_VALUE)
        {
            glDeleteBuffers(1, &_EBO);
            _EBO = GL_INVALID_VALUE;
        }
    }
}
