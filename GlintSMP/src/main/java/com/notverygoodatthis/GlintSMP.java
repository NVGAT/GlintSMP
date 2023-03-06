package com.notverygoodatthis;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class GlintSMP extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("test");
        Bukkit.getPluginManager().registerEvents(this, this);
    }
}