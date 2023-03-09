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
                //We create an EnchantmentStorageMeta and point it to the book's ItemMeta.
                //We also store the currently held item in an ItemStack object
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) player.getInventory().getItemInOffHand().getItemMeta();
                ItemStack currentItem = player.getInventory().getItemInMainHand();
                //We loop through every enchantment in the EnchantmentStorageMeta
                for(Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
                    //If the enchantment is supported on the currently held item we apply it.
                    if(entry.getKey().canEnchantItem(currentItem)) {
                        currentItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                    } else {
                        //If it's not we notify the player that the enchantment isn't supported.
                        player.sendMessage(GlintSMP.glintText(String.format("Couldn't apply %s %d to your %s",
                                entry.getKey().getKey(), entry.getValue(), currentItem.getType().name())));
                    }
                }
                //After the loop, we take away the book in the offhand and tell the player that the enchantments have
                //been applied.
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                player.sendMessage(GlintSMP.glintText("Finished applying enchantments. If something didn't work, contact an admin."));
                return true;
            }
        }
        return false;
    }
}
