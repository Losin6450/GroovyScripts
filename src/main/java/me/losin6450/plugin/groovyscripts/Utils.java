package me.losin6450.plugin.groovyscripts;


import java.lang.reflect.Field;

public abstract class Utils {


    public static Object getField(Object obj, String f){
        try {
            Field field = obj.getClass().getDeclaredField(f);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
