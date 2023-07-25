package com.easteregg.events.listeners;

import com.easteregg.CreatingState;
import com.easteregg.EasterEgg;
import com.easteregg.EasterMain;
import com.easteregg.events.AbstractListeners;
import com.easteregg.gui.EggsGUI;
import com.easteregg.managers.ManagerHandler;
import com.easteregg.utils.ColorsUtil;
import com.easteregg.utils.FunctionReflections;
import com.easteregg.utils.ReflectionsUtils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.persistence.Embedded;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerInteractListener extends AbstractListeners {

    protected final ManagerHandler managerHandler;
    public PlayerInteractListener(final ManagerHandler managerHandler){
        this.managerHandler = managerHandler;
    }

    @Override
    protected void onInteract(PlayerInteractEvent e) {
        final Player player = e.getPlayer();
        final boolean hasItem = e.hasItem();
        final Block block = e.getClickedBlock();

        if(hasItem) return;
        if(e.getClickedBlock() == null) return;

        if(managerHandler.getCacheManager().getChoosing().containsKey(player) && !managerHandler.getCacheManager().getCreatingStateMap().containsKey(player)){
            EasterEgg.Builder eggBuilder = managerHandler.getCacheManager().getChoosing().get(player);
            final Block upperBlock = block.getRelative(BlockFace.UP);

            eggBuilder.setLocation(upperBlock.getLocation());
            managerHandler.getCacheManager().getCreatingStateMap().put(player, CreatingState.CHOOSING_TEXTURE);
            player.sendMessage(ColorsUtil.translate.apply("&aChoose egg texture"));
            return;
        }

        if(!block.hasMetadata("easterEgg")) return;

        if(!managerHandler.getCacheManager().getClaimedEggs().containsKey(player.getUniqueId())) {
            managerHandler.getCacheManager().getClaimedEggs().put(player.getUniqueId(),new ArrayList<>());
        }

        List<EasterEgg> eggList = managerHandler.getCacheManager().getClaimedEggs().get(player.getUniqueId());
        Optional<EasterEgg> eggOptional = managerHandler.getEasterEggsManager().getEggByLocation(e.getClickedBlock().getLocation());

        if(eggOptional.isPresent()){

            final EasterEgg easterEgg = eggOptional.get();

            if(managerHandler.getCooldownManager().hasCooldown(player.getUniqueId())){
                final String claimCooldownMessage = managerHandler.getEasterMain().getConfig().getString("claim-cooldown-message");
                player.playSound(player.getLocation(), Sound.NOTE_BASS,5f,5f);
                player.sendMessage(ColorsUtil.translate.apply(claimCooldownMessage));
                return;
            }

            if(!eggList.contains(easterEgg)){
                final EasterMain easterMain = managerHandler.getEasterMain();
                eggList.add(easterEgg);
                final String claimedMessage = managerHandler.getEasterMain().getConfig().getString("claimed-message");
                final int claimedEggs = managerHandler.getCacheManager().getClaimedEggs().get(player.getUniqueId()).size();
                final int maxEggs = managerHandler.getEasterEggsManager().getEasterEggs().size();
                player.sendMessage(ColorsUtil.translate.apply(claimedMessage
                        .replace("{current}",String.valueOf(claimedEggs))
                        .replace("{max}",String.valueOf(maxEggs))));

                player.playSound(player.getLocation(), Sound.LEVEL_UP,5f,5f);

                managerHandler.getCooldownManager().addCooldown(player.getUniqueId());
                if(easterMain.getConfig().getBoolean("execute-command")){
                    final String command = easterMain.getConfig().getString("command");
                    if(command == null) return;
                    easterMain.getServer().dispatchCommand(easterMain.getServer().getConsoleSender(),command);
                }else{
                    if(easterMain.getConfig().getStringList("give-items") == null) return;
                    List<String> materialsString = easterMain.getConfig().getStringList("give-items");
                    materialsString.stream().map(Material::getMaterial)
                            .forEach(mat->player.getInventory().addItem(new ItemStack(mat)));
                }
            }else{
                final String alreadyClaimedMessage = managerHandler.getEasterMain().getConfig().getString("already-claimed-message");
                player.playSound(player.getLocation(), Sound.NOTE_BASS,5f,5f);

                player.sendMessage(ColorsUtil.translate.apply(alreadyClaimedMessage));
            }
        }
    }

    @Override
    protected void onBreak(BlockBreakEvent e) {
        final Player player = e.getPlayer();

        if(e.getBlock() == null) return;
        final Block block = e.getBlock();

        if(block.hasMetadata("easterEgg")){
            e.setCancelled(true);

            if(player.isOp()){
                player.sendMessage(ColorsUtil.translate.apply("&cYou have to delete it from /eggs command"));
            }
        }
    }

    @Override
    protected void onClick(InventoryClickEvent e) {

        if(!e.getClickedInventory().getTitle().equals("Eggs list")) return;
        if(e.getClickedInventory() == null) return;

        final Player player = (Player) e.getWhoClicked();
        e.setCancelled(true);
        final Object nbtItem = ReflectionsUtils.getNMSItem(e.getCurrentItem());

        if(FunctionReflections.hasTag.apply(nbtItem)){
            final Object itemTag = FunctionReflections.getTag.apply(nbtItem);
            if(itemTag == null) return;

            if(FunctionReflections.hasKey.apply("addEgg",itemTag)){
                managerHandler.getCacheManager().getChoosing().put(player,new EasterEgg.Builder());
                player.sendMessage(ColorsUtil.translate.apply("&aChoose the egg location"));
                player.closeInventory();
            }else if(FunctionReflections.hasKey.apply("placedEggs",itemTag)){
                if(e.isLeftClick()){
                    final String id = FunctionReflections.getString.apply("placedEggs",itemTag);
                    final Optional<EasterEgg> eggOptional = managerHandler.getEasterEggsManager().getEggById(id);

                    eggOptional.ifPresent(egg -> player.teleport(egg.getLocation()));
                }else{
                    final String id = FunctionReflections.getString.apply("placedEggs",itemTag);
                    final Optional<EasterEgg> eggOptional = managerHandler.getEasterEggsManager().getEggById(id);
                    eggOptional.ifPresent(egg ->{
                        managerHandler.getEasterEggsManager().removeEgg(egg);
                        managerHandler.getCacheManager().getClaimedEggs()
                                .values()
                                .forEach(v->v.removeIf(vb->vb.equals(egg)));

                        egg.getEggItem().setType(Material.AIR);
                    });

                    new EggsGUI(managerHandler).openInv(player);
                }
            }
        }
    }
}
