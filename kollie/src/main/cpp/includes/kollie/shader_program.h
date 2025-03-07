//
// Created by daltw on 2/5/2025.
//

#ifndef KOLLIE_SHADER_PROGRAM_H
#define KOLLIE_SHADER_PROGRAM_H

#include <string>
#include <unordered_map>
#include "glm/glm.hpp"

class ShaderProgram {

public:

    ShaderProgram();

    std::string vertexShaderSource;
    std::string fragmentShaderSource;

    unsigned int id();

    void compile();

    bool canUse();

    void use();

    void destroy(bool freeGLResources = true);

    float getUniform1f(std::string name);
    glm::vec2 getUniform2f(std::string name);
    glm::vec3 getUniform3f(std::string name);
    glm::vec4 getUniform4f(std::string name);

    int getUniform1i(std::string name);
    glm::ivec2 getUniform2i(std::string name);
    glm::ivec3 getUniform3i(std::string name);
    glm::ivec4 getUniform4i(std::string name);

    void setUniform1f(std::string name, float x);
    void setUniform2f(std::string name, float x, float y);
    void setUniform3f(std::string name, float x, float y, float z);
    void setUniform4f(std::string name, float x, float y, float z, float w);

    void setUniform1i(std::string name, int x);
    void setUniform2i(std::string name, int x, int y);
    void setUniform3i(std::string name, int x, int y, int z);
    void setUniform4i(std::string name, int x, int y, int z, int w);

    // TODO: getUniformMatrix*(...), setUniformMatrix*(...)

    void setUniformMatrix3fv(std::string name,
                             unsigned int count,
                             bool transpose,
                             const float* value);

    void setUniformMatrix4fv(std::string name,
                             unsigned int count,
                             bool transpose,
                             const float* value);

    int getUniformLocation(std::string name);

private:

    unsigned int _programID;

    std::unordered_map<std::string, int> _uniformLocations;
};



#endif //KOLLIE_SHADER_PROGRAM_H
