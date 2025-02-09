//
// Created by daltw on 2/8/2025.
//

#include "kollie/camera_2d.h"

#include "glm/ext.hpp"

void Camera2D::lookAt(const glm::vec3 &target) {

    _front = glm::normalize(target - _position);

    _updateViewMatrix();
}

void Camera2D::setPosition(const glm::vec3 &pos) {

    _position = pos;

    _updateViewMatrix();
}

glm::mat4 Camera2D::getViewMatrix() const {
    return _viewMatrix;
}

glm::mat4 Camera2D::getProjectionMatrix() const {
    return _projectionMatrix;
}

Camera2D::Camera2D(float left, float right, float bottom, float top, float nearPlane,
                   float farPlane): _left(left), _right(right), _bottom(bottom), _top(top),
                                    _nearPlane(nearPlane), _farPlane(farPlane) {
    _updateViewMatrix();
    _updateProjectionMatrix();
}

void Camera2D::_updateViewMatrix() {

    _viewMatrix = glm::lookAt(_position, _position + _front, _up);
}

void Camera2D::_updateProjectionMatrix() {

    _projectionMatrix = glm::ortho(
            _left, _right, _bottom, _top, _nearPlane, _farPlane);
}


