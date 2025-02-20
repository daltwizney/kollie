#define PI 3.14159

#define DEGREES_TO_RADIANS(val) ((val) * PI / 180.0)

in vec2 v_uv;

uniform sampler2D u_tex;

uniform float u_time;

out vec4 fragColor;

vec2 rotate(vec2 point, float theta) {

    // build 2D (clockwise) rotation matrix
    float c = cos(theta);
    float s = sin(theta);

    // note: if you want counter-clockwise,
    // you should do mat2(c, -s, s, c)

    // clockwise more common in CG
    mat2 mat = mat2(c, s, -s, c);

    // TODO: would distort img if the aspect ratio of the quad != 1.0

    // return rotated point
    return mat * point;
}

float inRect(vec2 point, vec2 bottomLeft, vec2 topRight) {

    vec2 s = step(bottomLeft, point) - step(topRight, point);
    return s.x * s.y;
}

void main() {

    float rotationSpeed = 0.5;

    float angle = u_time * 0.2;
    // float angle = DEGREES_TO_RADIANS(42.0f);

    // center around origin (-0.5 to 0.5)
    vec2 rotatedUV = v_uv - 0.5;

    // rotate about origin
    rotatedUV = rotate(rotatedUV, angle);

    // shift back into quad coords (0.0 to 1.0)
    rotatedUV = rotatedUV + 0.5;

    vec3 bg = vec3(0.0);
    vec3 texel = texture2D(u_tex, rotatedUV).rgb;

    /**
     *  This is a clever trick - we're actually comparing the rotated
     *  UV coordinate to the non-rotated coordinate space.
     *
     *  It takes of advantage of the fact that asking "is this rotated
     *  UV coordinate inside the rotated coordinate space?" is equivalent
     *  to asking "was the point that rotated into this position originally
     *  in the unrotated coordinated space?"
     *
     */
    float a = inRect(rotatedUV, vec2(0.0), vec2(1.0));

    vec3 color = mix(bg, texel, a);

    fragColor = vec4(color, 1.0);
}