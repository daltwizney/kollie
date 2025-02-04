//
// Created by daltw on 2/2/2025.
//
#include <string>

#include <GLES3/gl31.h>

#include "collie/log.h"

#include "collie/renderer.h"

// Vertex shader source
const char* vertexShaderSource = R"(#version 300 es
precision mediump float;
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

out vec2 TexCoord;

void main()
{
    gl_Position = vec4(aPos, 1.0);
    TexCoord = aTexCoord;
}
)";

// Fragment shader source
const char* fragmentShaderSource = R"(#version 300 es
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
    float radius = 0.2;

    // Smooth edge
    float smoothWidth = 0.01;
    float circle = smoothstep(radius + smoothWidth, radius - smoothWidth, distance);

    // Output color (white circle on black background)
    FragColor = vec4(vec3(circle), 1.0);
}
)";

// Function to check shader compilation/linking errors
void checkShaderErrors(GLuint shader, const std::string& type)
{
    GLint success;
    GLchar infoLog[1024];

    if (type != "PROGRAM")
    {
        glGetShaderiv(shader, GL_COMPILE_STATUS, &success);
        if (!success)
        {
            glGetShaderInfoLog(shader, 1024, NULL, infoLog);
            LOGE("ERROR::SHADER_COMPILATION_ERROR of type: %s\n%s\n", type.c_str(), infoLog);
        }
    }
    else
    {
        glGetProgramiv(shader, GL_LINK_STATUS, &success);
        if (!success)
        {
            glGetProgramInfoLog(shader, 1024, NULL, infoLog);
            LOGE("ERROR::PROGRAM_LINKING_ERROR of type: %s\n%s\n", type.c_str(), infoLog);
        }
    }
}

void Renderer::init() {

    // set clear color
    glClearColor(1.0f, 0.0f, 1.0f, 1.0f);

    _frameCounter = 0;

    // Create and compile shaders
    // Vertex shader
    unsigned int vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &vertexShaderSource, NULL);
    glCompileShader(vertexShader);
    checkShaderErrors(vertexShader, "VERTEX");

    // Fragment shader
    unsigned int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL);
    glCompileShader(fragmentShader);
    checkShaderErrors(fragmentShader, "FRAGMENT");

    // Shader program
    _shaderProgram = glCreateProgram();
    glAttachShader(_shaderProgram, vertexShader);
    glAttachShader(_shaderProgram, fragmentShader);
    glLinkProgram(_shaderProgram);
    checkShaderErrors(_shaderProgram, "PROGRAM");

    // Delete shaders after linking
    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);

    // Vertex data for fullscreen quad
    float vertices[] = {
            // positions        // texture coords
            -1.0f,  1.0f, 0.0f, 0.0f, 1.0f,   // top left
            -1.0f, -1.0f, 0.0f, 0.0f, 0.0f,   // bottom left
            1.0f,  1.0f, 0.0f, 1.0f, 1.0f,   // top right
            1.0f, -1.0f, 0.0f, 1.0f, 0.0f    // bottom right
    };

    // Create VAO and VBO
    glGenVertexArrays(1, &_screenQuadVAO);
    glGenBuffers(1, &_screenQuadVBO);

    glBindVertexArray(_screenQuadVAO);
    glBindBuffer(GL_ARRAY_BUFFER, _screenQuadVBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STATIC_DRAW);

    // Position attribute
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)0);
    glEnableVertexAttribArray(0);

    // Texture coordinate attribute
    glVertexAttribPointer(1, 2, GL_FLOAT, GL_FALSE, 5 * sizeof(float), (void*)(3 * sizeof(float)));
    glEnableVertexAttribArray(1);

    // Setup uniforms
    _resolutionLocation = glGetUniformLocation(_shaderProgram, "resolution");

    LOGI("OpenGL initialized!");
}

void Renderer::resize(int width, int height) {

    glViewport(0, 0, width, height);

    _width = width;
    _height = height;

    LOGI("Surface resized to %dx%d", width, height);
}

void Renderer::draw() {

    // Clear the screen
    glClear(GL_COLOR_BUFFER_BIT);

    // Draw quad
    glUseProgram(_shaderProgram);

    glUniform2f(_resolutionLocation, (float) _width, (float) _height);

    glBindVertexArray(_screenQuadVAO);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

    _frameCounter++;
}

void Renderer::setShaderSource(std::string& vertexShaderSrc, std::string& fragmentShaderSrc)
{
    _vertexShaderSrc = vertexShaderSrc;
    _fragmentShaderSrc = fragmentShaderSrc;

    LOGD("vertex shader = %s", _vertexShaderSrc.c_str());
    LOGD("frag shader = %s", _fragmentShaderSrc.c_str());
}

int Renderer::frameCounter() {

    return _frameCounter;
}

void Renderer::destroy() {

    if (_screenQuadVAO != GL_INVALID_VALUE)
    {
        glDeleteVertexArrays(1, &_screenQuadVAO);
        _screenQuadVAO = GL_INVALID_VALUE;
    }

    if (_screenQuadVBO != GL_INVALID_VALUE)
    {
        glDeleteBuffers(1, &_screenQuadVBO);
        _screenQuadVBO = GL_INVALID_VALUE;
    }

    if (_shaderProgram == 0)
    {
        glDeleteProgram(_shaderProgram);
        _shaderProgram = 0;
    }
}