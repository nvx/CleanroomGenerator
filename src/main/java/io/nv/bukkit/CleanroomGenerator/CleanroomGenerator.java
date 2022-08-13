package io.nv.bukkit.CleanroomGenerator;

import org.bukkit.Bukkit;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

public class CleanroomGenerator extends JavaPlugin {

    private Logger log = Logger.getLogger("Minecraft");

    private String serverVersion;

    private Class<? extends ChunkGenerator> cleanRoomGenerator;

    @Override
    public void onEnable() {
        serverVersion = Bukkit.getVersion();

        if(Integer.valueOf(serverVersion.split("\\.")[1]) >= 17) {
            cleanRoomGenerator = NewCleanroomChunkGenerator.class;
            log.info("[CleanroomGenerator] " + getDescription().getFullName() + " enabled using the new 1.17.x+ generator.");
        } else {
            cleanRoomGenerator = CleanroomChunkGenerator.class;
            log.info("[CleanroomGenerator] " + getDescription().getFullName() + " enabled");
        }
    }

    @Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        try {
            return cleanRoomGenerator.getConstructor(String.class).newInstance(id);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
