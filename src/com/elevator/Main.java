package com.elevator;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	
	HashMap<String, Long> cooldown = new HashMap<String, Long>();
	
	@Override
	public void onEnable(){
		getLogger().info("Elevators Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable(){
		getLogger().info("Elevators DISABLED");
	}
	
	@EventHandler
	public void playerMove(PlayerMoveEvent event){
		if(event.getFrom().getBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_BLOCK){
			if(event.getTo().getY() > event.getFrom().getY()){
				for(int i = 0; i < 15; i++){
					Location searchPos = event.getFrom().add(0, 1, 0);
					if(searchPos.getBlock().getType() == Material.IRON_BLOCK){
						teleport(searchPos, event.getPlayer());
						event.setCancelled(true);
						break;
					}
				}
			}
		}
	}
	
	@EventHandler
	public void playerSneak(PlayerToggleSneakEvent event){
		Player player = event.getPlayer();
		if(player.isSneaking()){
			if(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_BLOCK){
				Location location = player.getLocation();
				location.add(0, -1, 0);
				for(int i = 0; i < 16; i++){
					location.add(0, -1, 0);
					if(location.getBlock().getType() == Material.IRON_BLOCK){
						teleport(location, player);
						break;
					}
				}
			}
		}
	}
	
	public void teleport(Location location, Player player){
		boolean check = cooldownCheck(player);
		if(check == true){
			if((location.add(0, 1, 0).getBlock().getType() == Material.AIR) && (location.add(0, 1, 0).getBlock().getType() == Material.AIR)){
				player.teleport(location.add(0, -1, 0));
				player.playSound(location, Sound.ORB_PICKUP, 1, -2);
			}
		}
		else if(check == false){
			//player.sendMessage(ChatColor.GREEN + " Not so fast!");
		}
		else{
			player.sendMessage(ChatColor.GREEN + "Path is obstructed, please make sure path is clear.");
		}
	}
	
	public boolean cooldownCheck(Player player){
		if((!cooldown.containsKey(player.getName())) || ((System.currentTimeMillis() - cooldown.get(player.getName())) > 150)){
			cooldown.put(player.getName(), System.currentTimeMillis());
			return true;
		}
		else{
			return false;
		}
	}
}