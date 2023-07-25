package com.easteregg.commands;

import com.easteregg.gui.EggsGUI;
import com.easteregg.managers.ManagerHandler;
import com.easteregg.utils.ColorsUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EggCommand implements CommandExecutor {

    private final ManagerHandler managerHandler;
    public EggCommand(final ManagerHandler managerHandler){
        this.managerHandler = managerHandler;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if(!(sender instanceof Player))return false;
        final Player player = (Player) sender;

        if(!(player.isOp())){
            player.sendMessage(ColorsUtil.translate.apply("No permissions"));
            return false;
        }

        if(args.length == 1){
            final String action = args[0];
            if(action.equals("reload")){
                managerHandler.getEasterMain().reloadConfig();
                player.sendMessage(ColorsUtil.translate.apply("&aReloaded config file ! "));
            }else{
                player.sendMessage(ColorsUtil.translate.apply("&cCorrect Usage /eggs reload"));
            }
            return true;
        }

        new EggsGUI(managerHandler).openInv(player);

        return false;
    }
}
