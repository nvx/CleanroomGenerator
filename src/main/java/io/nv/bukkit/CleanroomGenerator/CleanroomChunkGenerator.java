package io.nv.bukkit.CleanroomGenerator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;
import java.util.logging.Logger;

public class CleanroomChunkGenerator extends ChunkGenerator {
    private Logger log = Logger.getLogger("Minecraft");
    private BlockData[] layerBlock;
    private int[] layerHeight;
    private boolean noBedrock = false;
    private boolean newHeight = false;

    public CleanroomChunkGenerator() {
        this("");
    }

    public CleanroomChunkGenerator(String id) {
        IDParser parser = new IDParser(id);
        this.layerBlock = parser.getLayerBlock();
        this.layerHeight = parser.getLayerHeight();
        this.noBedrock = parser.isNoBedrock();
        this.newHeight = parser.isNewHeight();
    }

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);

        int y = 0;
        if (newHeight) {
            y = -64;
        }
        for (int i = 0; i < layerBlock.length; i++) {
            chunk.setRegion(0, y, 0, 16, y + layerHeight[i], 16, layerBlock[i]);
            y += layerHeight[i];
        }

        return chunk;
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random) {
        if (!world.isChunkLoaded(0, 0)) {
            world.loadChunk(0, 0);
        }

        int highestBlock = world.getHighestBlockYAt(0, 0);

        if ((highestBlock <= 0) && (world.getBlockAt(0, 0, 0).getType() == Material.AIR)) // SPACE!
        {
            return new Location(world, 0, 64, 0); // Let's allow people to drop a little before hitting the void then shall we?
        }

        return new Location(world, 0, highestBlock, 0);
    }
}
