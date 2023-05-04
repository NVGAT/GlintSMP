package com.notverygoodatthis;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GlintReviveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            ItemStack held = player.getInventory().getItemInMainHand();
            if(held.getItemMeta().getDisplayName().equals(GlintSMP.glintText("Revival head"))) {
                if(Bukkit.getBanList(BanList.Type.NAME).isBanned(strings[0])) {
                    Bukkit.getBanList(BanList.Type.NAME).pardon(strings[0]);
                    player.sendMessage(GlintSMP.glintText(String.format("Successfully revived %s.", strings[0])));
                    player.getInventory().getItemInMainHand().setAmount(held.getAmount() - 1);
                } else {
                    player.sendMessage(GlintSMP.glintText("Couldn't revive. Did you make a typo in the name?"));
                }
            }
        } else {
            if(Bukkit.getBanList(BanList.Type.NAME).isBanned(strings[0])) {
                Bukkit.getBanList(BanList.Type.NAME).pardon(strings[0]);
                commandSender.sendMessage(GlintSMP.glintText(String.format("Successfully revived %s.", strings[0])));
            } else {
                commandSender.sendMessage(GlintSMP.glintText("Couldn't revive. Did you make a typo in the name?"));
            }
        }
        return false;
    }
}
