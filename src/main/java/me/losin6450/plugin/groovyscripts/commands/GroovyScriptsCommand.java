package me.losin6450.plugin.groovyscripts.commands;

import me.losin6450.plugin.groovyscripts.GroovyScripts;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GroovyScriptsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("load")){
                GroovyScripts.getInstance().loadScripts(sender);
            } else {
                sendHelp(sender);
                return true;
            }
        } else {
            sendHelp(sender);
            return true;

        }
        return true;
    }

    public void sendHelp(CommandSender sender){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&2GroovyScripts\n&7&l-----------------------------------------"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e/groovyscripts load"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&l-----------------------------------------\n&2GroovyScripts"));
    }
}
