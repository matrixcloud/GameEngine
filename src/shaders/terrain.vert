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
out vec3 toCameraVector;
out float visibility;

const float density = 0.0035;
const float gradient = 5;


void main(void){
	vec4 position_worldspace = transformMat4 * vec4(position_modelspace, 1.0);
	vec4 position_cameraspace = viewMat4 * position_worldspace;
	gl_Position = projectionMat4 * position_cameraspace;
	pass_textureCoords = textureCoords;
	
	surfaceNormal = (transformMat4 * vec4(normal, 0)).xyz;
	toLightVector = lightPosition_worldspace - position_worldspace.xyz;
	toCameraVector = (inverse(viewMat4) * vec4(0, 0, 0, 1)).xyz - position_worldspace.xyz;
	
	float distance = length(position_cameraspace.xyz);
	visibility = exp(-pow(distance*density, gradient));
	visibility = clamp(visibility, 0, 1);
}