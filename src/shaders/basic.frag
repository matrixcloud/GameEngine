#version 400 core

in vec3 color;
in vec2 pass_textureCoords;
in vec3 toLightVector;
in vec3 surfaceNormal;
in vec3 toCameraVector;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;


void main(){
	vec3 n = normalize(surfaceNormal);
	vec3 l = normalize(toLightVector);
	float cosTheta = clamp(dot(n, l), 0, 1);//brightness 
	vec3 diffuse = cosTheta * lightColor;

	vec3 unitToCameraVector = normalize(toCameraVector);
	vec3 incomingLight = -l;
	vec3 reflectedLightDirection = reflect(incomingLight, n);
	float specularFactor = clamp(dot(reflectedLightDirection, unitToCameraVector), 0, 1);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = reflectivity * dampedFactor * lightColor;

	fragColor = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords)
	 				+ vec4(finalSpecular, 1.0);
}