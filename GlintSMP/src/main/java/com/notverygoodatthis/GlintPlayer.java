package com.notverygoodatthis;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

enum GlintTier {
    S,
    A,
    B,
    C,
    D
}

public class GlintPlayer {
    private Player player;
    private GlintTier tier;
    //this is the worst list initialization I've ever written. only me and god himself know what I was doing here.
    private List<Enchantment> valuables = Arrays.asList(new Enchantment[]{Enchantment.DAMAGE_ALL,
            Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.DURABILITY, Enchantment.MENDING, Enchantment.ARROW_DAMAGE,
            Enchantment.DAMAGE_UNDEAD, Enchantment.DAMAGE_ARTHROPODS, Enchantment.PROTECTION_EXPLOSIONS,
            Enchantment.PROTECTION_FALL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_PROJECTILE,
            Enchantment.SWIFT_SNEAK, Enchantment.FIRE_ASPECT, Enchantment.DEPTH_STRIDER, Enchantment.DIG_SPEED,
            Enchantment.SOUL_SPEED, Enchantment.SILK_TOUCH, Enchantment.LOOT_BONUS_BLOCKS, Enchantment.LOOT_BONUS_MOBS});
    public GlintPlayer(Player player, GlintTier tier) {
        this.player = player;
        this.tier = tier;
    }

    public Player getPlayer() {
        return player;
    }

    public GlintTier getTier() {
        return tier;
    }

    public void setTier(GlintTier newTier) {
        tier = newTier;
        updateTabListName();
    }

    public ItemStack getGlintBook() {
        GlintPlayer killer = new GlintPlayer(player.getKiller(), GlintTier.valueOf(GlintSMP.getTierForPlayer(player.getKiller())));
        ItemStack item = new ItemStack(Material.AIR);
        Random rd = new Random();
        Enchantment ench;
        ench = Arrays.asList(Enchantment.values()).get(rd.nextInt(Arrays.asList(Enchantment.values()).size()));
        switch(killer.getTier()) {
            case S:
                ench = valuables.get(rd.nextInt(valuables.size()));
                item = GlintSMP.getGlintBook(ench, GlintSMP.randRange(9, 11));
                break;
            case A:
                item = GlintSMP.getGlintBook(ench, GlintSMP.randRange(7, 9));
                break;
            case B:
                item = GlintSMP.getGlintBook(ench, GlintSMP.randRange(5, 7));
                break;
            case C:
                item = GlintSMP.getGlintBook(ench, GlintSMP.randRange(3, 5));
                break;
            case D:
                item = GlintSMP.getGlintBook(ench, GlintSMP.randRange(1, 3));
                break;
        }
        return item;
    }

    public boolean shouldLevelUp(GlintTier compTier) {
        List<GlintTier> allTiers = Arrays.asList(GlintTier.values());
        Bukkit.getLogger().info(String.format("Tier of player: %d\nTier of killer: %d", allTiers.indexOf(tier), allTiers.indexOf(compTier)));
        if(allTiers.indexOf(tier) >= allTiers.indexOf(compTier)) {
            return true;
        }
        return false;
    }

    public void incrementTier() {
        List<GlintTier> allTiers = Arrays.asList(GlintTier.values());
        try {
            GlintTier plusOne = allTiers.get(allTiers.indexOf(tier) - 1);
            setTier(plusOne);
        } catch(ArrayIndexOutOfBoundsException e) {
            Bukkit.getLogger().info(String.format("Couldn't increment the tier of %s because it's already maxed", player.getName()));
        }
        updateTabListName();
    }

    public void glintBan() {
        setTier(GlintTier.B);
        String reason = GlintSMP.glintText(String.format("You've been knocked out of D tier by %s. Thank you for playing on the Glint SMP.", player.getKiller().getName()));
        Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), reason, null, "Glint SMP plugin");
        player.kickPlayer(reason);
    }

    public void decrementTier() {
        List<GlintTier> allTiers = Arrays.asList(GlintTier.values());
        try {
            GlintTier minusOne = allTiers.get(allTiers.indexOf(tier) + 1);
            setTier(minusOne);
            Bukkit.getLogger().info(String.format("Decremented the tier of %s to %s", player.getName(), minusOne));
        } catch(ArrayIndexOutOfBoundsException e) {
            Bukkit.getLogger().info(String.format("Couldn't decrement the tier of %s because it's already at its lowest", player.getName()));
            glintBan();
        }
        updateTabListName();
    }

    public void updateTabListName() {
        player.setPlayerListName(String.format("[%s] %s", tier.toString().replace("GlintTier.", ""), player.getName()));
        GlintSMP.playersTiers.remove(player.getName(), GlintSMP.getTierForPlayer(player));
        GlintSMP.playersTiers.put(player.getName(), tier.toString());
        List<String> players = new ArrayList<>(GlintSMP.playersTiers.keySet());
        List<String> tiers = new ArrayList<>(GlintSMP.playersTiers.values());
        Bukkit.getPluginManager().getPlugin("GlintSMP").getConfig().set("players", players);
        Bukkit.getPluginManager().getPlugin("GlintSMP").getConfig().set("tiers", tiers);
        Bukkit.getPluginManager().getPlugin("GlintSMP").saveConfig();
        GlintSMP.playersTiers.remove(player.getName(), GlintSMP.getTierForPlayer(player));
        GlintSMP.playersTiers.put(player.getName(), tier.toString());
    }
}
