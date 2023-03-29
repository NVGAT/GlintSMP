package com.notverygoodatthis;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCheckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(GlintSMP.spawnLocation.distance(player.getLocation()) > 20) {
                player.sendMessage(GlintSMP.glintText("This area does not count as spawn. You can kill without any repercussions."));
            } else {
                player.sendMessage(GlintSMP.glintText("This area counts as spawn. If you kill here you will be logged and possibly punished."));
            }
            return true;
        }
        return false;
    }
}
