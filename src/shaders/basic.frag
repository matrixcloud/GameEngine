#version 400 core

in vec3 color;
in vec2 pass_textureCoords;
in vec3 toLightVector;
in vec3 surfaceNormal;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(){
	vec3 n = normalize(surfaceNormal);
	vec3 l = normalize(toLightVector);
	float cosTheta = clamp(dot(n, l), 0, 1);//brightness 
	vec3 diffuse = cosTheta * lightColor;

	fragColor = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords);
}