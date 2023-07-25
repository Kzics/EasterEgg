package com.easteregg.managers;

import com.easteregg.EasterEgg;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class EasterEggsManager extends AbstractManager{


    private LinkedHashSet<EasterEgg> easterEggs;
    public EasterEggsManager(ManagerHandler managerHandler) {
        super(managerHandler);

        this.easterEggs = new LinkedHashSet<>();
    }

    public Set<EasterEgg> getEasterEggs() {
        return easterEggs;
    }


    public boolean addEgg(final EasterEgg egg){
        return easterEggs.add(egg);
    }
    public Optional<EasterEgg> getEggByLocation(final Location location){
        return easterEggs
                .stream()
                .filter(egg->egg.getLocation().equals(location))
                .findFirst();
    }

    public Optional<EasterEgg> getEggById(final String id){
        return easterEggs
                .stream()
                .filter(egg->egg.getId().equals(id))
                .findFirst();
    }

    public void resetEggs(){
        this.easterEggs.forEach(egg->egg.getEggItem().setType(Material.AIR));
        this.easterEggs = new LinkedHashSet<>();
    }

    public boolean removeEgg(final EasterEgg egg){
        return easterEggs.remove(egg);
    }
}
