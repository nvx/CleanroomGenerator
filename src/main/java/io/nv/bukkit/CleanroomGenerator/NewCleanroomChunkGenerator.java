package io.nv.bukkit.CleanroomGenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * CleanroomGenerator; io.nv.bukkit.CleanroomGenerator:NewCleanroomChunkGenerator
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 13.08.2022
 */
public class NewCleanroomChunkGenerator extends ChunkGenerator {
    private Logger log = Logger.getLogger("Minecraft");
    private BlockData[] layerBlock;
    private int[] layerHeight;
    private boolean noBedrock = false;
    private boolean newHeight = false;

    public NewCleanroomChunkGenerator() {
        this("");
    }

    public NewCleanroomChunkGenerator(String id) {
        IDParser parser = new IDParser(id);
        this.layerBlock = parser.getLayerBlock();
        this.layerHeight = parser.getLayerHeight();
        this.noBedrock = parser.isNoBedrock();
        this.newHeight = parser.isNewHeight();
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        if (!world.isChunkLoaded(0, 0)) {
            world.loadChunk(0, 0);
        }
        int highestBlock = world.getHighestBlockYAt(0, 0);
        if (highestBlock <= 0 && world.getBlockAt(0, 0, 0).getType() == Material.AIR) {
            return new Location(world, 0.0D, 64.0D, 0.0D);
        }
        return new Location(world, 0.0D, highestBlock, 0.0D);
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        int y = 0;
        if (this.newHeight) {
            y = -64;
        }
        for (int i = 0; i < this.layerBlock.length; i++) {
            chunkData.setRegion(0, y, 0, 16, y + this.layerHeight[i], 16, this.layerBlock[i]);
            y += this.layerHeight[i];
        }
    }

    @Override
    public void generateBedrock(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        // nothing -> bedrock is generated on surface method
    }

    @Override
    public BiomeProvider getDefaultBiomeProvider(WorldInfo worldInfo) {
        return new BiomeProvider() {
            @Override
            public Biome getBiome(WorldInfo worldInfo, int coordsX, int coordsY, int coordsZ) {
                return Biome.FOREST; // set default biome to a normal FOREST biome
            }

            @Override
            public List<Biome> getBiomes(WorldInfo worldInfo) {
                ArrayList<Biome> biomes = new ArrayList<>();
                biomes.add(Biome.FOREST); // only apply biome FOREST to the world
                return biomes;
            }
        };
    }

}
