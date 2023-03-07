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

public class GlintApplyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.getInventory().getItemInOffHand().getType() == Material.ENCHANTED_BOOK &&
                    player.getInventory().getItemInOffHand().getItemMeta().getDisplayName().equals(GlintSMP.glintText("Glint book"))) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) player.getInventory().getItemInOffHand().getItemMeta();
                ItemStack currentItem = player.getInventory().getItemInMainHand();
                for(Map.Entry<Enchantment, Integer> entry : meta.getStoredEnchants().entrySet()) {
                    if(entry.getKey().canEnchantItem(currentItem)) {
                        currentItem.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                    } else {
                        player.sendMessage(GlintSMP.glintText(String.format("Couldn't apply %s %d to your %s",
                                entry.getKey().getKey(), entry.getValue(), currentItem.getType().name())));
                    }
                }
                player.getInventory().getItemInOffHand().setAmount(player.getInventory().getItemInOffHand().getAmount() - 1);
                player.sendMessage(GlintSMP.glintText("Finished applying enchantments. If something didn't work, contact an admin."));
                return true;
            }
        }
        return false;
    }
}
