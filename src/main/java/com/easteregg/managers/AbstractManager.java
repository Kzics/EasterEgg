package com.easteregg.managers;

public abstract class AbstractManager {

    private final ManagerHandler managerHandler;
    public AbstractManager(final ManagerHandler managerHandler){
        this.managerHandler = managerHandler;
    }

    public ManagerHandler getManagerHandler() {
        return managerHandler;
    }
}
