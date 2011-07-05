package net.neo_vortex.bukkit.CleanroomGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class CleanroomChunkGenerator extends ChunkGenerator
{
    private int height;
    private byte block;

    public CleanroomChunkGenerator()
    {
        height = 64;
        block = (byte)Material.STONE.getId();
    }

    public CleanroomChunkGenerator(String id)
    {
        if (id != null)
        {
            try
            {
                String tokens[] = id.split("[,]");
                height = Integer.parseInt(tokens[0]);
                if ((height < 0) || (height > 127))
                {
                    System.out.println("Invalid CleanroomGenerator ID '" + id + "'. Invalid height. Using 63 instead.");
                    height = 64;
                }

                Material mat = Material.getMaterial(tokens[1]);
                if (mat == null)
                {
                    mat = Material.getMaterial(Integer.parseInt(tokens[1]));
                }
                if (mat == null)
                {
                    System.out.println("Invalid CleanroomGenerator ID '" + id + "'. Invalid block #. Using stone instead.");
                    block = 1;
                } else
                {
                    block = (byte)mat.getId();
                }
            } catch(Exception e)
            {
                height = 64;
                block = (byte)Material.STONE.getId();
                System.out.println("Error parsing CleanroomGenerator ID '" + id + "'. using defaults (64,1).");
            }
        } else
        {
            height = 64;
            block = (byte)Material.STONE.getId();
        }
    }

    public byte[] generate(World world, Random random, int cx, int cz)
    {
        byte[] chunk = new byte[32768];
        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                chunk[(x * 16 + z) * 128] = (byte)Material.BEDROCK.getId(); // Bottom layer is always bedrock.
                for (int y = 1; y < height; y++)
                {
                    chunk[(x * 16 + z) * 128 + y] = block;
                }
            }
        }
        return chunk;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world)
    {
        // This is the default, but just in case default populators change to stock minecraft populators by default...
        return new ArrayList<BlockPopulator>();
    }

    @Override
    public Location getFixedSpawnLocation(World world, Random random)
    {
        return new Location(world, 0, height + 2, 0);
    }
}
