#version 300 es
precision mediump float;

out vec4 FragColor;

in vec3 lineColor;

void main()
{
    // FragColor = color;
    FragColor = vec4(lineColor, 1.0);
}