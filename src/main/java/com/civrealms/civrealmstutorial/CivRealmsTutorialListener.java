/*
 * This project is licensed under GPLv3
 * https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.civrealms.civrealmstutorial;

/**
 *
 * @author crimeo
 */

import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.ChatColor;

public class CivRealmsTutorialListener implements Listener {
    
    private CivRealmsTutorial plugin;
    public static Logger LOG = Logger.getLogger("CivRealmsPVE");
    
    public CivRealmsTutorialListener(CivRealmsTutorial plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event){
        String uuid = event.getPlayer().getUniqueId().toString();
        if (event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2){
            if (plugin.profiles.containsKey(uuid)){
                PlayerProfile profile = plugin.profiles.get(uuid);
                if (profile.triggerHistory[0]%10 == 1){ //the first and every 10 more times you try and fail
                    
                    //do tutorial stuff
                    event.getPlayer().sendMessage(ChatColor.RED + "TUTORIAL: You cannot break trees by hand. Make a primitive axe instead by using the regular minecraft axe recipe but with either bone or flint for the head of the axe. All animals drop bones. You can get sticks for your tools by punching leaves. There are similar recipes for all other primitive tools.");
                    
                    profile.triggerHistory[0] = (byte)Math.min(255,profile.triggerHistory[0]++); //eventually it will stop pestering you.
                }
            }
        }
        //Ores: explain hiddenore, etc.
        //plants: explain RB
    }
    
}
