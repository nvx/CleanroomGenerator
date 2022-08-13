package io.nv.bukkit.CleanroomGenerator;

import org.bukkit.Bukkit;
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

    NewCleanroomChunkGenerator(String id) {
        if (id == null || id.equals("")) {
            id = "64|stone";
        }

        if (id.equals(".")) {
            // Void world early exit to simplify later code
            layerBlock = new BlockData[0];
            layerHeight = new int[0];
            noBedrock = true; // FIX: no creation of bedrock on empty worlds
            return;
        }

        try {
            while (id.charAt(0) == '.' || id.charAt(0) == '^') {
                if (id.charAt(0) == '.') {
                    noBedrock = true;
                }
                if (id.charAt(0) == '^') {
                    newHeight = true;
                }
                id = id.substring(1);
            }
            if (!noBedrock) {
                // Unless the id starts with a '.' make the first layer bedrock
                id = "1|minecraft:bedrock|" + id;
            }

            String tokens[];

            tokens = id.split("[|]");

            if ((tokens.length % 2) != 0) throw new Exception();

            int layerCount = tokens.length / 2;
            layerBlock = new BlockData[layerCount];
            layerHeight = new int[layerCount];

            for (int i = 0; i < layerCount; i++) {
                int j = i * 2;
                int height = Integer.parseInt(tokens[j]);
                if (height <= 0) {
                    log.warning("[CleanroomGenerator] Invalid height '" + tokens[j] + "'. Using 64 instead.");
                    height = 64;
                }

                BlockData blockData;
                try {
                    blockData = Bukkit.createBlockData(tokens[j + 1]);
                } catch (Exception e) {
                    log.warning("[CleanroomGenerator] Failed to lookup block '" + tokens[j + 1] + "'. Using stone instead. Exception: " + e);
                    blockData = Material.STONE.createBlockData();
                }

                layerBlock[i] = blockData;
                layerHeight[i] = height;
            }
        } catch (Exception e) {
            log.severe("[CleanroomGenerator] Error parsing CleanroomGenerator ID '" + id + "'. using defaults '64,1': " + e);
            e.printStackTrace();

            layerBlock = new BlockData[2];
            layerBlock[0] = Material.BEDROCK.createBlockData();
            layerBlock[1] = Material.STONE.createBlockData();

            layerHeight = new int[2];
            layerHeight[0] = 1;
            layerHeight[1] = 64;
        }
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
