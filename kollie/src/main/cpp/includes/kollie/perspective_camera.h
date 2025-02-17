//
// Created by daltw on 2/17/2025.
//

#ifndef KOLLIE_PERSPECTIVE_CAMERA_H
#define KOLLIE_PERSPECTIVE_CAMERA_H

#include "glm/glm.hpp"

class PerspectiveCamera {

public:

    PerspectiveCamera(float fov, // field-of-view in degrees
                      float aspectRatio,
                      float nearPlane = 0.1f,
                      float farPlane = 100.0f);

    void setPosition(const glm::vec3& pos);
    void lookAt(const glm::vec3& target);

    void setAspectRatio(const float aspectRatio);

    glm::mat4 getViewMatrix() const;
    glm::mat4 getProjectionMatrix() const;

private:

    // projection matrix params
    float _aspectRatio;
    float _fov;
    float _nearPlane;
    float _farPlane;

    // glm::lookat params
    glm::vec3 _position;
    glm::vec3 _front;
    glm::vec3 _up;

    glm::mat4 _viewMatrix;
    glm::mat4 _projectionMatrix;

    void _updateViewMatrix();
    void _updateProjectionMatrix();
};


#endif //KOLLIE_PERSPECTIVE_CAMERA_H
