/*
 * Cleanroom Generator
 * Copyright (C) 2011-2012 nvx
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.nv.bukkit.CleanroomGenerator;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class CleanroomGenerator extends JavaPlugin
{
    private Logger log = Logger.getLogger("Minecraft");
    PluginDescriptionFile pluginDescriptionFile;

    public void onEnable()
    {
        pluginDescriptionFile = getDescription();
        log.info("[CleanroomGenerator] " + pluginDescriptionFile.getFullName() + " enabled");
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
