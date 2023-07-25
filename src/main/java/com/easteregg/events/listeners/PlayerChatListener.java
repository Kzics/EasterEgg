package com.easteregg.events.listeners;

import com.easteregg.CreatingState;
import com.easteregg.EasterEgg;
import com.easteregg.events.AbstractListeners;
import com.easteregg.managers.ManagerHandler;
import com.easteregg.utils.ColorsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener extends AbstractListeners {


    protected final ManagerHandler managerHandler;
    public PlayerChatListener(final ManagerHandler managerHandler){
        this.managerHandler = managerHandler;
    }

    @Override
    protected void onChat(AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        if(!managerHandler.getCacheManager().getCreatingStateMap().containsKey(player)) return;

        final CreatingState currentState = managerHandler.getCacheManager().getCreatingStateMap().get(player);
        final EasterEgg.Builder eggBuilder = managerHandler.getCacheManager().getChoosing().get(player);

        switch (currentState){
            case CHOOSING_TEXTURE:
                e.setCancelled(true);

                eggBuilder.setTexture(e.getMessage());
                player.sendMessage(ColorsUtil.translate.apply("&aChoose egg id"));
                managerHandler.getCacheManager().getCreatingStateMap().put(player,CreatingState.CHOOSING_ID);
                break;
            case CHOOSING_ID:
                e.setCancelled(true);

                eggBuilder.setId(e.getMessage());

                Bukkit.getScheduler().runTask(managerHandler.getEasterMain(), () -> {
                    final EasterEgg egg = eggBuilder.build();

                    managerHandler.getEasterEggsManager().addEgg(egg);
                    player.sendMessage(ColorsUtil.translate.apply("&aSuccessfully created an easter egg!"));
                    managerHandler.getCacheManager().getCreatingStateMap().remove(player);
                    managerHandler.getCacheManager().getChoosing().remove(player);
                });
                break;
        }

    }
}
