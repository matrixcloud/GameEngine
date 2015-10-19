#version 400 core

in vec3 color;
in vec2 pass_textureCoords;
in vec3 toLightVector;
in vec3 surfaceNormal;
in vec3 toCameraVector;
in float visibility;

out vec4 fragColor;

uniform sampler2D backgroundTexSampler;
uniform sampler2D rTexSampler;
uniform sampler2D gTexSampler;
uniform sampler2D bTexSampler;
uniform sampler2D blendTexSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(){
	vec4 blendTexColor = texture(blendTexSampler, pass_textureCoords);
	float backgroundTexAmount = 1 - (blendTexColor.r + blendTexColor.g + blendTexColor.b);
	vec2 tiledCoords = pass_textureCoords * 40;
	vec4 backgroundTexColor = texture(backgroundTexSampler, tiledCoords) * backgroundTexAmount;
	vec4 rTexColor = texture(rTexSampler, tiledCoords) * blendTexColor.r;
	vec4 gTexColor = texture(gTexSampler, tiledCoords) * blendTexColor.g;
	vec4 bTexColor = texture(bTexSampler, tiledCoords) * blendTexColor.b;
	vec4 totalColor = backgroundTexColor + rTexColor + gTexColor + bTexColor;
	

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

	fragColor = vec4(diffuse, 1.0) * totalColor
	 				+ vec4(finalSpecular, 1.0);
	fragColor = mix(vec4(skyColor, 1), fragColor, visibility);
}