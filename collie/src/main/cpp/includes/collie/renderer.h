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

    int frameCounter();

private:

    int _frameCounter;
};


#endif //KOLLIE_RENDERER_H
