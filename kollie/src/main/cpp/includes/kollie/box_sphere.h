//
// Created by daltw on 3/6/2025.
//

#ifndef KOLLIE_BOX_SPHERE_H
#define KOLLIE_BOX_SPHERE_H

#include "kollie/shader_program.h"
#include "kollie/perspective_camera.h"

class BoxSphere {

public:

    void draw(ShaderProgram* program, PerspectiveCamera *camera);

    void destroy(bool freeGLResources);

private:

    unsigned int _VAO;
    unsigned int _positionVBO;
    unsigned int _colorVBO;
    unsigned int _normalVBO;
    unsigned int _EBO;

    float* _positions;
    float* _colors;
    float* _normals;

    unsigned int* _indices;

    size_t nPositions;
    size_t nColors;
    size_t nNormals;
    size_t nIndices;

    // TODO: here for testing for now - but move this to kotlin app code!
    glm::mat4 _model;
    glm::mat3 _normalMatrix;
};

#endif //KOLLIE_BOX_SPHERE_H
