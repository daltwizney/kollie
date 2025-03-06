#version 300 es
precision mediump float;

 uniform float ambient;

in vec3 vColor;

out vec4 fragColor;

void main() {
    fragColor = ambient * vec4(vColor, 1.0);
}