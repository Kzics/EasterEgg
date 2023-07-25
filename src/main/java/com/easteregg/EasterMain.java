package com.easteregg;

import com.easteregg.commands.ClaimResetCommand;
import com.easteregg.commands.EggCommand;
import com.easteregg.commands.EggResetCommand;
import com.easteregg.managers.ManagerHandler;
import com.easteregg.storage.EggsStorage;
import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class EasterMain extends JavaPlugin {

    private ManagerHandler managerHandler;
    private static EasterMain easterMain;
    @Override
    public void onEnable() {
        easterMain = this;
        this.managerHandler = new ManagerHandler(this);
        getCommand("eggs").setExecutor(new EggCommand(managerHandler));
        getCommand("eggreset").setExecutor(new EggResetCommand(managerHandler));
        getCommand("claimreset").setExecutor(new ClaimResetCommand(managerHandler));
        try {
            FileUtils.copyInputStreamToFile(getResource("config.yml"),new File(getDataFolder(),"config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        EggsStorage.loadEggs(managerHandler);
    }

    @Override
    public void onDisable() {
        EggsStorage.saveEggs(managerHandler);
    }

    public static EasterMain getInstance(){
        return easterMain;
    }
}
