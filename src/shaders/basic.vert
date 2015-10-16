#version 400 core

in vec3 position_modelspace;
in vec2 textureCoords;
uniform mat4 transformMat4;

out vec2 pass_textureCoords;

void main(void){
	gl_Position = vec4(position_modelspace, 1.0);
	pass_textureCoords = textureCoords;
}