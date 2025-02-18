//
// Created by daltw on 2/17/2025.
//

#include "kollie/perspective_camera.h"

#include "kollie/log.h"

#include "glm/ext.hpp"

#include <sstream>
#include <iomanip>

std::string mat4ToString(const glm::mat4& matrix, int precision = 6) {
    std::stringstream ss;
    ss << std::fixed << std::setprecision(precision) << "\n";

    // Get raw data pointer
    const float* data = glm::value_ptr(matrix);

    // GLM matrices are column-major, but we'll print in row-major for readability
    for (int row = 0; row < 4; row++) {
        ss << "| ";
        for (int col = 0; col < 4; col++) {
            // Convert from column-major to row-major index
            float value = data[col * 4 + row];
            ss << std::setw(precision + 4) << value;
            if (col < 3) ss << "  ";
        }
        ss << " |\n";
    }

    return ss.str();
}

#include "glm/ext.hpp"

PerspectiveCamera::PerspectiveCamera(float fov, float aspectRatio, float nearPlane,
                                     float farPlane):
    _fov(fov), _aspectRatio(aspectRatio), _nearPlane(nearPlane), _farPlane(farPlane)
 {

    _position = glm::vec3(0.0f, 0.0f, 5.0f);
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
//    std::string s = mat4ToString(_viewMatrix);
//
//    LOGD("view matrix = %s", s.c_str());

    return _viewMatrix;
}

glm::mat4 PerspectiveCamera::getProjectionMatrix() const {

//    std::string s = mat4ToString(_projectionMatrix);
//
//    LOGD("projection matrix = %s", s.c_str());

    return _projectionMatrix;
}

void PerspectiveCamera::_updateViewMatrix() {

    glm::vec3 target = _position + _front;

    _viewMatrix = glm::lookAt(_position, target, _up);
}

void PerspectiveCamera::_updateProjectionMatrix() {

    _projectionMatrix = glm::perspective(
            glm::radians(_fov), _aspectRatio, _nearPlane, _farPlane);
}