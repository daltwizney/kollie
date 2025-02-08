//
// Created by daltw on 2/8/2025.
//

#ifndef KOLLIE_CAMERA_2D_H
#define KOLLIE_CAMERA_2D_H

#include "glm/glm.hpp"

/**
 *  2D orthographic camera responsible for managing view and projection matrices
 */
class Camera2D {
public:

    // TODO: constructor

    Camera2D(
            float left = -10.0f, float right = 10.0f,
            float bottom = -10.0f, float top = 10.0f,
            float nearPlane = 0.1f, float farPlane = 100.0f
    );

    void setPosition(const glm::vec3& pos);
    void lookAt(const glm::vec3& target);

    glm::mat4 getViewMatrix() const;
    glm::mat4 getProjectionMatrix() const;

    // TODO: frustum controls

    // TODO: zoom controls

private:

    // glm::ortho frustum params
    float _left;
    float _right;
    float _bottom;
    float _top;
    float _nearPlane;
    float _farPlane;

    // glm::lookat params
    glm::vec3 _position;
    glm::vec3 _up;
    glm::vec3 _front;

    glm::mat4 _viewMatrix;
    glm::mat4 _projectionMatrix;

    void _updateViewMatrix();
    void _updateProjectionMatrix();
};


#endif //KOLLIE_CAMERA_2D_H
