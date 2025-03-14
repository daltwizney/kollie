#version 300 es
precision mediump float;
out vec4 FragColor;
in vec2 TexCoord;

uniform vec2 resolution;

void main()
{
    // scale tex coords based on aspect ratio
    float aspectRatio = resolution.x / resolution.y;

    vec2 uv = TexCoord * 2.0 - 1.0;
    uv.x *= aspectRatio;

    // Calculate distance from center
    vec2 center = vec2(0.0, 0.0);
    float distance = length(uv - center);

    // Circle radius
    float radius = 0.5;

    // Smooth edge
    float smoothWidth = 0.01;
    float circle = smoothstep(radius + smoothWidth, radius - smoothWidth, distance);

    vec3 color = vec3(0.2f, 0.3f, 0.3f);

    // Output color (white circle on black background)
    FragColor = vec4(circle * color, 1.0);
}