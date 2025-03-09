//
// Created by daltw on 2/17/2025.
//

#include <GLES3/gl31.h>

#include <sstream>
#include <iomanip>

#include "glm/ext.hpp"

#include "kollie/cube.h"

#include "kollie/log.h"

Cube::Cube()
    : _model(glm::mat4(1.0))
{
    // initialize vertex data
    _generateFaces();
    _generateTriangleMesh();

    // initialize index data
    for (int i = 0; i < 36; ++i)
    {
        _indices[i] = i;
    }

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
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(_indices), _indices, GL_STATIC_DRAW);

    // bind position VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _positionVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 36 * sizeof(glm::vec3),
                 _positions, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(0);

    // Create and bind color VBO
    glBindBuffer(GL_ARRAY_BUFFER, _colorVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 36 * sizeof(glm::vec3), _colors, GL_STATIC_DRAW);

    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(1);

    // bind normal VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _normalVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 36 * sizeof(glm::vec3), _normals, GL_STATIC_DRAW);

    glVertexAttribPointer(2, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(2);

    // TODO: we shouldn't need to unbind GL_ARRAY_BUFFER, but this won't work without it,
    // so there may be a bug in our code upstream/downstream of this, or in the driver...
    glBindBuffer(GL_ARRAY_BUFFER, 0);

    // Unbind VAO
    glBindVertexArray(0);
}

void Cube::_generateFaces() {

    // cube vertices
    glm::vec3 frontBottomLeft(-0.5f, -0.5f, 0.5f);
    glm::vec3 frontBottomRight(0.5f, -0.5f, 0.5f);
    glm::vec3 frontTopRight(0.5f, 0.5f, 0.5f);
    glm::vec3 frontTopLeft(-0.5f, 0.5f, 0.5f);

    glm::vec3 backBottomLeft(-0.5f, -0.5f, -0.5f);
    glm::vec3 backTopLeft(-0.5f, 0.5f, -0.5f);
    glm::vec3 backTopRight(0.5f, 0.5f, -0.5f);
    glm::vec3 backBottomRight(0.5f, -0.5f, -0.5f);

    // vertex colors
    glm::vec3 purple((128 / 255.0f), (0/255.0f), (128/255.0f));
    glm::vec3 lightBlue((144 / 255.0f), (213/255.0f), (1.0f));

    // define cube faces
    _faces = new CubeFace[6] {
            CubeFace { // front face
                {
                    frontBottomLeft,
                    frontBottomRight,
                    frontTopRight,
                    frontTopLeft
                },
                glm::vec3(0.0, 0.0, 1.0),
                {
                    purple,
                    purple,
                    purple,
                    purple
                },
            },
            CubeFace { // back face
                {
                    backBottomLeft,
                    backTopLeft,
                    backTopRight,
                    backBottomRight
                },
                glm::vec3(0.0, 0.0, -1.0),
                {
                    lightBlue,
                    lightBlue,
                    lightBlue,
                    lightBlue
                }
            },
            CubeFace { // top face
                {
                    frontTopLeft,
                    frontTopRight,
                    backTopRight,
                    backTopLeft
                },
                glm::vec3(0.0, 1.0, 0.0),
                {
                    purple,
                    purple,
                    lightBlue,
                    lightBlue
                }
            },
            CubeFace { // bottom face
                {
                    frontBottomLeft,
                    backBottomLeft,
                    backBottomRight,
                    frontBottomRight
                },
                glm::vec3(0.0, -1.0, 0.0),
                {
                    purple,
                    lightBlue,
                    lightBlue,
                    purple
                }
            },
            CubeFace { // right face
                {
                    frontBottomRight,
                    backBottomRight,
                    backTopRight,
                    frontTopRight
                },
                glm::vec3(1.0, 0.0, 0.0),
                {
                    purple,
                    lightBlue,
                    lightBlue,
                    purple
                }
            },
            CubeFace { // front face
                {
                    frontBottomLeft,
                    frontTopLeft,
                    backTopLeft,
                    backBottomLeft
                },
                glm::vec3(-1.0, 0.0, 0.0),
                {
                    purple,
                    purple,
                    lightBlue,
                    lightBlue
                }
            },
    };
}

void Cube::_generateTriangleMesh() {

    // calculate triangle _positions, _colors and _normals from faces
    for (int i = 0; i < 6; ++i)
    {
        CubeFace face = _faces[i];

        // first triangle
        _positions[6 * i + 0] = face.vertexPositions[0];
        _positions[6 * i + 1] = face.vertexPositions[1];
        _positions[6 * i + 2] = face.vertexPositions[2];

        _normals[6 * i + 0] = face.faceNormal;
        _normals[6 * i + 1] = face.faceNormal;
        _normals[6 * i + 2] = face.faceNormal;

        _colors[6 * i + 0] = face.vertexColors[0];
        _colors[6 * i + 1] = face.vertexColors[1];
        _colors[6 * i + 2] = face.vertexColors[2];

        // second triangle
        _positions[6 * i + 3] = face.vertexPositions[2];
        _positions[6 * i + 4] = face.vertexPositions[3];
        _positions[6 * i + 5] = face.vertexPositions[0];

        _normals[6 * i + 3] = face.faceNormal;
        _normals[6 * i + 4] = face.faceNormal;
        _normals[6 * i + 5] = face.faceNormal;

        _colors[6 * i + 3] = face.vertexColors[2];
        _colors[6 * i + 4] = face.vertexColors[3];
        _colors[6 * i + 5] = face.vertexColors[0];
    }
}

void Cube::draw(ShaderProgram *program, PerspectiveCamera *camera) {

    glEnable(GL_DEPTH_TEST);

    glEnable(GL_CULL_FACE);
//    glDisable(GL_CULL_FACE);

    glBindVertexArray(_VAO);

    glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    GLint modelLoc = glGetUniformLocation(program->id(), "model");
    GLint viewLoc = glGetUniformLocation(program->id(), "view");
    GLint projLoc = glGetUniformLocation(program->id(), "projection");

//    GLint normalMatrixLoc = glGetUniformLocation(program->id(), "normalMatrix");
    GLint normalMatrixLoc = program->getUniformLocation("normalMatrix");

    if (modelLoc == -1 || viewLoc == -1 || projLoc == -1 || normalMatrixLoc == -1) {
         LOGE("Failed to get uniform locations! normalLoc = %d", normalMatrixLoc);
    }

    // Update model matrix to rotate cube
    _model = glm::rotate(_model, glm::radians(1.0f), glm::vec3(0.0f, 1.0f, 0.0f));

    glm::mat4 modelViewMatrix = camera->getViewMatrix() * _model;

    _normalMatrix = glm::mat3(glm::transpose(glm::inverse(modelViewMatrix)));

    program->setUniformMatrix4fv("model", 1, false, glm::value_ptr(_model));
    program->setUniformMatrix3fv("normalMatrix", 1, false, glm::value_ptr(_normalMatrix));

    glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
}

void Cube::destroy(bool freeGLResources) {

    if (_faces != nullptr)
    {
        delete[] _faces;
        _faces = nullptr;
    }

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
