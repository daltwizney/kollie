#version 300 es
precision mediump float;

layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aColor;
layout(location = 2) in vec3 aNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

uniform mat3 normalMatrix;

out vec3 vColor;
out vec3 vNormal;

void main() {
    
    gl_Position = projection * view * model * vec4(aPos, 1.0);

    vColor = aColor;

    vNormal = normalize(normalMatrix * aNormal);
}