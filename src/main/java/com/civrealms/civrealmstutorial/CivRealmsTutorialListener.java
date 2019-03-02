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
    
    //listen for login and add profile to map.
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event){
        String uuid = event.getPlayer().getUniqueId().toString();
        if (event.isCancelled()){ //fail to chop a log
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
            //PUT ANY OTHER FAILED MINING CHECKS HERE
        } else{
            if (event.getBlock().getType() == Material.IRON_ORE //Ores: explain hiddenore, etc.
                    || event.getBlock().getType() == Material.GOLD_ORE
                    || event.getBlock().getType() == Material.LAPIS_ORE
                    || event.getBlock().getType() == Material.EMERALD_ORE
                    || event.getBlock().getType() == Material.DIAMOND_ORE
                    || event.getBlock().getType() == Material.COAL_ORE
                    || event.getBlock().getType() == Material.REDSTONE_ORE
                    || event.getBlock().getType() == Material.GLOWING_REDSTONE_ORE
                    || event.getBlock().getType() == Material.COAL_BLOCK){
                if (plugin.profiles.containsKey(uuid)){
                    PlayerProfile profile = plugin.profiles.get(uuid);
                    if (profile.triggerHistory[1] == 0){ //the first and every 10 more times you try and fail

                        //do tutorial stuff
                        event.getPlayer().sendMessage(ChatColor.RED + "TUTORIAL: Ores appear as you mine; they are not pre-generated with the world. Some ores are more common in different parts of the world (by biome), so if you can't find an ore you need, ask around and consider trading with your neighbors. Ores are also more common in the mining world, accessible through rare holes in the bedrock. See /r/civrealms wiki for more information about the mining world.");

                        profile.triggerHistory[1] = (byte)1; //eventually it will stop pestering you.
                    }
                }
            }
            //plants: explain RB
        }
    }
    
}
