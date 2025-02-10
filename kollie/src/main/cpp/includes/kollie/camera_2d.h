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

    Camera2D(const int screenWidth, const int screenHeight);

    void setPosition(const glm::vec3& pos);
    void lookAt(const glm::vec3& target);

    void setScreenSize(const int screenWidth, const int screenHeight);

    glm::mat4 getViewMatrix() const;
    glm::mat4 getProjectionMatrix() const;

    // TODO: frustum controls

    // TODO: zoom controls

private:

    // screen dims
    int _screenWidth;
    int _screenHeight;

    // glm::lookat params
    glm::vec3 _position;
    glm::vec3 _front;
    glm::vec3 _up;

    glm::mat4 _viewMatrix;
    glm::mat4 _projectionMatrix;

    void _updateViewMatrix();
    void _updateProjectionMatrix();
};


#endif //KOLLIE_CAMERA_2D_H
