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

    long compileShader(const std::string& vertexShaderSrc, const std::string& fragmentShaderSrc);

    void destroy();

private:

    int _width;
    int _height;

    int _resolutionLocation;

    unsigned int _shaderProgram;
    unsigned int _screenQuadVAO;
    unsigned int _screenQuadVBO;
};

#endif //KOLLIE_RENDERER_H
