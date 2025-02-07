//
// Created by daltw on 2/2/2025.
//

#ifndef KOLLIE_RENDERER_H
#define KOLLIE_RENDERER_H

#include <string>

class Renderer {

public:

    static void resize(int width, int height);

    static void clearColorBuffer();

    static bool isContextValid();
};

#endif //KOLLIE_RENDERER_H
