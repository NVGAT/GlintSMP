package com.notverygoodatthis;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

//Command that players can use to apply their Glint books
public class GlintApplyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //If the sender is a player we store it in an object
        if(sender instanceof Player) {
            Player player = (Player) sender;
            //Then we check if they're holding a Glint book by checking if the material is an enchanted book and if
            //the book is named accordingly
            if(player.getInventory().getItemInOffHand().getType() == Material.ENCHANTED_BOOK &&
                    player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(GlintSMP.glintText("Glint book"))) {
                //We create an EnchantmentStorageMeta and point it to the book's ItemMeta, and pick the first enchantment in there.
                //This works because glint books only have one enchantment. We also store the level.
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) player.getInventory().getItemInOffHand().getItemMeta();
                Enchantment ench = meta.getStoredEnchants().keySet().iterator().next();
                int level = meta.getStoredEnchants().values().iterator().next();
                //If the enchantment can in fact go through...
                if(ench.canEnchantItem(player.getInventory().getItemInMainHand())) {
                    //We enchant the player's item, notify them and take away one glint book.
                    player.getInventory().getItemInMainHand().addUnsafeEnchantment(ench, level);
                    player.sendMessage(String.format(GlintSMP.glintText("Applied %s %d to your %s"), ench.getKey(), level, player.getInventory().getItemInMainHand().getType()));
                    player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                } else {
                    //But if it can't, we notify the player and don't take away anything.
                    player.sendMessage(String.format(GlintSMP.glintText("Couldn't apply %s %d to your %s"), ench.getKey(), level, player.getInventory().getItemInMainHand().getType()));
                }
                return true;
            }
        }
        return false;
    }
}
