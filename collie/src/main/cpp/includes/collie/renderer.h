//
// Created by daltw on 2/2/2025.
//

#ifndef KOLLIE_RENDERER_H
#define KOLLIE_RENDERER_H

class Renderer {

public:

    void init();
    void resize(int width, int height);
    void draw();

    void destroy();

    int frameCounter();

private:

    int _width;
    int _height;

    int _frameCounter;

    int _resolutionLocation;

    unsigned int _shaderProgram;
    unsigned int _screenQuadVAO;
    unsigned int _screenQuadVBO;
};

#endif //KOLLIE_RENDERER_H
