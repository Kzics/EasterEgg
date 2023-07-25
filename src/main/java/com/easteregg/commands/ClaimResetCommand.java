package com.easteregg.commands;

import com.easteregg.managers.ManagerHandler;
import com.easteregg.utils.ColorsUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ClaimResetCommand implements CommandExecutor {

    private final ManagerHandler managerHandler;
    public ClaimResetCommand(final ManagerHandler managerHandler){
        this.managerHandler = managerHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if(args.length != 1){
            managerHandler.getCacheManager().setClaimedEggs(new HashMap<>());
            sender.sendMessage(ColorsUtil.translate.apply("&aSuccessfully reset cooldown for ALL players"));
            return false;
        }

        final String playerName = args[0];
        final Player target = Bukkit.getPlayer(playerName);

        if(playerName == null){
            sender.sendMessage(ColorsUtil.translate.apply("&cPlayer don't exist"));
            return false;
        }

        managerHandler.getCacheManager().getClaimedEggs().keySet().forEach(key->{
            if(key.equals(target.getUniqueId())) {
                managerHandler.getCacheManager().getClaimedEggs().remove(target.getUniqueId());
                sender.sendMessage("Successfully reset cooldown for " + target.getName());
            }

        });
        return false;
    }
}
