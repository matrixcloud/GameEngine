#version 400 core

in vec2 pass_textureCoords;
in vec3 toLightVector[4];
in vec3 surfaceNormal;
in vec3 toCameraVector;
in float visibility;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(){
	vec3 n = normalize(surfaceNormal);
	vec3 unitToCameraVector = normalize(toCameraVector);	
	
	vec3 totalDiffuse = vec3(0);
	vec3 totalSpecular = vec3(0);
	
	for(int i = 0; i < 4; i++){
		//*********Calculate diffuse**********//
		vec3 l = normalize(toLightVector[i]);
		float cosTheta = dot(n, l);
		float brightness = max(cosTheta, 0.0); 
		vec3 diffuse = brightness * lightColor[i];
		totalDiffuse += diffuse;
	
		//*********Calculate specular**********//
		vec3 incomingLight = -l;
		vec3 reflectedLightDirection = reflect(incomingLight, n);
		float specularFactor = dot(reflectedLightDirection, unitToCameraVector);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		vec3 finalSpecular = reflectivity * dampedFactor * lightColor[i];
		totalSpecular += finalSpecular;
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	vec4 textureColor = texture(textureSampler, pass_textureCoords);
	if(textureColor.a < 0.5f){
		discard;
	}

	fragColor = vec4(totalDiffuse, 1.0) * textureColor
	 				+ vec4(totalSpecular, 1.0);
	fragColor = mix(vec4(skyColor, 1), fragColor, visibility);
}