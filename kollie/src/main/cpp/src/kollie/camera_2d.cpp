//
// Created by daltw on 2/8/2025.
//

#include "kollie/camera_2d.h"

#include "glm/ext.hpp"

Camera2D::Camera2D(const int screenWidth, const int screenHeight)
        : _screenWidth(screenWidth), _screenHeight(screenHeight)
{
    _position = glm::vec3(0.0f, 0.0f, 0.0f); // camera positioned at origin
    _front = glm::vec3(0.0f, 0.0f, -1.0f); // looking down -z axis
    _up = glm::vec3(0.0f, 1.0f, 0.0f); // +y-up

    _updateViewMatrix();
    _updateProjectionMatrix();
}

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

void Camera2D::_updateViewMatrix() {

    glm::vec3 target = _position + _front;

    _viewMatrix = glm::lookAt(_position, target, _up);
}

void Camera2D::_updateProjectionMatrix() {

    _projectionMatrix = glm::ortho(
            0.0f, _screenWidth * 1.0f, 0.0f, _screenHeight * 1.0f, -1.0f, 1.0f);
}

void Camera2D::setScreenSize(const int screenWidth, const int screenHeight) {

    _screenWidth = screenWidth;
    _screenHeight = screenHeight;

    _updateProjectionMatrix();
}


