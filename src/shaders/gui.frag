#version 400 core

in vec2 texCoords;
uniform sampler2D texSampler;

out vec4 fragColor;

void main(){
	fragColor = texture(texSampler, texCoords);
}