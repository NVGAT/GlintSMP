package com.notverygoodatthis;

import dev.dbassett.skullcreator.SkullCreator;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GlintSMP extends JavaPlugin implements Listener {
    public static Location spawnLocation;
    List<String> cooldownPlayers = new ArrayList<>();
    public static HashMap<String, String> playersTiers = new HashMap<>();

    @Override
    public void onEnable() {
        //Registering the event listener and the commands
        Bukkit.getPluginManager().registerEvents(this, this);
        registerCommands();
        Bukkit.addRecipe(revival());
        Bukkit.addRecipe(tierUp());

        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if(getConfig().isSet("spawn-location")) {
                    List<Double> spawnCords = (List<Double>) getConfig().getList("spawn-location");
                    spawnLocation = new Location(Bukkit.getWorld("world"), spawnCords.get(0), spawnCords.get(1), spawnCords.get(2));
                } else {
                    getLogger().info("Spawn not set in the config! You can set it manually or through the /spawnset command");
                }

                List<String> playerNames = (List<String>) getConfig().getList("players");
                List<String> playerTiers = (List<String>) getConfig().getList("tiers");

                for(String p : playerNames) {
                    String tier = playerTiers.get(playerNames.indexOf(p));
                    playersTiers.put(p, tier);
                    getLogger().info(String.format("%s is tier %s", p, tier));
                }
            }
        }, 20L);
    }

    public static String getTierForPlayer(Player p) {
        return playersTiers.get(p.getName());
    }


    public static ItemStack getGlintBook(Enchantment ench, int level) {
        //Getter for glint books
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

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(!playersTiers.containsKey(e.getPlayer())) {
            playersTiers.put(e.getPlayer().getName(), "B");
        }
        GlintPlayer p = new GlintPlayer(e.getPlayer(), GlintTier.valueOf(getTierForPlayer(e.getPlayer())));
        p.updateTabListName();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(glintText("Tier-up"))) {
                GlintPlayer p = new GlintPlayer(e.getPlayer(), GlintTier.valueOf(getTierForPlayer(e.getPlayer())));
                p.incrementTier();
                p.getPlayer().getInventory().getItemInMainHand().setAmount(p.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            }
        }
    }

    //Player death event, the most important one on the SMP
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if(e.getEntity().getKiller() instanceof Player && getConfig().isSet("spawn-location")) {
            Player p = e.getEntity();
            Location l = p.getLocation();
            if(spawnLocation.distance(l) < 20) {
                getLogger().info(String.format("%s killed %s at spawn!", p.getKiller().getName(), p.getName()));
            }
        }
        //If they got killed by a player, and they're not on cooldown...
        if(e.getEntity().getKiller() instanceof Player && !cooldownPlayers.contains(e.getEntity().getName())) {
            //We store the player and the killer in a GlintPlayer
            GlintPlayer player = new GlintPlayer(e.getEntity(), GlintTier.valueOf(getTierForPlayer(e.getEntity())));
            GlintPlayer killer = new GlintPlayer(e.getEntity().getKiller(), GlintTier.valueOf(getTierForPlayer(e.getEntity().getKiller())));
            //We drop the appropriate book
            player.getPlayer().getWorld().dropItemNaturally(player.getPlayer().getLocation(), player.getGlintBook());
            if(killer.shouldLevelUp(player.getTier())) {
                //And if the killer should level up, we increment the killer's tier and decrement the player's tier
                killer.incrementTier();
                player.decrementTier();
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        //Buffed creeper drop rates
        if(e.getEntity() instanceof Creeper) {
            e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), new ItemStack(Material.GUNPOWDER, 5));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            //Shield break sounds
            Player attacker = (Player) e.getDamager();
            Player attacked = (Player) e.getEntity();
            if(attacked.isBlocking() && attacker.getInventory().getItemInMainHand().getType().name().contains("_axe")) {
                attacker.getWorld().playSound(attacker.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1);
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
        getCommand("glinttier").setExecutor(new GlintTierCommand());
        getCommand("glintrevive").setExecutor(new GlintReviveCommand());
    }

    ShapedRecipe revival() {
        NamespacedKey key = new NamespacedKey(this, "player_head");
        ItemStack head = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjYyNjUxODc5ZDg3MDQ5OWRhNTBlMzQwMzY4MDBkZGZmZDUyZjNlNGUxOTkzYzVmYzBmYzgyNWQwMzQ0NmQ4YiJ9fX0=");
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(glintText("Revival head"));
        head.setItemMeta(meta);
        ShapedRecipe rec = new ShapedRecipe(key, head);
        rec.shape("RTR", "TET", "RTR");
        rec.setIngredient('R', Material.RECOVERY_COMPASS);
        rec.setIngredient('T', Material.TOTEM_OF_UNDYING);
        rec.setIngredient('E', Material.ELYTRA);
        return rec;
    }

    ShapedRecipe tierUp() {
        NamespacedKey key = new NamespacedKey(this, "tier_up");
        ItemStack head = SkullCreator.itemFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzY1MDViMWJlZmJhMjQyMTcwYTQ2ZTg5NDdiNTJhZWE1NGE1OTA2MGYzZTFjMzZmMjFjZWJiNDQ2OTBmOGIwYyJ9fX0=");
        ItemMeta meta = head.getItemMeta();
        meta.setDisplayName(glintText("Tier-up"));
        head.setItemMeta(meta);
        ShapedRecipe rec = new ShapedRecipe(key, head);
        rec.shape("DTD", "TNT", "DTD");
        rec.setIngredient('D', Material.DIAMOND_BLOCK);
        rec.setIngredient('T', Material.TOTEM_OF_UNDYING);
        rec.setIngredient('N', Material.TOTEM_OF_UNDYING);
        return rec;
    }
}