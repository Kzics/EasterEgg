package com.easteregg.utils;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.function.BiFunction;

public class ReflectionsUtils {




    public static BiFunction<String,Object,Object> getNMSInstanceBis = (str, obj)->{
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

    public static Object getPacket(final String packetName, Object... args) {
        final String serverVersion = getVersion();
        try {
            final Class<?> packetClass = Class.forName("net.minecraft.server." + serverVersion + "." + packetName);
            final Class<?>[] argClasses = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                argClasses[i] = args[i].getClass();
            }
            final Constructor<?> packetConstructor;
            if(packetName.equals("PacketPlayOutEntityTeleport")){
                packetConstructor = packetClass.getDeclaredConstructor(ReflectionsUtils.getNMSClass("Entity","server"));
            }else{
                packetConstructor = packetClass.getDeclaredConstructor(argClasses);
            }
            return packetConstructor.newInstance(args);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getPacket(final String packetName, Map<Class<?>,Object> args) {
        final String serverVersion = getVersion();
        try {
            final Class<?> packetClass = Class.forName("net.minecraft.server." + serverVersion + "." + packetName);

            Constructor<?> packetConstructor = null;
            Object value = null;

            for (Map.Entry<Class<?>,Object> entry : args.entrySet()) {
                packetConstructor = packetClass.getDeclaredConstructor(entry.getKey());
                value = entry.getValue();
            }

            return packetConstructor.newInstance(value);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Constructor<?> getPacketConstructor(final String packetName, Class<?>... args) {
        final String serverVersion = getVersion();
        try {
            final Class<?> packetClass = Class.forName("net.minecraft.server." + serverVersion + "." + packetName);


            if (packetName.equals("PacketPlayOutEntityTeleport")) {
                return packetClass.getDeclaredConstructor(ReflectionsUtils.getNMSClass("Entity","server"));
            } else {
                return packetClass.getDeclaredConstructor(args);
            }
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }



    public static Object getEnumValue(String enumValueName) {
        try {
            // Obtention de la classe EnumPlayerInfoAction
            Class<?> enumClass = Class.forName("net.minecraft.server."+getVersion()+".PacketPlayOutPlayerInfo$EnumPlayerInfoAction");

            // Appel de la méthode valueOf pour obtenir la valeur de l'enum à partir de son nom
            Object enumValue = Enum.valueOf((Class<Enum>) enumClass, enumValueName);

            return enumValue;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMSClass(final String className,final String type){
        final String version = getVersion();
        Class<?> clazz;
        try {
            if(type.equals("server")) {
                if(Integer.parseInt(version.split("_")[1]) < 17) {
                    clazz = Class.forName("net.minecraft.server." + version + "." + className);
                }else{
                    if(className.startsWith("Packet")) {
                        clazz = Class.forName("net.minecraft.network.protocol.game." + className);
                    }else if(className.equals("EntityZombie")) {
                        clazz = Class.forName("net.minecraft.world.entity.monster." + className);
                    } else if (className.equals("Entity")) {
                        clazz = Class.forName("net.minecraft.world.entity." + className);
                    } else if (className.equals("EntityPlayer") || className.equals("EntityHuman")) {
                        clazz = Class.forName("net.minecraft.server.level." + className);
                    } else if(className.startsWith("Pathfinder")) {
                        clazz = Class.forName("net.minecraft.world.entity.ai.goal."+ className);
                    } else if (className.startsWith("PlayerInteract")) {
                        clazz = Class.forName("net.minecraft.server.level."+ className);
                    } else{
                        clazz = Class.forName("net.minecraft.nbt." + className);

                    }

                }
            }else if(type.equals("network")) {
                clazz = Class.forName("net.minecraft.network.chat." + className);
            } else{
                clazz = Class.forName("org.bukkit.craftbukkit." + version + "." + className);
            }

            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

    public static Object getNMSItem(ItemStack it){
        final Class<?> craftItemStack = ReflectionsUtils.getNMSClass("inventory.CraftItemStack","bukkit");

        final Object nmsItem;
        try {
            nmsItem = craftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class)
                    .invoke(craftItemStack,it);


        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return nmsItem;
    }
    public static Object getNMSItem(Object it,String methodName){
        final Class<?> craftItemStack = ReflectionsUtils.getNMSClass("inventory.CraftItemStack","bukkit");

        final Object nmsItem;
        try {
            nmsItem = craftItemStack.getMethod(methodName, it.getClass())
                    .invoke(craftItemStack,it);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return nmsItem;
    }

    public static Object getValue(Object obj, String methodName, Object... arguments) {
        Object value;
        try {
            Class<?>[] argumentTypes = new Class<?>[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                argumentTypes[i] = arguments[i].getClass();
            }
            value = obj.getClass().getMethod(methodName, argumentTypes).invoke(obj, arguments);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return value;
    }

    public static String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }

    public static org.bukkit.inventory.ItemStack getUpdatedNmsStack(org.bukkit.inventory.ItemStack farmItem, String key, String value){

        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(farmItem);
        NBTTagCompound tag = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        tag.set(key,new NBTTagString(value));
        nmsItem.setTag(tag);

        farmItem.getItemMeta().spigot().setUnbreakable(true);

        return CraftItemStack.asCraftMirror(nmsItem);
    }

    public static int getVersionNum(){
        return Integer.parseInt(getVersion().split("_")[1]);
    }
}

