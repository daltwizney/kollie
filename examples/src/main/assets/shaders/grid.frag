#version 300 es
precision mediump float;
out vec4 FragColor;
in vec2 TexCoord;

uniform vec2 resolution;

// uniform vec4 color;

void main()
{
    // FragColor = color;
    FragColor = vec4(0.0, 1.0, 0.0, 1.0);
}