#version 300 es
precision mediump float;

out vec4 FragColor;

in vec3 quadColor;

void main()
{
    // FragColor = color;
    FragColor = vec4(quadColor, 1.0);
}