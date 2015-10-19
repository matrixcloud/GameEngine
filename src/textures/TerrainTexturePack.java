package textures;

public class TerrainTexturePack {
	private TerrainTexture backgroundTex;
	private TerrainTexture rTex;
	private TerrainTexture gTex;
	private TerrainTexture bTex;
	
	public TerrainTexturePack(TerrainTexture backgroundTex, TerrainTexture rTex, TerrainTexture gTex,
			TerrainTexture bTex) {
		this.backgroundTex = backgroundTex;
		this.rTex = rTex;
		this.gTex = gTex;
		this.bTex = bTex;
	}

	public TerrainTexture getBackgroundTex() {
		return backgroundTex;
	}

	public TerrainTexture getrTex() {
		return rTex;
	}

	public TerrainTexture getgTex() {
		return gTex;
	}

	public TerrainTexture getbTex() {
		return bTex;
	}
}
