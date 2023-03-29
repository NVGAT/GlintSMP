package com.notverygoodatthis;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GlintSMP extends JavaPlugin implements Listener {
    public static Location spawnLocation;
    List<String> cooldownPlayers = new ArrayList<>();

    @Override
    public void onEnable() {
        //Registering the event listener and the commands
        Bukkit.getPluginManager().registerEvents(this, this);
        registerCommands();

        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if(getConfig().isSet("spawn-location")) {
                    List<Double> spawnCords = (List<Double>) getConfig().getList("spawn-location");
                    spawnLocation = new Location(Bukkit.getWorld("world"), spawnCords.get(0), spawnCords.get(1), spawnCords.get(2));
                } else {
                    getLogger().info("Spawn not set in the config! You can set it manually or through the /spawnset command");
                }
            }
        }, 20L);
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

    public static int randRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    //Player death event, the most important one on the SMP
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player) {
            Player p = e.getEntity();
            Location l = p.getLocation();
            if(spawnLocation.distance(l) > 20) {
                getLogger().info(String.format("%s died at %d %d %d in %s", p.getName(), l.getX(), l.getY(), l.getZ(), l.getWorld().getName()));
            } else {
                getLogger().info(String.format("%s died at spawn to %s", p.getName(), p.getKiller().getName()));
            }
        }
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
                player.getWorld().dropItemNaturally(player.getLocation(), getGlintBook(ench, randRange(1, 10)));
                cooldownPlayers.add(player.getName());
                //After all of that, we assign a task to let the player get out of cooldown after 10 minutes
                Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                    @Override
                    public void run() {
                        cooldownPlayers.remove(player.getName());
                    }
                }, 20L * 600);
                if(spawnLocation.distance(player.getLocation()) < 20) {

                }
            } else {
                getLogger().info(String.format("%s just tried killing themselves for a free book. what a bozo", player.getName()));
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if(e.getEntity() instanceof Creeper) {
            e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.GUNPOWDER, 5));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player attacker = (Player) e.getDamager();
            Player attacked = (Player) e.getEntity();
            getLogger().info(String.format("%s has hit %s", attacker.getName(), attacked.getName()));
            if(attacked.isBlocking() && attacker.getInventory().getItemInMainHand().getType().name().contains("_axe")) {
                attacker.getWorld().playSound(attacker.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
            }
            if(attacked.getLocation().distance(attacked.getLocation()) < 20) {
                getLogger().info(String.format("%s has attacked %s outside of spawn.", attacker.getName(), attacked.getName()));
            }
        }
    }

    //Method that registers all the commands using the correct CommandExecutors
    void registerCommands() {
        getCommand("glintapply").setExecutor(new GlintApplyCommand());
        getCommand("glintbook").setExecutor(new GlintBookCommand());
        getCommand("glintrandom").setExecutor(new RandGlintBook());
        getCommand("setspawn").setExecutor(new SetSpawnCommand());
        getCommand("spawncheck").setExecutor(new SpawnCheckCommand());
    }
}