#version 300 es
precision mediump float;

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec3 quadPosition;
layout(location = 2) in vec3 quadColor;

// uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

out vec3 myVertexColor;

void main() {
    
    // gl_Position = projection * view * model * vec4(position, 1.0);

    gl_Position = projection * view * vec4(vertexPosition + quadPosition, 1.0);

    myVertexColor = quadColor;
}