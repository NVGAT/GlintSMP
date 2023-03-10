package com.notverygoodatthis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GlintSMP extends JavaPlugin implements Listener {
    //list of players on cooldown from dropping books
    List<String> cooldownPlayers = new ArrayList<>();
    @Override
    public void onEnable() {
        //Registering the event listener and the commands
        Bukkit.getPluginManager().registerEvents(this, this);
        registerCommands();
    }

    public static ItemStack getGlintBook(Enchantment ench, int level) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
        meta.addStoredEnchant(ench, level, true);
        meta.setDisplayName(glintText("Glint book"));
        book.setItemMeta(meta);
        return book;
    }

    //Used to make text golden so I don't have to look up "minecraft color codes" every time
    public static String glintText(String text)  {
        return String.format("§6%s", text);
    }

    //Player death event, the most important one on the SMP
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        //If they got killed by a player, and they're not on cooldown...
        if(e.getEntity().getKiller() instanceof Player && !cooldownPlayers.contains(e.getEntity().getName())) {
            //We store the player in an object
            Player player = e.getEntity();
            if(!player.getKiller().getName().equals(player.getName())) {
                //Then we store every enchantment in a list, which helps us choose a random one
                List<Enchantment> enchList = new ArrayList<>(Arrays.asList(Enchantment.values()));
                //We pick a random enchantment and log it
                Random rd = new Random();
                Enchantment ench = enchList.get(rd.nextInt(enchList.size()));
                getLogger().info(String.format("Enchantment picked: %s", ench.getKey()));
                //After all of that, we drop the book at the player's location and add them to the cooldown list
                player.getWorld().dropItemNaturally(player.getLocation(), getGlintBook(ench, rd.nextInt(10)));
                cooldownPlayers.add(player.getName());
                //After all of that, we assign a task to let the player get out of cooldown after 10 minutes
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        cooldownPlayers.remove(player.getName());
                    }
                }, 20L * 600);
            } else {
                getLogger().info(String.format("%s just tried killing themselves for a free book. what a bozo", player.getName()));
            }
        }
    }

    //Method that registers all the commands using the correct CommandExecutors
    void registerCommands() {
        getCommand("glintapply").setExecutor(new GlintApplyCommand());
        getCommand("glintbook").setExecutor(new GlintBookCommand());
    }
}