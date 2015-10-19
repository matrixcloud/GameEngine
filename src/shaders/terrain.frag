#version 400 core

in vec3 color;
in vec2 pass_textureCoords;
in vec3 toLightVector;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in float visibility;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

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

	fragColor = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords)
	 				+ vec4(finalSpecular, 1.0);
	fragColor = mix(vec4(skyColor, 1), fragColor, visibility);
}