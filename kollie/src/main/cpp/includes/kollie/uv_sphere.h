//
// Created by daltw on 3/6/2025.
//

#ifndef KOLLIE_UV_SPHERE_H
#define KOLLIE_UV_SPHERE_H

#include "kollie/shader_program.h"
#include "kollie/perspective_camera.h"

#include "glm/glm.hpp"

struct UvSphereFace {

    glm::vec3 vertexPositions[4];
    glm::vec3 faceNormal;
    glm::vec3 vertexColors[4];
};

class UvSphere {

public:

    UvSphere(int segments = 7, int rings = 5, float radius = 1.0f);

    void draw(ShaderProgram* program, PerspectiveCamera *camera);

    void destroy(bool freeGLResources);

private:

    int _segments;
    int _rings;

    float _radius;

    unsigned int _VAO;
    unsigned int _positionVBO;
    unsigned int _colorVBO;
    unsigned int _normalVBO;
    unsigned int _EBO;

    glm::vec3 *_positions;
    glm::vec3 *_colors;
    glm::vec3 *_normals;

    unsigned int* _indices;

    size_t _nPositions;
    size_t _nColors;
    size_t _nNormals;
    size_t _nIndices;

    size_t _nFaces;

    // TODO: here for testing for now - but move this to kotlin app code!
    glm::mat4 _model;
    glm::mat3 _normalMatrix;

    UvSphereFace *_faces;

    void _allocateSphereMemory();

    void _generateVertexData();
    void _generateTriangleMesh();
};

#endif //KOLLIE_UV_SPHERE_H
