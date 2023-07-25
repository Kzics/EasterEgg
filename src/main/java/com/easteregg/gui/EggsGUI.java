package com.easteregg.gui;

import com.easteregg.EasterEgg;
import com.easteregg.managers.ManagerHandler;
import com.easteregg.utils.ColorsUtil;
import com.easteregg.utils.ItemUtils;
import com.easteregg.utils.ReflectionsUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class EggsGUI extends AbstractInventoryGUI {

    private final Inventory inventory;
    private final ManagerHandler managerHandler;

    public EggsGUI(ManagerHandler managerHandler) {
        super(managerHandler.getEasterMain());
        this.managerHandler = managerHandler;

        this.inventory = managerHandler.getEasterMain().getServer().createInventory(null,54,"Eggs list");
    }


    public void openInv(final Player player){
        Set<EasterEgg> eggs = getManagerHandler().getEasterEggsManager().getEasterEggs();

        eggs.forEach(egg->{
            ItemStack itemStack = getSkull(egg.getTexture());
            ItemMeta meta = itemStack.getItemMeta();;

            meta.setDisplayName(ColorsUtil.translate.apply("&a" + egg.getId()));
            meta.setLore(Arrays.asList("",ColorsUtil.translate.apply("&e&lTeleport &7(Left-Click)"),
                    ColorsUtil.translate.apply("&e&lDelete egg &7(Right-Click)")));

            itemStack.setItemMeta(meta);

            itemStack = ReflectionsUtils.getUpdatedNmsStack(itemStack,"placedEggs",egg.getId());
            inventory.addItem(itemStack);
        });

        ItemStack itemStack = new ItemUtils(Material.CHEST,1)
                .setNBTTag(Collections.singletonMap("addEgg",ReflectionsUtils.getNMSInstanceBis.apply("NBTTagString","add")))
                .setDisplayName(ColorsUtil.translate.apply("&aAdd egg"))
                .build();
        inventory.addItem(itemStack);

        player.openInventory(inventory);
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }

    @Override
    public InventoryHolder getInventoryHolder() {
        return inventory.getHolder();
    }

    public static ItemStack getSkull(String url) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if(url.isEmpty())return head;
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();


        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}","http://textures.minecraft.net/texture/" + url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        Field idField = null;
        try
        {
            profileField = headMeta.getClass().getDeclaredField("profile");
            idField = profile.getClass().getDeclaredField("id");

            idField.setAccessible(true);
            profileField.setAccessible(true);

            Arrays.stream(profile.getClass().getDeclaredFields())
                    .forEach(System.out::println);

            idField.set(profile,UUID.randomUUID());
            profileField.set(headMeta, profile);

        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        head.setItemMeta(headMeta);
        return head;
    }

}
