/*
 * This project is licensed under GPLv3
 * https://www.gnu.org/licenses/gpl-3.0.html
 */
package com.civrealms.civrealmstutorial;

/**
 *
 * @author crimeo
 */

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class CivRealmsTutorialListener implements Listener {
    
    private CivRealmsTutorial plugin;
    public static Logger LOG = Logger.getLogger("CivRealmsPVE");
    public HashMap<String,PlayerProfile> profiles = new HashMap<String,PlayerProfile>(); 
    
    public CivRealmsTutorialListener(CivRealmsTutorial plugin) {
        this.plugin = plugin;
    }
    
    //listen for login and add profile to map.
    
    private PlayerProfile findProfile(String uuid){
        if (profiles.containsKey(uuid)){
            return profiles.get(uuid);
        } else {
            PlayerProfile profile = new PlayerProfile();
            profiles.put(uuid, profile);
            return profiles.get(uuid);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event){
        String uuid = event.getPlayer().getUniqueId().toString();
        if (event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2){
            Material handMat = event.getPlayer().getInventory().getItemInMainHand().getType();
            if (handMat != Material.STONE_AXE
                    && handMat != Material.IRON_AXE
                    && handMat != Material.GOLD_AXE
                    && handMat != Material.DIAMOND_AXE){ //fail to chop a log due to wrong tool (not due to citadel, etc.)
                PlayerProfile profile = findProfile(uuid);
                if (profile.triggerHistory[0] < 3){ //up to 3 tutorial warnings (only happens when you don't use an axe)
                    event.getPlayer().sendMessage(ChatColor.AQUA + "TUTORIAL:" + ChatColor.BLUE + " You cannot break trees by hand. Make a primitive axe instead by using the regular minecraft axe recipe but with either bone or flint for the head of the axe. All animals drop bones.");
                    event.getPlayer().sendMessage("");
                    event.getPlayer().sendMessage(ChatColor.BLUE + "You can get sticks for your tools by punching leaves. There are similar recipes for all other primitive tools.");
                    profile.triggerHistory[0] = (byte)Math.min(3,profile.triggerHistory[0] + 1); //eventually it will stop pestering you.
                }
            }
        } else if (event.getBlock().getType() == Material.STONE //explain stone breaking inefficiency
                    || event.getBlock().getType() == Material.COBBLESTONE){
            Material handMat = event.getPlayer().getInventory().getItemInMainHand().getType();
            if (findProfile(uuid).triggerHistory[1] == 0 && (handMat == Material.STONE_PICKAXE
                    || handMat == Material.IRON_PICKAXE
                    || handMat == Material.GOLD_PICKAXE)){
                event.getPlayer().sendMessage(ChatColor.AQUA + "TUTORIAL:" + ChatColor.BLUE + " Primitive picks have a % chance to fail to break stone. Stone will break nearby rock into cobble, which is easier to break. Higher tier metal picks greatly improve mining speed, and mithril (diamond) picks have 100% success rates.");
                findProfile(uuid).triggerHistory[1] = (byte)1; //eventually it will stop pestering you.
            }
        } else if (event.getBlock().getType() == Material.IRON_ORE //Ores: explain hiddenore, etc.
                    || event.getBlock().getType() == Material.GOLD_ORE
                    || event.getBlock().getType() == Material.LAPIS_ORE
                    || event.getBlock().getType() == Material.EMERALD_ORE
                    || event.getBlock().getType() == Material.DIAMOND_ORE
                    || event.getBlock().getType() == Material.COAL_ORE
                    || event.getBlock().getType() == Material.REDSTONE_ORE
                    || event.getBlock().getType() == Material.GLOWING_REDSTONE_ORE
                    || event.getBlock().getType() == Material.COAL_BLOCK){
            //PlayerProfile profile = findProfile(uuid);
            LOG.info("[TUTDEBUG] TH[1] = " + (findProfile(uuid).toString()));
            if (findProfile(uuid).triggerHistory[2] == 0){
                event.getPlayer().sendMessage(ChatColor.AQUA + "TUTORIAL:" + ChatColor.BLUE + " Ores appear as you mine; they are not pre-generated with the world. Some ores are more common in different parts of the world (by biome). Ores are also more common in the mining world, accessible through rare holes in the bedrock.");
                findProfile(uuid).triggerHistory[2] = (byte)1; //eventually it will stop pestering you.
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void cobbleCraft(InventoryClickEvent event) {
		if (event.getInventory().getType() == InventoryType.CRAFTING
				|| event.getInventory().getType() == InventoryType.WORKBENCH) {
			if (event.getSlotType() == InventoryType.SlotType.CRAFTING) {
                if (event.getInventory().getItem(event.getSlot()).getType() == Material.COBBLESTONE){ //trying to craft with cobblestone. Tell about tools and ovens.
                    String uuid = event.getWhoClicked().getUniqueId().toString();
                    PlayerProfile profile = findProfile(uuid);
                    if (profile.triggerHistory[3] == 0){ //up to 3 tutorial warnings (only happens when you don't use an axe)
                        event.getWhoClicked().sendMessage(ChatColor.AQUA + "TUTORIAL:" + ChatColor.BLUE + " Ovens are made out of hardened clay (terracotta) instead of cobble. Primitive tools are made out of flint or bone instead of cobble.");
                        profile.triggerHistory[3] = (byte)1; //eventually it will stop pestering you.
                    }
                }
            }
        }
    }
    
}
