//
// Created by daltw on 2/5/2025.
//

#include <GLES3/gl31.h>

#include "collie/shader_program.h"

#include "collie/log.h"

using namespace glm;
using namespace std;

// Function to check shader compilation/linking errors
bool checkShaderErrors(GLuint shader, const std::string& type)
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

    return success;
}

ShaderProgram::ShaderProgram()
    : _programID(0)
{
}


void ShaderProgram::compile() {

    bool success = false;

    const char* vertShaderSource = this->vertexShaderSource.c_str();
    const char* fragShaderSource = this->fragmentShaderSource.c_str();

    // Vertex shader
    unsigned int vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &vertShaderSource, NULL);
    glCompileShader(vertexShader);

    success = checkShaderErrors(vertexShader, "VERTEX");

    if (!success)
    {
        _programID = 0;
        return;
    }

    // Fragment shader
    unsigned int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

    glShaderSource(fragmentShader, 1, &fragShaderSource, NULL);
    glCompileShader(fragmentShader);

    success = checkShaderErrors(fragmentShader, "FRAGMENT");

    if (!success)
    {
        _programID = 0;
        return;
    }

    // Shader program
    unsigned int programID = glCreateProgram();
    glAttachShader(programID, vertexShader);
    glAttachShader(programID, fragmentShader);
    glLinkProgram(programID);

    success = checkShaderErrors(programID, "PROGRAM");

    // Delete shaders after linking
    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);

    if (!success)
    {
        _programID = 0;
        return;
    }

    _programID = programID;
}

bool ShaderProgram::canUse() {

    return _programID > 0;
}

void ShaderProgram::use() {

    if (_programID > 0)
    {
        glUseProgram(_programID);
    }
}

float ShaderProgram::getUniform1f(string name) {
    return 0;
}

vec2 ShaderProgram::getUniform2f(string name) {
    return vec2();
}

vec3 ShaderProgram::getUniform3f(string name) {
    return vec3();
}

vec4 ShaderProgram::getUniform4f(string name) {
    return vec4();
}

int ShaderProgram::getUniform1i(string name) {
    return 0;
}

ivec2 ShaderProgram::getUniform2i(string name) {
    return ivec2();
}

ivec3 ShaderProgram::getUniform3i(string name) {
    return ivec3();
}

ivec4 ShaderProgram::getUniform4i(string name) {
    return ivec4();
}

void ShaderProgram::setUniform1f(string name, float x) {

}

void ShaderProgram::setUniform2f(string name, float x, float y) {

}

void ShaderProgram::setUniform3f(string name, float x, float y, float z) {

}

void ShaderProgram::setUniform4f(string name, float x, float y, float z, float w) {

}

void ShaderProgram::setUniform1i(string name, int x) {

}

void ShaderProgram::setUniform2i(string name, int x, int y) {

}

void ShaderProgram::setUniform3i(string name, int x, int y, int z) {

}

void ShaderProgram::setUniform4i(string name, int x, int y, int z, int w) {

}

