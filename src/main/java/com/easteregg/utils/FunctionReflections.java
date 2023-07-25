package com.easteregg.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

public class FunctionReflections {

    public static Function<Object,Boolean> hasTag = (item)->{
        try {
            return (Boolean) item.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "hasTag": "t").invoke(item);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    };
    public static Function<Object,Object> getTag = (item)->{
        try {
            return item.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "getTag": "u").invoke(item);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };
    public static BiFunction<String,Object,Boolean> hasKey = (str, tag)-> {
        try {
            return (Boolean) tag.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "hasKey": "e", String.class).invoke(tag, str);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    };

    public static BiFunction<String,Object,String> getString = (str,tag)->{
        try {
            return (String) tag.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "getString": "l",String.class).invoke(tag,str);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    };

    public static BiFunction<String,Object,Integer> getInt = (str,tag)->{
        try {
            return (Integer) tag.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "getInt": "h",String.class).invoke(tag,str);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    };


    public static BiFunction<String,Object,Object> getNBTList = (str,tag)->{
        try {
            return tag.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "getList": "c",String.class,int.class).invoke(tag,str,8);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };

    public static BiFunction<Integer,Object,String> getValueFromNBTList = (n,tag)->{
        try {
            return (String) tag.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "getString": "j",int.class).invoke(tag,n);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };

    public static Function<Object, Method> getAddMethod = (tag) -> {
        final int versionNum = ReflectionsUtils.getVersionNum();
        final Class<?> nbtBase = ReflectionsUtils.getNMSClass("NBTBase","server");
        try {
            if(versionNum < 17){
                return tag.getClass().getMethod("add",nbtBase);
            }else {
                return tag.getClass().getMethod("b", int.class, nbtBase);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    };

    public static BiFunction<String,Object,Object> getNMSInstance = (str,obj)->{
        try {
            final String version = ReflectionsUtils.getVersion();

            if(Integer.parseInt(version.split("_")[1]) < 17){
                return ReflectionsUtils.getNMSClass(str,"server").getConstructors()[0].newInstance(obj);
            }else{
                final Constructor<?> constructor = ReflectionsUtils.getNMSClass(str,"server").getDeclaredConstructors()[0];
                constructor.setAccessible(true);

                return constructor.newInstance(obj);
            }        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };

    public static BiFunction<String,Object,Object> getNMSInstanceBis = (str,obj)->{
        try {
            final String version = ReflectionsUtils.getVersion();

            if(Integer.parseInt(version.split("_")[1]) < 17){
                return ReflectionsUtils.getNMSClass(str,"server").getConstructors()[1].newInstance(obj);

            }else{
                final Constructor<?> constructor = ReflectionsUtils.getNMSClass(str,"server").getDeclaredConstructors()[0];
                constructor.setAccessible(true);

                return constructor.newInstance(obj);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    };

}