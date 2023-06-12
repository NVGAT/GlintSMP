package com.notverygoodatthis;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RevivalWave implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player p = (Player) commandSender;
            ItemStack itemInHand = p.getInventory().getItemInMainHand();
            if(itemInHand.getType() == Material.PLAYER_HEAD && itemInHand.getItemMeta().getDisplayName().equals("§6R E V I V A L    W A V E")) {
                for(OfflinePlayer rev : Bukkit.getServer().getBannedPlayers()) {
                    Bukkit.getBanList(BanList.Type.NAME).pardon(rev.getName());
                    GlintSMP.playersTiers.remove(rev.getName(), GlintSMP.getTierForPlayer(rev.getName()));
                    GlintSMP.playersTiers.put(rev.getName(), "B");
                    p.sendMessage(GlintSMP.glintText(String.format("Revived %s", rev.getName())));
                }
                List<String> players = new ArrayList<>(GlintSMP.playersTiers.keySet());
                List<String> tiers = new ArrayList<>(GlintSMP.playersTiers.values());
                Bukkit.getPluginManager().getPlugin("GlintSMP").getConfig().set("players", players);
                Bukkit.getPluginManager().getPlugin("GlintSMP").getConfig().set("tiers", tiers);
                Bukkit.getPluginManager().getPlugin("GlintSMP").saveConfig();
                p.sendMessage(GlintSMP.glintText("Revival wave finished. Let's hope you know what you're doing..."));
                p.getInventory().getItemInMainHand().setAmount(0);
                return true;
            }
        }
        return false;
    }
}
