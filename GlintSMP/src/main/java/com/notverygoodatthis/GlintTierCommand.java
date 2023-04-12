package com.notverygoodatthis;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GlintTierCommand implements CommandExecutor {
    //Command to manually set a player's tier
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        //We store the mentioned player in a Player object
        Player p = Bukkit.getPlayerExact(strings[0]);
        try {
            //And we try to convert it into a GlintPlayer, and we try to convert the second argument to a GlintTier
            GlintPlayer glintPlayer = new GlintPlayer(p, GlintTier.valueOf(GlintSMP.getTierForPlayer(p)));
            GlintTier newTier = GlintTier.valueOf(strings[1]);
            //Then we try to set the GlintPlayer's tier.
            glintPlayer.setTier(newTier);
        } catch(NullPointerException e) {
            commandSender.sendMessage(GlintSMP.glintText("Invalid tier or player"));
            Bukkit.getLogger().info(String.format("%s failed to set the tier of %s to %s", commandSender.getName(), p.getName(), GlintTier.valueOf(strings[1])));
        }
        return false;
    }
}
