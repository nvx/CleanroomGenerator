package io.nv.bukkit.CleanroomGenerator;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CleanroomGenerator extends JavaPlugin
{
    private Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable()
    {
        log.info("[CleanroomGenerator] " + getDescription().getFullName() + " enabled");
    }

    @Override
    public void onDisable()
    {
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
    {
        return new CleanroomChunkGenerator(id);
    }
}
