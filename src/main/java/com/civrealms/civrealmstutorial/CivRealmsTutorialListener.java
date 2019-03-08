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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
    
    //Method to use for basic, one-off triggers. If doing something fancy like "5 THEN go quiet" or "every 10th time" just do inline instead.
    private void basicTutorialMessage(int tutorialIndex, Player player, String mainText){
        String uuid = player.getUniqueId().toString();
        PlayerProfile profile = findProfile(uuid);
        if (profile.triggerHistory[tutorialIndex] == 0){ 
            player.sendMessage(ChatColor.AQUA + "TUTORIAL:" + ChatColor.BLUE + mainText);
            profile.triggerHistory[tutorialIndex] = 1; 
        }
    }
    
    //--------------- TRIGGERS: -------------------------------
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event){
        String uuid = event.getPlayer().getUniqueId().toString();
        PlayerProfile profile = findProfile(uuid);
        if (event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2){
            Material handMat = event.getPlayer().getInventory().getItemInMainHand().getType();
            if (handMat != Material.STONE_AXE
                    && handMat != Material.IRON_AXE
                    && handMat != Material.GOLD_AXE
                    && handMat != Material.DIAMOND_AXE){ //fail to chop a log due to wrong tool (not due to citadel, etc.)
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
            if (profile.triggerHistory[1] == 0 && (handMat == Material.STONE_PICKAXE
                    || handMat == Material.IRON_PICKAXE
                    || handMat == Material.GOLD_PICKAXE)){
                event.getPlayer().sendMessage(ChatColor.AQUA + "TUTORIAL:" + ChatColor.BLUE + " Primitive picks have a % chance to fail to break stone. Stone will break nearby rock into cobble, which is easier to break. Higher tier metal picks greatly improve mining speed, and mithril (diamond) picks have 100% success rates.");
                profile.triggerHistory[1] = (byte)1;
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
            if (profile.triggerHistory[2] == 0){
                event.getPlayer().sendMessage(ChatColor.AQUA + "TUTORIAL:" + ChatColor.BLUE + " Ores appear as you mine; they are not pre-generated with the world. Some ores are more common in different parts of the world (by biome). Ores are also more common in the mining world, accessible through rare holes in the bedrock.");
                profile.triggerHistory[2] = (byte)1; 
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void cobbleCraft(InventoryClickEvent event) {
		if (event.getInventory().getType() == InventoryType.CRAFTING
				|| event.getInventory().getType() == InventoryType.WORKBENCH) {
			if (event.getSlotType() == InventoryType.SlotType.CRAFTING) {
                if (event.getInventory().getItem(event.getSlot()).getType() == Material.COBBLESTONE){ //trying to craft with cobblestone. Tell about tools and ovens.
                    basicTutorialMessage (3, (Player)event.getWhoClicked(), " Ovens are made out of hardened clay (terracotta) instead of cobble. Primitive tools are made out of flint or bone instead of cobble.");
                }
            }
        }
    }
        
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void enterWater (PlayerMoveEvent event) {
        if (Math.random() < 0.01){ //uuids and map lookups, etc. every move event is a lot and unnecessary here.
            if (event.getPlayer().getLocation().getBlock().getType() == Material.STATIONARY_WATER){
                basicTutorialMessage (4, event.getPlayer(), " Careful! You can drown in water if you carry more than a few full stacks of items in deep water and are too far out to crawl to shore along the bottom.");
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void placeBlock (BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.CHEST){
            basicTutorialMessage (5, event.getPlayer(), " Some blocks like chests and crafting benches are disguised as stone until a player walks within a few blocks of them. You can use this to hide chests from x-ray. You may also want to 'lock' chests or any other block to make them harder (not impossible) to break by using the plugin Citadel. See reddit.com/r/Civrealms/wiki/protecting_property for more info.");
        }
    }
    
}
