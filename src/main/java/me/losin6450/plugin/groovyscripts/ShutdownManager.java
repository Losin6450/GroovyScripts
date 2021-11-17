package me.losin6450.plugin.groovyscripts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ShutdownManager {

    private List<Consumer<Object>> consumers;

    public ShutdownManager(){
        consumers = new ArrayList<Consumer<Object>>();
    }

    public void add(Consumer<Object> consumer){
        consumers.add(consumer);
    }

    public boolean executeShutdownHooks(){
        for(Consumer<Object> consumer : consumers){
            consumer.accept(null);
        }
        return true;
    }

    public void clear(){
        consumers.clear();
    }

}
