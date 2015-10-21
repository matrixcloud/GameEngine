package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import utils.Maths;

public class TerrainShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/shaders/terrain.vert";
	private static final String FRAGMENT_FILE = "src/shaders/terrain.frag";
	private static final int MAX_LIGHTS = 4;
	private int transformMat4Loc;
	private int projectionMat4Loc;
	private int viewMat4Loc;
	private int lightPosition_worldspaceLoc[];
	private int lightColorLoc[];
	private int shineDamperLoc;
	private int reflectivityLoc;
	private int backgroundTexSamplerLoc;
	private int rTexSamplerLoc;
	private int gTexSamplerLoc;
	private int bTexSamplerLoc;
	private int blendTexSamplerLoc;
	private int skyColorLoc;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttributes(0, "position_modelspace");
		super.bindAttributes(1, "textureCoords");
		super.bindAttributes(2, "normal");
	}

	@Override
	protected void getAllUiformLocations() {
		transformMat4Loc = super.getUniformLocation("transformMat4");
		projectionMat4Loc = super.getUniformLocation("projectionMat4");
		viewMat4Loc = super.getUniformLocation("viewMat4");
		shineDamperLoc = super.getUniformLocation("shineDamper");
		reflectivityLoc = super.getUniformLocation("reflectivity");
		backgroundTexSamplerLoc = super.getUniformLocation("backgroundTexSampler");
		rTexSamplerLoc = super.getUniformLocation("rTexSampler");
		gTexSamplerLoc = super.getUniformLocation("gTexSampler");
		bTexSamplerLoc = super.getUniformLocation("bTexSampler");
		blendTexSamplerLoc = super.getUniformLocation("blendTexSampler");
		skyColorLoc = super.getUniformLocation("skyColor");
		
		lightPosition_worldspaceLoc = new int[MAX_LIGHTS];
		lightColorLoc = new int[MAX_LIGHTS];
		for(int i = 0; i < MAX_LIGHTS; i++){
			lightPosition_worldspaceLoc[i] = 
					this.getUniformLocation("lightPosition_worldspace[" + i + "]");
			lightColorLoc[i] = this.getUniformLocation("lightColor[" + i + "]");
		}
	}
	
	public void connectTextureUnits(){
		super.setInt(backgroundTexSamplerLoc, 0);
		super.setInt(rTexSamplerLoc, 1);
		super.setInt(gTexSamplerLoc, 2);
		super.setInt(bTexSamplerLoc, 3);
		super.setInt(blendTexSamplerLoc, 4);
	}
	
	public void setShineVariables(float damper, float reflectivity){
		super.setFloat(shineDamperLoc, damper);
		super.setFloat(reflectivityLoc, reflectivity);
	}

	public void setTransformMat4(Matrix4f mat4){
		super.setMatrix4(transformMat4Loc, mat4);
	}
	
	public void setProjectionMat4(Matrix4f mat4){
		super.setMatrix4(projectionMat4Loc, mat4);;
	}
	
	public void setViewMat4(Camera camera){
		Matrix4f viewMat4 = Maths.createViewMatrix4(camera);
		super.setMatrix4(viewMat4Loc, viewMat4);
	}
	
	public void setLights(List<Light> lights){
		for(int i = 0; i < MAX_LIGHTS; i++){
			if(i < lights.size()){
				Light light = lights.get(i);
				super.setVector(lightPosition_worldspaceLoc[i], light.getPosition());
				super.setVector(lightColorLoc[i], light.getColor());
			}else{
				super.setVector(lightPosition_worldspaceLoc[i], new Vector3f(0, 0, 0));
				super.setVector(lightColorLoc[i], new Vector3f(0, 0, 0));
			}
		}
	}
	
	public void setSkyColor(float r, float g, float b){
		super.setVector(skyColorLoc, new Vector3f(r, g, b));
	}
}
