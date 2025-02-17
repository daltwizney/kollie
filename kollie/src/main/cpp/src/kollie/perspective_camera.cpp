//
// Created by daltw on 2/17/2025.
//

#include "kollie/perspective_camera.h"

#include "glm/ext.hpp"

PerspectiveCamera::PerspectiveCamera(float fov, float aspectRatio, float nearPlane,
                                     float farPlane) {

    _position = glm::vec3(0.0f, 0.0f, 10.0f);
    _front = glm::vec3(0.0f, 0.0f, -1.0f);
    _up = glm::vec3(0.0f, 1.0f, 0.0f);

    _updateViewMatrix();
    _updateProjectionMatrix();
}

void PerspectiveCamera::setPosition(const glm::vec3 &pos) {

    _position = pos;

    _updateViewMatrix();
}

void PerspectiveCamera::lookAt(const glm::vec3 &target) {

    _front = glm::normalize(target - _position);

    _updateViewMatrix();
}

void PerspectiveCamera::setAspectRatio(const float aspectRatio) {

    _aspectRatio = aspectRatio;

    _updateProjectionMatrix();
}

glm::mat4 PerspectiveCamera::getViewMatrix() const {
    return _viewMatrix;
}

glm::mat4 PerspectiveCamera::getProjectionMatrix() const {
    return _projectionMatrix;
}

void PerspectiveCamera::_updateViewMatrix() {

    glm::vec3 target = _position + _front;

    _viewMatrix = glm::lookAt(_position, target, _up);
}

void PerspectiveCamera::_updateProjectionMatrix() {

    _projectionMatrix = glm::perspective(
            _fov, _aspectRatio, _nearPlane, _farPlane);
}