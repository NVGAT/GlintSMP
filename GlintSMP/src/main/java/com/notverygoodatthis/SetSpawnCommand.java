package com.notverygoodatthis;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetSpawnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Location l = player.getLocation();
            List<Double> cords = new ArrayList<>();
            cords.add(l.getX());
            cords.add(l.getY());
            cords.add(l.getZ());
            Bukkit.getPluginManager().getPlugin("GlintSMP").getConfig().set("spawn-location", cords);
            Bukkit.getPluginManager().getPlugin("GlintSMP").saveConfig();
            return true;
        } else {
            double[] cords = {Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2])};
            Bukkit.getLogger().info(String.format("%d %d %d", cords[0], cords[1], cords[2]));
            Bukkit.getPluginManager().getPlugin("GlintSMP").getConfig().set("spawn-location", Arrays.asList(cords));
            sender.sendMessage(GlintSMP.glintText(String.format("Set the spawn coordinates to %d %d %d", cords[0], cords[1], cords[2])));
            Bukkit.getPluginManager().getPlugin("GlintSMP").saveConfig();
            return true;
        }
    }
}
