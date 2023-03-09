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

//Command that admins can use to get a hold of any enchanted book
public class GlintBookCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //If the command sender is a player...
        if(sender instanceof Player) {
            //Then we store it in a Player object
            Player player = (Player) sender;
            //Then we convert the first method argument into an Enchantment object and the second argument into an integer
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[0]));
            int level = Integer.parseInt(args[1]);
            //Then we create the book and the EnchantmentStorageMeta for it
            ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
            //We set the display name to the glint book and add the enchantment. After that we set the book's item meta
            meta.setDisplayName(GlintSMP.glintText("Glint book"));
            meta.addStoredEnchant(ench, level, true);
            book.setItemMeta(meta);
            //Finally, we add the book into the player's inventory and notify then that we've given them the requested book
            player.getInventory().addItem(book);
            player.sendMessage(GlintSMP.glintText(String.format("Gave you a book with %s %d", ench.getKey(), level)));
            return true;
        }
        return false;
    }
}
