package com.easteregg.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager extends AbstractManager {


    private final Map<UUID,Long> lastClaim;
    public CooldownManager(ManagerHandler managerHandler) {
        super(managerHandler);
        this.lastClaim = new HashMap<>();
    }




    public boolean hasCooldown(UUID playerUUID){
        if(lastClaim.get(playerUUID) == null) return false;

        boolean hasCooldown = System.currentTimeMillis() < lastClaim.get(playerUUID);

        if(hasCooldown){
            return true;
        }else{
            lastClaim.remove(playerUUID);
            return false;
        }
    }


    public void addCooldown(UUID playerUUID){
        long cooldown = getManagerHandler().getEasterMain().getConfig().getInt("claim-cooldown")*1000L;
        lastClaim.put(playerUUID,System.currentTimeMillis() + cooldown);
    }



}
