package me.losin6450.plugin.groovyscripts;

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.RegisteredListener;

import java.util.*;
import java.util.function.Consumer;

public class EventManager {

    private Map<Class<? extends Event>, List<Consumer<Event>>> listeners;

    public EventManager(){
        listeners = new HashMap<Class<? extends Event>, List<Consumer<Event>>>();
    }
    public void addListener(Class<? extends Event> event, Consumer<Event> consumer){
        if(listeners.containsKey(event)){
            listeners.get(event).add(consumer);
        } else {
            CustomListener listener = new CustomListener(event);
            Bukkit.getPluginManager().registerEvent(event, listener, EventPriority.NORMAL, listener, GroovyScripts.getInstance());
            listeners.put(event, Arrays.asList(consumer));
        }

    }

    public void removeListeners(){
        List<RegisteredListener> handlers = HandlerList.getRegisteredListeners(GroovyScripts.getInstance());
        for(RegisteredListener rl : handlers){
            if(rl.getListener() instanceof CustomListener){
                HandlerList.unregisterAll(rl.getListener());
            }
        }
        listeners.clear();
    }

    public Map<Class<? extends Event>, List<Consumer<Event>>> getListeners(){
        return listeners;
    }

    private class CustomListener implements EventExecutor, Listener {

        private Class eventclass;

        public CustomListener(Class<? extends Event> ec){
            eventclass = ec;
        }
        @Override
        public void execute(Listener listener, Event event) throws EventException {

            if(listeners.containsKey(event.getClass())){
                for(Consumer<Event> function : listeners.get(event.getClass())){
                    function.accept(event);
                }
            }
        }
    }
}
