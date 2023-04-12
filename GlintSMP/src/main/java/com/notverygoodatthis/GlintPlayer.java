package com.notverygoodatthis;

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
        ItemStack item = new ItemStack(Material.AIR);
        Random rd = new Random();
        Enchantment ench = Arrays.asList(Enchantment.values()).get(rd.nextInt(Arrays.asList(Enchantment.values()).size()));
        switch(tier) {
            case S:
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

    public void decrementTier() {
        List<GlintTier> allTiers = Arrays.asList(GlintTier.values());
        try {
            GlintTier minusOne = allTiers.get(allTiers.indexOf(tier) + 1);
            setTier(minusOne);
        } catch(ArrayIndexOutOfBoundsException e) {
            Bukkit.getLogger().info(String.format("Couldn't decrement the tier of %s because it's already at its lowest", player.getName()));
        }
        updateTabListName();
    }

    public void updateTabListName() {
        player.setPlayerListName(String.format("[%s] %s", tier.toString().replace("GlintTier.", ""), player.getName()));
        Plugin glintPlugin = Bukkit.getPluginManager().getPlugin("GlintSMP");
        List<String> players = new ArrayList<>(GlintSMP.playersTiers.keySet());
        List<String> tiers = new ArrayList<>(GlintSMP.playersTiers.values());
        glintPlugin.getConfig().set("players", players);
        glintPlugin.getConfig().set("tiers", tiers);
        glintPlugin.saveConfig();
        GlintSMP.playersTiers.remove(player.getName(), GlintSMP.getTierForPlayer(player));
        GlintSMP.playersTiers.put(player.getName(), tier.toString());
    }
}
