//
// Created by daltw on 2/17/2025.
//

#ifndef KOLLIE_CUBE_H
#define KOLLIE_CUBE_H

#include "glm/glm.hpp"

#include "kollie/shader_program.h"

#include "kollie/perspective_camera.h"

struct CubeFace {

    glm::vec3 vertexPositions[4];
    glm::vec3 faceNormal;
    glm::vec3 vertexColors[4];
};

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

    glm::vec3 _positions[36];
    glm::vec3 _normals[36];
    glm::vec3 _colors[36];

    unsigned int _indices[36];

    // TODO: here for testing for now - but move this to kotlin app code!
    glm::mat4 _model;
    glm::mat3 _normalMatrix;

    CubeFace *_faces;

    void _generateFaces();
    void _generateTriangleMesh();
};


#endif //KOLLIE_CUBE_H
