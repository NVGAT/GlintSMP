package com.notverygoodatthis;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class GlintBookCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[0]));
            int level = Integer.parseInt(args[1]);
            Bukkit.getLogger().info(String.format("Matched enchantment to %s\nMatched level to %d", ench.getKey(), level));
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
            meta.setDisplayName(GlintSMP.glintText("Glint book"));
            meta.addStoredEnchant(ench, level, true);
            book.setItemMeta(meta);
            player.getInventory().addItem(book);
            player.sendMessage(GlintSMP.glintText(String.format("Gave you a book with %s %d", ench.getKey(), level)));
            return true;
        }
        return false;
    }
}
