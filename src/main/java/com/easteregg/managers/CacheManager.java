package com.easteregg.managers;

import com.easteregg.CreatingState;
import com.easteregg.EasterEgg;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CacheManager extends AbstractManager{


    private final Map<Player, EasterEgg.Builder> creating;
    private final Map<Player, CreatingState> creatingStateMap;
    private Map<UUID, List<EasterEgg>> claimedEggs;
    public CacheManager(ManagerHandler managerHandler) {
        super(managerHandler);

        this.creating = new HashMap<>();
        this.creatingStateMap = new HashMap<>();
        this.claimedEggs = new HashMap<>();
    }


    public Map<Player,EasterEgg.Builder> getChoosing() {
        return creating;
    }

    public Map<UUID, List<EasterEgg>> getClaimedEggs() {
        return claimedEggs;
    }


    public void setClaimedEggs(final Map<UUID,List<EasterEgg>> eggs){
        this.claimedEggs = eggs;

    }

    public Map<Player, CreatingState> getCreatingStateMap() {
        return creatingStateMap;
    }
}
