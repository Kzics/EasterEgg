package com.easteregg.commands;

import com.easteregg.managers.ManagerHandler;
import com.easteregg.utils.ColorsUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;

public class EggResetCommand implements CommandExecutor {


    private final ManagerHandler managerHandler;
    public EggResetCommand(final ManagerHandler managerHandler){
        this.managerHandler = managerHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        managerHandler.getEasterEggsManager().resetEggs();

        managerHandler.getCacheManager().setClaimedEggs(new HashMap<>());
        sender.sendMessage(ColorsUtil.translate.apply("&aSucessfully reset!"));
        return false;
    }
}
