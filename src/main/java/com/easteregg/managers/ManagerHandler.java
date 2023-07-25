package com.easteregg.managers;

import com.easteregg.EasterMain;
import com.easteregg.events.AbstractListeners;
import com.easteregg.events.EventsHandler;
import com.easteregg.events.listeners.PlayerChatListener;
import com.easteregg.events.listeners.PlayerInteractListener;
import com.easteregg.gui.EggsGUI;

import java.util.HashSet;
import java.util.Set;

public class ManagerHandler {



    private final Set<AbstractListeners> abstractListeners = new HashSet<>();

    private final EasterEggsManager easterEggsManager;
    private final EasterMain easterMain;
    private final CacheManager cacheManager;
    private final CooldownManager cooldownManager;

    public ManagerHandler(final EasterMain easterMain){
        this.easterMain = easterMain;

        this.cooldownManager = new CooldownManager(this);
        this.cacheManager = new CacheManager(this);
        this.easterEggsManager = new EasterEggsManager(this);
        new EventsHandler(this);
        AbstractListeners.addListener(new PlayerChatListener(this),this);
        AbstractListeners.addListener(new PlayerInteractListener(this),this);
    }

    public Set<AbstractListeners> getAbstractListeners() {
        return abstractListeners;
    }

    public EasterEggsManager getEasterEggsManager() {
        return easterEggsManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public EasterMain getEasterMain() {
        return easterMain;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}
