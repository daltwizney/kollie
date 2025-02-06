//
// Created by daltw on 2/6/2025.
//

#ifndef KOLLIE_FULL_SCREEN_QUAD_H
#define KOLLIE_FULL_SCREEN_QUAD_H


class FullScreenQuad {

public:

    void initBuffers();
    void draw();
    void destroy();

private:

    unsigned int _VAO;
    unsigned int _VBO;
};


#endif //KOLLIE_FULL_SCREEN_QUAD_H
