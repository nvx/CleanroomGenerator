package io.nv.bukkit.CleanroomGenerator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.logging.Logger;

/**
 * CleanroomGenerator; io.nv.bukkit.CleanroomGenerator:IDParser
 *
 * @author LuciferMorningstarDev - https://github.com/LuciferMorningstarDev
 * @since 13.08.2022
 */
public class IDParser {
    private Logger log = Logger.getLogger("Minecraft");
    private BlockData[] layerBlock;
    private int[] layerHeight;
    private boolean noBedrock = false;
    private boolean newHeight = false;

    public IDParser(String id) {
        if (id == null || id.equals("")) {
            id = "64|stone";
        }

        if (id.equals(".")) {
            // Void world early exit to simplify later code
            layerBlock = new BlockData[0];
            layerHeight = new int[0];
            noBedrock = true;
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
                id = "1|bedrock|" + id;
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

    public BlockData[] getLayerBlock() {
        return layerBlock;
    }

    public int[] getLayerHeight() {
        return layerHeight;
    }

    public boolean isNewHeight() {
        return newHeight;
    }

    public boolean isNoBedrock() {
        return noBedrock;
    }

}
