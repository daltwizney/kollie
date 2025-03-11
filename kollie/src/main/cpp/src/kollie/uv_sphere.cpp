//
// Created by daltw on 3/6/2025.
//

#include <GLES3/gl31.h>

#include "glm/ext.hpp"

#include "kollie/uv_sphere.h"

#include "kollie/log.h"

UvSphere::UvSphere(int segments, int rings, float radius)
    : _segments(segments), _rings(rings), _radius(radius),
        _model(glm::mat4(1.0))
{
    // initialize sphere data
    _allocateSphereMemory();

    _generateVertexData();

    _generateTriangleMesh();

    LOGI("_nFaces = %d, _nIndices = %d, _nPositions = %d", _nFaces, _nIndices, _nPositions);

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
                 _nIndices * sizeof(unsigned int),
                 _indices, GL_STATIC_DRAW);

    // bind position VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _positionVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 _nPositions * sizeof(glm::vec3),
                 _positions, GL_STATIC_DRAW);

    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(0);

    // bind color VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _colorVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 _nColors * sizeof(glm::vec3),
                 _colors, GL_STATIC_DRAW);

    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, nullptr);
    glEnableVertexAttribArray(1);

    // bind normal VBO and buffer data
    glBindBuffer(GL_ARRAY_BUFFER, _normalVBO);
    glBufferData(GL_ARRAY_BUFFER,
                 _nNormals * sizeof(glm::vec3),
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

    glEnable(GL_DEPTH_TEST);

//    glEnable(GL_CULL_FACE);
    glDisable(GL_CULL_FACE);

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

    // Update model matrix to rotate sphere
    _model = glm::rotate(_model, glm::radians(1.0f), glm::vec3(0.0f, 1.0f, 0.0f));

    glm::mat4 modelViewMatrix = camera->getViewMatrix() * _model;

    _normalMatrix = glm::mat3(glm::transpose(glm::inverse(modelViewMatrix)));

    program->setUniformMatrix4fv("model", 1, false, glm::value_ptr(_model));
    program->setUniformMatrix3fv("normalMatrix", 1, false, glm::value_ptr(_normalMatrix));

    glDrawElements(GL_TRIANGLES, _nIndices, GL_UNSIGNED_INT, 0);
}

void UvSphere::destroy(bool freeGLResources) {

    // free application resources
    if (_faces != nullptr)
    {
        delete[] _faces;
        _faces = nullptr;
    }

    if (_positions != nullptr)
    {
        delete[] _positions;
        _positions = nullptr;
    }

    if (_normals != nullptr)
    {
        delete[] _normals;
        _normals = nullptr;
    }

    if (_colors != nullptr)
    {
        delete[] _colors;
        _colors = nullptr;
    }

    if (_indices != nullptr)
    {
        delete[] _indices;
        _indices = nullptr;
    }

    // free opengl resources
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

void UvSphere::_allocateSphereMemory() {

    // initialize array sizes
    _nFaces = _segments * _rings;

    // this is just for testing without top and bottom rings for now
    _nFaces -= 2 * _segments; // TODO: remove before flight

    // TODO: this will be different for flat vs smooth shading
    _nPositions = _nFaces * 6;
    _nColors = _nFaces * 6;
    _nNormals = _nFaces * 6;
    _nIndices = _nFaces * 6;

    // allocate _positions, _colors, _normals arrays
    _positions = new glm::vec3[_nPositions];
    _colors = new glm::vec3[_nColors];
    _normals = new glm::vec3[_nNormals];

    _indices = new unsigned int[_nIndices];

    _faces = new UvSphereFace[_nFaces];
}

glm::vec3 _convertToCartesian(float radius, float theta, float phi) {

    glm::vec3 result;

    // NOTE the 'z' and 'y' flip here - b.c. 'up' is 'y' in opengl!
    result.x = radius * glm::sin(theta) * glm::cos(phi);
    result.z = radius * glm::sin(theta) * sin(phi);
    result.y = radius * cos(theta);

    return result;
}

void UvSphere::_generateVertexData() {

    /**
     *  NOTE:
     *
     *  - polar angle is 'theta'
     *  - azimuthal angle is 'phi'
     */

    // compute vertex data for UV sphere
    float dTheta = M_PI / _rings;
    float dPhi = 2 * M_PI / _segments;

    int nThetaAngles = _rings - 1; // doesn't include top and bottom verts
    int nPhiAngles = _segments;

    int count = 0;

    // TODO: update the indexing to consider the top and bottom tris separately
    for (int i = 0; i < nThetaAngles - 1; ++i)
    {
        for (int j = 0; j < nPhiAngles; ++j)
        {
            // be careful calculating theta1 in terms of 'i', b.c. _faces
            // gets populated based on 'i' too!
            float theta1 = (i+1) * dTheta;
            float phi1 = j * dPhi;

            float theta2 = theta1 + dTheta;
            float phi2 = phi1 + dPhi;

            UvSphereFace face;

            // compute vertex positions
            face.vertexPositions[0] = _convertToCartesian(
                    _radius, theta1, phi1);

            face.vertexPositions[1] = _convertToCartesian(
                    _radius, theta1, phi2);

            face.vertexPositions[2] = _convertToCartesian(
                    _radius, theta2, phi2);

            face.vertexPositions[3] = _convertToCartesian(
                    _radius, theta2, phi1);

            float theta1d = theta1 * 180.0f / M_PI;
            float theta2d = theta2 * 180.0f / M_PI;
            float phi1d = phi1 * 180.0f / M_PI;
            float phi2d = phi2 * 180.0f / M_PI;

            LOGI("FACE p1 = (%f, %f), p2 = (%f, %f), p3 = (%f, %f), p4 = (%f, %f)",
                 theta1d, phi1d,
                 theta1d, phi2d,
                 theta2d, phi2d,
                 theta2d, phi1d);

//            LOGI("vertex = (%f, %f, %f), radius = %f",
//                 face.vertexPositions[0].x,
//                 face.vertexPositions[0].y,
//                 face.vertexPositions[0].z,
//                 glm::length(face.vertexPositions[0]));

            // compute vertex normals
            face.faceNormal = glm::vec3(0, 0, 0);

            for (int k = 0; k < 4; ++k)
            {
                face.faceNormal += face.vertexPositions[k];
            }

            face.faceNormal = glm::normalize(face.faceNormal);

            // compute vertex colors
            for (int k = 0; k < 4; ++k)
            {
                face.vertexColors[k] = glm::vec3(0.0f, 0.0f, 1.0f);
            }

            // save computed face
            _faces[i * _segments + j] = face;

            count++;
        }
    }

    LOGI("computed face count = %d", count);
}

void UvSphere::_generateTriangleMesh() {

    // generate vertex attribute data
    for (int i = 0; i < _nFaces; ++i)
    {
        UvSphereFace face = _faces[i];

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

//    for (int i = 0; i < _nColors; i++)
//    {
//        LOGI("VERTEX COLOR: (%f, %f, %f)",
//             _colors[i].x, _colors[i].y, _colors[i].z);
//    }

    // generate indices
    for (int i = 0; i < _nIndices; ++i)
    {
        _indices[i] = i;
    }
}