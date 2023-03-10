package com.notverygoodatthis;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

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
            //Finally, we add the book into the player's inventory and notify then that we've given them the requested book
            player.getInventory().addItem(GlintSMP.getGlintBook(ench, level));
            player.sendMessage(GlintSMP.glintText(String.format("Gave you a book with %s %d", ench.getKey(), level)));
            return true;
        }
        return false;
    }
}
