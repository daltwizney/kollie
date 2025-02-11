#version 300 es
precision mediump float;
out vec4 FragColor;
in vec2 TexCoord;

in vec3 myVertexColor;

uniform vec2 resolution;

// uniform vec4 color;

void main()
{
    // FragColor = color;
    FragColor = vec4(myVertexColor, 1.0);
}