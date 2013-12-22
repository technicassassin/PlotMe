package com.worldcretornica.plotme;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class PlotGen extends ChunkGenerator {
	
	private double plotsize;
	private double pathsize;
	private short bottom;
	private short wall;
	private short plotfloor;
	private short filling;
	private short floor1;
	private short floor2;
	private int roadheight;
	private PlotMapInfo temppmi;
	
	public PlotGen()
	{
		plotsize = 32;
		pathsize = 7;
		bottom = 7;
		wall = 44;
		plotfloor = 2;
		filling = 3;
		roadheight = 64;
		floor1 = 5;
		floor2 = 5;
		temppmi = null;
		PlotMe.logger.warning(PlotMe.PREFIX + " Unable to find configuration, using defaults");
	}
	
	public PlotGen(PlotMapInfo pmi)
	{
		plotsize = pmi.PlotSize;
		pathsize = pmi.PathWidth;
		
		bottom = pmi.BottomBlockId;
		wall = pmi.WallBlockId;
		plotfloor = pmi.PlotFloorBlockId;
		filling = pmi.PlotFillingBlockId;
		roadheight = pmi.RoadHeight;
		floor1 = pmi.RoadMainBlockId;
		floor2 = pmi.RoadStripeBlockId;
		temppmi = pmi;
	}
	
	@Override
	public short[][] generateExtBlockSections(World world, Random random, int cx, int cz, BiomeGrid biomes)
	{
		int maxY = world.getMaxHeight();
		
		short[][] result = new short[maxY / 16][]; 
		
		double size = plotsize + pathsize;
		int valx;
		int valz;
		
		//floor1 = (short)Material.WOOL.getId();
		//floor2 = (short)Material.WOOD.getId();
		//byte air = (byte)Material.AIR.getId();
		
        for (int x = 0; x < 16; x++) {
        	
        for (int z = 0; z < 16; z++) {
            	
                int height = roadheight + 1;
                
            for (int y = 0; y < height; y++) {
            	
            	valx = cx * 16 + x - 64;
        		valz = cz * 16 + z - 64;
            	
        		if(y == 0)
            	{
            		//result[(x * 16 + z) * 128 + y] = bottom;
        			setBlock(result, x, y , z, bottom);
            	}
        		else if(y == roadheight)
            	{
        			if(valx % size == 0 || valz % size == 0){
        				
        				setBlock(result, x, y, z, floor1);
        			}
        			
            	}
        		else if(y == (roadheight + 1))
        			
            	{
        			
            	}else{
            		//result[(x * 16 + z) * 128 + y] = filling;
            		setBlock(result, x, y, z, filling);
            	}
            }
        }
        
        }
		
		return result;
	}
	
	private void setBlock(short[][] result, int x, int y, int z, short blkid) {
		
        if (result[y >> 4] == null) {
        	
            result[y >> 4] = new short[4096];
        }
        result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = blkid;
    }
	
	public List<BlockPopulator> getDefaultPopulators(World world)
	{
		if(temppmi == null)
		{
			return Arrays.asList((BlockPopulator)new PlotPopulator());
		}else{
			return Arrays.asList((BlockPopulator)new PlotPopulator(temppmi));
		}
    }

	public Location getFixedSpawnLocation(World world, Random random)
	{
		return new Location(world, 0, roadheight + 2, 0);
	}
}
