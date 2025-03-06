//
// Created by daltw on 2/17/2025.
//

#ifndef KOLLIE_CUBE_H
#define KOLLIE_CUBE_H

#include "glm/glm.hpp"

#include "kollie/shader_program.h"

#include "kollie/perspective_camera.h"

class Cube {

public:

    Cube();

    // TODO: only passing shader for testing - eventually going to update model from kotlin layer!
    void draw(ShaderProgram* program, PerspectiveCamera *camera);

    void destroy(bool freeGLResources);

private:

    unsigned int _VAO;
    unsigned int _positionVBO;
    unsigned int _colorVBO;
    unsigned int _normalVBO;
    unsigned int _EBO;

    float _positions[108];
    float _colors[108];
    float _normals[108];

    unsigned int _indices[36];

    // TODO: here for testing for now - but move this to kotlin app code!
    glm::mat4 _model;
    glm::mat3 _normalMatrix;
};


#endif //KOLLIE_CUBE_H
