#version 400 core

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
	float cosTheta = dot(n, l);
	float brightness = max(cosTheta, 0.2); 
	vec3 diffuse = brightness * lightColor;

	vec3 unitToCameraVector = normalize(toCameraVector);
	vec3 incomingLight = -l;
	vec3 reflectedLightDirection = reflect(incomingLight, n);
	float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = reflectivity * dampedFactor * lightColor;

	vec4 textureColor = texture(textureSampler, pass_textureCoords);

	fragColor = vec4(diffuse, 1.0) * textureColor
	 				+ vec4(finalSpecular, 1.0);
}