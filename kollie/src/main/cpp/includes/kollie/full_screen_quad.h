//
// Created by daltw on 2/6/2025.
//

#ifndef KOLLIE_FULL_SCREEN_QUAD_H
#define KOLLIE_FULL_SCREEN_QUAD_H

class FullScreenQuad {

public:

    FullScreenQuad();

    void resize(int screenWidth, int screenHeight);
    void draw();
    void destroy(bool freeGLResources = true);

private:

    unsigned int _VAO;
    unsigned int _VBO;

    float _vertices[20];
};


#endif //KOLLIE_FULL_SCREEN_QUAD_H
