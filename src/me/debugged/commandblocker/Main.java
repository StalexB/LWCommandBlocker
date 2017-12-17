package me.debugged.commandblocker;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        FileConfiguration configuration = getConfig();

        boolean blockColonCommands = configuration.getBoolean("block_colon_commands");
        List<String> blockedCommands = configuration.getStringList("blocked_commands");
        String message = configuration.getString("block_message");
        String bypassPermission = configuration.getString("block_bypass_permission");

        getServer().getPluginManager().registerEvents(new Listener() {

            @EventHandler(priority = EventPriority.HIGHEST)
            public void onCommand(PlayerCommandPreprocessEvent e) {
                Player player = e.getPlayer();
                String command = e.getMessage().split(" ")[0].replace("/", "");

                if(player.hasPermission(bypassPermission)) return;

                if(blockColonCommands && command.contains(":")) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    return;
                }

                for(String cmd : blockedCommands) {
                    if(cmd.equalsIgnoreCase(command)) {
                        e.setCancelled(true);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        return;
                    }
                }
            }

        }, this);
    }

}
