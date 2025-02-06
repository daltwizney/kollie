//
// Created by daltw on 2/2/2025.
//

#ifndef KOLLIE_RENDERER_H
#define KOLLIE_RENDERER_H

#include <string>

class Renderer {

public:

    void init();
    void resize(int width, int height);
    void drawFullScreenQuad(long shaderProgramID);

    void initGrid();
    void drawGrid();

    void destroy();

private:

    int _width;
    int _height;

    int _resolutionLocation;

    unsigned int _shaderProgram;

    unsigned int _screenQuadVAO;
    unsigned int _screenQuadVBO;

    unsigned int _gridVAO;
    unsigned int _gridVBO;
};

#endif //KOLLIE_RENDERER_H
