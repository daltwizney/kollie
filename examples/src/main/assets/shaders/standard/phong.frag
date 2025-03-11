#version 300 es
precision mediump float;

uniform float ambient;

in vec3 vColor;
in vec3 vNormal;

out vec4 fragColor;

void main() {

    // light is pointing towards object from camera, tilted 'downward' at 45 degrees
    vec3 lightDirection = -normalize(vec3(0, -1, -1)); // in view coordinates

    float diffuseFactor = max(dot(lightDirection, vNormal), 0.0);

    float lightIntensity = 1.0;
    vec3 lightColor = vec3(1, 1, 1);
    vec3 diffuseColor = lightIntensity * diffuseFactor * lightColor * vColor;

    vec3 ambientColor = ambient * vColor;

    fragColor = vec4(ambientColor + diffuseColor, 1.0);
}