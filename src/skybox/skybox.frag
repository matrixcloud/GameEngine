#version 400 core

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap;
uniform vec3 fogColor;

const float lowerLimit = 0;
const float upperLimit = 30;

void main(void){
	vec4 finalColor = texture(cubeMap, textureCoords);

	float factor = (textureCoords.y - lowerLimit)/(upperLimit - lowerLimit);
	factor = clamp(factor, 0, 1);
	out_Color = mix(vec4(fogColor, 1), finalColor, factor);
}