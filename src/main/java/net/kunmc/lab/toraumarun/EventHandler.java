package net.kunmc.lab.toraumarun;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventHandler implements Listener {

    /**
     * ステージからプレイヤーが落下したときの処理
     */
    @org.bukkit.event.EventHandler
    public void CheckLocation(PlayerMoveEvent event){
        if(CommandExecutor.start){
            if(event.getPlayer().getLocation().getY()<CommandExecutor.mainloc.getY()-3){
                if(GameLogic.playerList.contains(event.getPlayer())) {
                    Location tp = CommandExecutor.mainloc;
                    tp = new Location(tp.getWorld(),tp.getX()-1,tp.getY(), tp.getZ()+12);
                    event.getPlayer().teleport(tp);
                    GameLogic.playerList.remove(event.getPlayer());
                    if(GameLogic.playerList.size()==0){
                        GameLogic.last = event.getPlayer();
                    }
                }
            }
        }
    }
}
