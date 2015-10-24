#version 400 core

in vec3 textureCoords;
out vec4 out_Color;

uniform samplerCube cubeMap1;
uniform samplerCube cubeMap2;
uniform float blendFactor;
uniform vec3 fogColor;


const float lowerLimit = 0;
const float upperLimit = 30;

void main(void){
	vec4 color1 = texture(cubeMap1, textureCoords);
	vec4 color2 = texture(cubeMap2, textureCoords);
	vec4 finalColor = mix(color1, color2, blendFactor);

	float factor = (textureCoords.y - lowerLimit)/(upperLimit - lowerLimit);
	factor = clamp(factor, 0, 1);
	out_Color = mix(vec4(fogColor, 1), finalColor, factor);
}