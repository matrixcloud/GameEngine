#version 400 core

in vec3 position_modelspace;
in vec2 textureCoords;
in vec3 normal;

uniform mat4 transformMat4;
uniform mat4 viewMat4;
uniform mat4 projectionMat4;
uniform vec3 lightPosition_worldspace;

out vec2 pass_textureCoords;
out vec3 toLightVector;
out vec3 surfaceNormal;

void main(void){
	vec4 position_worldspace = transformMat4 * vec4(position_modelspace, 1.0);
	gl_Position = projectionMat4 * viewMat4 * position_worldspace;
	pass_textureCoords = textureCoords;
	
	surfaceNormal = (transformMat4 * vec4(normal, 0)).xyz;
	toLightVector = lightPosition_worldspace - position_worldspace.xyz;
}