//
// Created by daltw on 2/8/2025.
//

#ifndef KOLLIE_GRID_2D_H
#define KOLLIE_GRID_2D_H

#include <vector>

#include "glm/glm.hpp"

class Grid2D {
public:

    void initBuffers();
    void draw();
    void destroy(bool freeGLResources = true);

private:

    // flattened 2D array of cell colors, row-major
    std::vector<glm::vec4> _cellColors;

    // VAO has 3 VBOs - colors are dynamic, but vertex & cell positions don't change between frames
    unsigned int _VAO;

    unsigned int _vertexBuffer;
    unsigned int _positionBuffer;
    unsigned int _colorBuffer;
};


#endif //KOLLIE_GRID_2D_H
