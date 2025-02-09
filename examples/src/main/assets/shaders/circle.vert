#version 300 es
precision mediump float;

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoord;

// TODO: add model matrix, but can skip for now since all is in world-space
uniform mat4 view;
uniform mat4 projection;

out vec2 TexCoord;

void main()
{
    gl_Position = projection * view * vec4(aPosition, 1.0);

    // gl_Position = vec4(aPosition, 1.0);
    TexCoord = aTexCoord;
}