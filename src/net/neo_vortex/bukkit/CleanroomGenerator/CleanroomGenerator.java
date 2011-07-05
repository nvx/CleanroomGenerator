package net.neo_vortex.bukkit.CleanroomGenerator;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class CleanroomGenerator extends JavaPlugin
{
    PluginDescriptionFile pluginDescriptionFile;

    public void onEnable()
    {
        pluginDescriptionFile = getDescription();
        System.out.println(pluginDescriptionFile.getName() + " version " + pluginDescriptionFile.getVersion() + " is enabled!");
    }

    public void onDisable()
    {
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id)
    {
        return new CleanroomChunkGenerator(id);
    }
}
