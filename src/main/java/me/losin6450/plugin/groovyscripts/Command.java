package me.losin6450.plugin.groovyscripts;


import org.bukkit.command.CommandSender;

import java.util.List;

public class Command extends org.bukkit.command.Command {

    public Command(String name, String description, String usageMessage, List<String> aliases){
        super(name, description, usageMessage, aliases);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }
}
