#version 300 es
precision mediump float;

layout(location = 0) in vec3 position;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    
    // Combine the transformations and apply them to the vertex position
    gl_Position = projection * view * model * vec4(position, 1.0);
}