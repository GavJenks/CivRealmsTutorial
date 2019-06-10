/*
 * This project is licensed under GPLv3
 * https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.civrealms.civrealmstutorial;

/**
 *
 * @author crimeo
 */

import org.bukkit.plugin.java.JavaPlugin;
import java.util.HashMap;

public class CivRealmsTutorial extends JavaPlugin {
    
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CivRealmsTutorialListener(this), this);
    }
        
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
    
    //will want to have some op commands to manually clear out or set things about a profile, especially since no database means we can't do it in sql
    //need to still add a serialized object save/load, currently the tutorial starts over every restart.
        
}
