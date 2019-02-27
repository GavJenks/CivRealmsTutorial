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
    public HashMap<String,PlayerProfile> profiles = new HashMap<String,PlayerProfile>(); 
    
    // Fired when plugin is first enabled
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CivRealmsTutorialListener(this), this);
    }
        
    // Fired when plugin is disabled
    @Override
    public void onDisable() {

    }
        
    public PlayerProfile getProfile(String UUID){
        return profiles.get(UUID);
    }
    
    public void putProfile(String UUID, PlayerProfile profile){
        profiles.put(UUID, profile);
    }
        
}
