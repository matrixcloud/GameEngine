#version 400 core

in vec2 position;
uniform mat4 transformMat4;

out vec2 texCoords;

void main(){
	gl_Position = transformMat4 * vec4(position, 0, 1);
	texCoords = vec2((position.x + 1.0)/2.0, 1-(position.y + 1.0)/2.0);
}