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
    List<String> cooldownPlayers = new ArrayList<>();
    @Override
    public void onEnable() {
        getLogger().info("test");
        Bukkit.getPluginManager().registerEvents(this, this);
        registerCommands();
    }

    public static String glintText(String text)  {
        return String.format("§6%s", text);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player && !cooldownPlayers.contains(e.getEntity().getName())) {
            Player player = e.getEntity();
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
            List<Enchantment> enchList = new ArrayList<>(Arrays.asList(Enchantment.values()));
            Random rd = new Random();
            Enchantment ench = enchList.get(rd.nextInt(enchList.size()));
            getLogger().info(String.format("Enchantment picked: %s", ench.toString()));
            meta.addStoredEnchant(ench, rd.nextInt(10), true);
            meta.setDisplayName(glintText("Glint book"));
            book.setItemMeta(meta);
            player.getWorld().dropItemNaturally(player.getLocation(), book);
            cooldownPlayers.add(player.getName());
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    cooldownPlayers.remove(player.getName());
                }
            }, 20L * 600);
        }
    }

    void registerCommands() {
        getCommand("glintapply").setExecutor(new GlintApplyCommand());
        getCommand("glintbook").setExecutor(new GlintBookCommand());
    }
}