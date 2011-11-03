package net.neo_vortex.bukkit.CleanroomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.Material;

// Add grid on the ground.
public class GridPopulator extends BlockPopulator {
	
	static public class GridSetting {
		public GridSetting(int width, Material material) {
			this.width = width;
			this.material = material;
		}
		public int getWidth() {
			return width;
		}
		public Material getMaterial() {
			return material;
		}
		private int width;
		private Material material;
		
	}
	
	// e.g. "6,sandstone,3,sand,2,cobblestone,1,gravel"
	// means there will be 12-width sandstone lines between every region (32*32 chunks = 512*512 blocks),
	// 6-width sand lines divide it into 4 parts,
	// 4-width cobblestones line divide every part into 4 small parts,
	// 2-width gravel lines divide every small part into 4 more small parts.
	// A region is like this: http://i.imgur.com/UHdja.png
	public GridPopulator(String settings) {
		String tokens[] = settings.split("[,]");

        if ((tokens.length % 2) != 0)
        	return;

        List<GridSetting> gridSettings = new ArrayList<GridSetting>();
        try {
	        for (int i = 0; i < tokens.length; i += 2)
	        {
	        	int width = Integer.parseInt(tokens[i]);
	        	Material mat = Material.matchMaterial(tokens[i + 1]);
	        	gridSettings.add(new GridSetting(width, mat));
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        }
        if(gridSettings.size() > 0)
        	this.gridSettings = gridSettings;
	}

	public GridPopulator(List<GridSetting> gridSettings) {
		this.gridSettings = gridSettings;
		if(this.gridSettings != null && this.gridSettings.size() > 5) {
			this.gridSettings = new ArrayList<GridSetting>(this.gridSettings.subList(0, 8));
		}
	}
	
	protected List<GridSetting> gridSettings = null;
	
	@Override
	public void populate(World world, Random random, Chunk source) {
		if(this.gridSettings == null) {
			return;
		}
		
		GridSetting xGridSetting = null;
		boolean xGridTop = false;
		int xGridLayer = 0;
		GridSetting zGridSetting = null;
		boolean zGridTop = false;
		int zGridLayer = 0;
		for(int i=0, mask=0x1f; i<this.gridSettings.size(); ++i, mask >>= 1) {
			int textureX = source.getX() & mask;
			int textureZ = source.getZ() & mask;
			
			if(xGridSetting == null && (textureX == 0 || textureX == mask)) {
				xGridSetting = this.gridSettings.get(i);
				xGridLayer = i;
				xGridTop = textureX == mask;
				if(zGridSetting != null)
					break;
			}
			if(zGridSetting == null && (textureZ == 0 || textureZ == mask)) {
				zGridSetting = this.gridSettings.get(i);
				zGridLayer = i;
				zGridTop = textureZ == mask;
				if(xGridSetting != null)
					break;
			}
		}
		int baseX = source.getX() << 4;
		int baseZ = source.getZ() << 4;
		int xIncrease = xGridTop ? -1 : 1;
		int zIncrease = zGridTop ? -1 : 1;
		int baseXd = xGridTop ? 15 : 0;
		int baseZd = zGridTop ? 15 : 0;
		if(xGridLayer > zGridLayer) {
			drawGridX(baseX + baseXd, baseZ, xIncrease, xGridSetting, world);
			drawGridZ(baseX, baseZ + baseZd, zIncrease, zGridSetting, world);
		} else {
			drawGridZ(baseX, baseZ + baseZd, zIncrease, zGridSetting, world);
			drawGridX(baseX + baseXd, baseZ, xIncrease, xGridSetting, world);
		}
	}
	
	private void drawGridX(int baseX, int baseZ, int xIncrease, GridSetting xGridSetting, World world) {
		if(xGridSetting == null)
			return;
		for(int i=0, x=baseX; i<xGridSetting.getWidth(); ++i, x+=xIncrease) {
			for(int z=baseZ; z<baseZ+16; ++z) {
				int y = world.getHighestBlockYAt(x, z);
				y = y>0 ? y-1 : 0;
				world.getBlockAt(x, y, z).setType(xGridSetting.getMaterial()); 
			}
		}
	}
	
	private void drawGridZ(int baseX, int baseZ, int zIncrease, GridSetting zGridSetting, World world) {
		if(zGridSetting == null)
			return;
		for(int x=baseX; x<baseX+16; ++x) {
			for(int i=0, z=baseZ; i<zGridSetting.getWidth(); ++i, z+=zIncrease) {
				int y = world.getHighestBlockYAt(x, z);
				y = y>0 ? y-1 : 0;
				world.getBlockAt(x, y, z).setType(zGridSetting.getMaterial()); 
			}
		}
	}

}
