package com.easteregg;

import com.easteregg.storage.EasterEggsData;
import com.easteregg.utils.serializer.LocationSerializer;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.metadata.FixedMetadataValue;

import java.lang.reflect.Field;
import java.util.UUID;

public class EasterEgg {

    private final Block eggBlock;
    private final Location location;
    private final String id;
    private final String texture;


    public EasterEgg(final Location location,final String url,final String id){
        this.eggBlock = getSkullBlock(url,location);
        this.location = location;
        this.id = id;
        this.texture = url;
    }

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public Block getEggItem() {
        return eggBlock;
    }

    public String getTexture() {
        return texture;
    }

    private Block getSkullBlock(String url, Location location) {
        Block block = location.getBlock();
        block.setMetadata("easterEgg",new FixedMetadataValue(EasterMain.getInstance(),getId()));
        block.setType(Material.SKULL);

        BlockState state = block.getState();
        if (!(state instanceof Skull)) return block;

        Skull skull = (Skull) state;
        skull.setSkullType(SkullType.PLAYER);

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}","http://textures.minecraft.net/texture/"+url).getBytes());

        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        try {
            Field profileField = skull.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skull, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        skull.setRawData((byte) 1);

        skull.update();

        return block;
    }

    public EasterEggsData toData(){
        return new EasterEggsData(getTexture(), LocationSerializer.serializeLocation(getLocation()),getId());
    }

    public static class Builder {


        private String url;
        private String id;
        private Location location;

        public Builder setLocation(Location location){
            this.location = location;

            return this;
        }

        public Builder setTexture(String url){
            this.url = url;

            return this;
        }

        public Builder setId(String id){
            this.id = id;

            return this;
        }

        public EasterEgg build(){
            return new EasterEgg(location,url,id);
        }

    }
}

