package com.easteregg.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ItemUtils {

    private Material mat;

    private int amount;

    private ItemStack item;

    private String displayName;

    private HashMap<Enchantment, Integer> enchantmentList = new HashMap<>();

    private Map<String, Object> nbtTag;

    private List<String> loreList = new ArrayList<>();

    private ItemFlag flag;

    private short durability;

    public ItemUtils(Material mat, int amount) {
        this.mat = mat;
        this.amount = amount;
        this.item = new ItemStack(mat, amount);
    }
    public ItemUtils(ItemStack item, int amount) {
        this.mat = item.getType();
        this.amount = amount;
        this.item = item;
    }


    public ItemUtils setDisplayName(String displayName) {
        this.displayName = ColorsUtil.translate.apply(displayName);
        return this;
    }

    public ItemUtils addEnchants(Enchantment enchantment, int level) {
        enchantmentList.put(enchantment, level);
        return this;
    }

    public ItemUtils addLores(String... lore) {

        this.loreList.addAll(Arrays.asList(lore));
        this.loreList.replaceAll(l -> ColorsUtil.translate.apply(l));
        return this;
    }

    public ItemUtils addLores(List<String> lorelist) {
        lorelist.forEach(l->lorelist.set(lorelist.indexOf(l), ColorsUtil.translate.apply(l)));
        this.loreList = lorelist;
        return this;
    }

    public ItemUtils addFlag(ItemFlag flag) {
        this.flag = flag;
        return this;
    }

    public ItemUtils setDurability(short durability) {
        this.durability = durability;

        return this;
    }
    public ItemUtils setNBTTag(Map<String, Object> nbtTag) {
        this.nbtTag = nbtTag;
        return this;
    }
    public ItemUtils addNBTTag(String key,Object value){
        final Map<String, Object> tempTag = new HashMap<>();
        tempTag.putAll(nbtTag);
        tempTag.put(key,value);

        this.nbtTag = tempTag;

        return this;
    }



    public ItemStack build() {
        if (nbtTag != null) {
            Object nmsItem = ReflectionsUtils.getNMSItem(item);
            Object compound = null;

            try {
                compound = ReflectionsUtils.getNMSClass("NBTTagCompound", "server").newInstance();
                final Class<?> nbtBaseClass = ReflectionsUtils.getNMSClass("NBTBase","server");
                for (Map.Entry<String, Object> entry : nbtTag.entrySet()) {
                    compound.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "set": "a", String.class, nbtBaseClass)
                            .invoke(compound, entry.getKey(), nbtBaseClass.cast(entry.getValue()));
                }
                nmsItem.getClass().getMethod(ReflectionsUtils.getVersionNum() < 17 ? "setTag": "c",compound.getClass()).invoke(nmsItem,compound);
                item = (ItemStack) ReflectionsUtils.getNMSItem(nmsItem,"asCraftMirror");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(displayName);
        if (!loreList.isEmpty()) {
            meta.setLore(loreList);
        }
        if (!enchantmentList.isEmpty()) {
            Set<Enchantment> enchantments = enchantmentList.keySet();

            enchantments.forEach(enchant -> meta.addEnchant(enchant, enchantmentList.get(enchant), true));
        }

        if (flag != null) {
            meta.addItemFlags(flag);
        }
        if (durability != -1) {
            item.setDurability(durability);
        }

        item.setItemMeta(meta);

        return item;

    }
}