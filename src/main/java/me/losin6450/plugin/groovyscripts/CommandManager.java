package me.losin6450.plugin.groovyscripts;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class CommandManager {

    public void registerCommand(me.losin6450.plugin.groovyscripts.Command command){
        SimpleCommandMap commandMap = getCommandMap();
        commandMap.register(((Plugin) GroovyScripts.getInstance()).getName(), command);
    }
    public static SimpleCommandMap getCommandMap(){
        return (SimpleCommandMap) Utils.getField(Bukkit.getServer(), "commandMap");
    }

    public static void unregisterCommands(){
        Map<String, Command> known = (Map<String, Command>) Utils.getField(getCommandMap(), "knownCommands");
        known.values().removeIf(cmd -> cmd instanceof me.losin6450.plugin.groovyscripts.Command);

    }
}
