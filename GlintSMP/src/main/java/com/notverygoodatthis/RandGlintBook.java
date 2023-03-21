package com.notverygoodatthis;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandGlintBook implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<Enchantment> enchList = Arrays.asList(Enchantment.values());
            Random rd = new Random();
            Enchantment randEnch = enchList.get(rd.nextInt(enchList.size()));
            int randNum = GlintSMP.randRange(1, 10);
            player.getInventory().addItem(GlintSMP.getGlintBook(randEnch, randNum));
            player.sendMessage(GlintSMP.glintText(String.format("Gave you a book with %s %d", randEnch.getKey(), randNum)));
            return true;
        }
        return false;
    }
}
