package me.losin6450.plugin.groovyscripts;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import me.losin6450.plugin.groovyscripts.commands.GroovyScriptsCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class GroovyScripts extends JavaPlugin {

    private File scriptsfolder;
    private GroovyScriptEngine engine;
    private static GroovyScripts INSTANCE;
    private List<Thread> threads;
    private EventManager eventmanager;
    private CommandManager commandManager;
    private ShutdownManager shutdownManager;

    @Override
    public void onEnable() {
        shutdownManager = new ShutdownManager();
        commandManager = new CommandManager();
        eventmanager = new EventManager();
        threads = new ArrayList<Thread>();
        INSTANCE = this;
        scriptsfolder = new File(getDataFolder() + File.separator + "scripts/");
        if(!scriptsfolder.exists()){
            scriptsfolder.mkdirs();
        }
        getCommand("groovyscripts").setExecutor(new GroovyScriptsCommand());
        loadScripts(getServer().getConsoleSender());


    }

    @Override
    public void onDisable() {
        Thread shutdowns = new Thread(() -> {boolean wait = shutdownManager.executeShutdownHooks();});
        shutdowns.start();
        try {
            shutdowns.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void loadScripts(CommandSender sender){
        Thread shutdowns = new Thread(() -> {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aExecuting all consumers"));
            boolean wait = shutdownManager.executeShutdownHooks();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aExecuted all consumers ( " + wait + " )"));
        });
        shutdowns.start();
        try {
            shutdowns.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        shutdownManager.clear();
        if(!threads.isEmpty()){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoving current scripts..."));
            for(Thread thr : threads){
                thr.interrupt();
            }
            threads.clear();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved current scripts"));
        }
        try {
            List<URL> urls = new ArrayList<URL>();
            for(File file : scriptsfolder.listFiles()){
                if(!file.getName().startsWith("-")){
                    urls.add(file.toURI().toURL());
                }
            }
            engine = new GroovyScriptEngine(urls.toArray(URL[]::new), getClassLoader());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoving listeners..."));
        eventmanager.removeListeners();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved listeners"));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoving commands..."));
        CommandManager.unregisterCommands();
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved commands"));
        for(File file : scriptsfolder.listFiles()){
            if(!file.getName().startsWith("-")){

                Binding bindings = new Binding();
                bindings.setProperty("GroovyScripts", this);
                bindings.setProperty("CURRENTSCRIPT", file);
                Thread thread = new Thread(() -> {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aLoading script " + file.getName()));
                        int errors = 0;
                        try {
                            engine.run(file.getName(), bindings);
                        } catch (ResourceException e) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEncountered an error while loading script " + file.getName() + " ( " + e.getMessage() + " )"));
                            errors += 1;
                        } catch (ScriptException e) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEncountered an error while loading script " + file.getName() + " ( " + e.getMessage() + " )"));
                            errors += 1;
                        }
                        if(errors == 0) {
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aLoaded script " + file.getName()));
                        }


                    }
                );
                try {
                    thread.start();
                    threads.add(thread);
                } catch (Exception e){
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cEncountered an error while loading script " + file.getName() + " ( " + e.getMessage() + " )"));
                }

            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + file.getName() + " was disabled remove - from the start of the name to enable it"));
            }
        }
    }

    public static GroovyScripts getInstance(){
        return INSTANCE;
    }

    public EventManager getEventManager(){
        return eventmanager;
    }

    public CommandManager getCommandManager() {return commandManager;}

    public ShutdownManager getShutdownManager() {return shutdownManager;}
}
