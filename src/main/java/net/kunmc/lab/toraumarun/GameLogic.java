package net.kunmc.lab.toraumarun;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GameLogic extends JavaPlugin{

    public static List<Player> playerList = null;
    public static Player last = null;

    /**
     * ゲーム全体の処理
     * @param location  規準位置
     */
    static void startGame(Location location){
        //ゲーム開始前の処理
        Location tp = location;
        int lx = location.getBlockX(),ly = location.getBlockY(), lz = location.getBlockZ();
        tp = new Location(tp.getWorld(),tp.getX()+27,tp.getY()-2, tp.getZ()-2);

        for(int i = 3;i<=52;i++){
            for(int j = 3;j<=5;j++){
                Location fill = new Location(location.getWorld(),lx+i,ly+j-5,lz+1);
                fill.getBlock().setType(Material.WHITE_WOOL);
            }
        }
        playerList = new ArrayList<>();

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getMainScoreboard();
        Objective objective;
        if(board.getObjectives().contains(board.getObjective("run"))){
            board.getObjective("run").unregister();
        }
        objective = board.registerNewObjective("run","dummy","回数");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getGameMode()!= GameMode.SPECTATOR) {
                playerList.add(player);
            }
            player.sendMessage(ChatColor.GOLD+"ゲームを開始します！");
            player.teleport(tp);

            Score score = objective.getScore(ChatColor.GREEN + player.getName() + ":");
            score.setScore(0);
        }

        //ゲーム開始前のカウント処理
        new BukkitRunnable() {
            int c = 3;
            public void run() {
                if(c==0) {
                    this.cancel();
                }
                if(c>=1){
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.WHITE+"開始まで、"+c+"秒",null,0,15,0);
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE,0.2F,1);
                    }
                }else{
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.GOLD+"スタート！！",null,0,15,0);
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE,0.2F,1);
                    }
                }
                c--;
            }
        }.runTaskTimer(ToraumaRun.INSTANCE,200,20);

        Objective finalObjective = objective;

        //ゲーム本体の処理部
        new BukkitRunnable() {
            int c = 1;
            public void run() {
                if(!CommandExecutor.start) {
                    StageLogic.setAllBoard(location);
                    last = null ;
                    playerList = null;
                    CommandExecutor.start = false;
                    cancel();
                }else if(playerList.size()==0){
                    Score score = finalObjective.getScore(ChatColor.GREEN + last.getName() + ":");
                    int sc = score.getScore();
                    StageLogic.setAllBoard(location);
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.GOLD+("優勝者: "+last.getName()+" !!!"),ChatColor.AQUA+("スコア:"+sc+"回"),10,100,20);
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,5,1);
                    }
                    last = null ;
                    playerList = null;
                    CommandExecutor.start = false;
                    cancel();
                }else{
                    mainLogic(c, finalObjective);
                    c++;
                }
            }
        }.runTaskTimer(ToraumaRun.INSTANCE,260,270);
    }


        /**
         * アスレチックの設置、除去、音の処理
         * @param count 回数
         * @param objective "run"のobjective
         */
    static void mainLogic(int count,Objective objective){
        if (count == 10 || count == 20 || count == 30) {
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 10 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 20 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.6F);
                    }
                    if(count%2==0) {
                        PanelRemove(1);
                    }else{
                        PanelRemove(2);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 30 );
        } else {
            if (count >= 2) {
                new BukkitRunnable() {
                    public void run() {
                        if(!CommandExecutor.start||playerList.size()==0)
                            cancel();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.71F);
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                        }
                        if(count%2==0){
                            PanelRemove(1);
                        }else{
                            PanelRemove(2);
                        }
                    }
                }.runTaskLater(ToraumaRun.INSTANCE, 30 );
            } else {
                new BukkitRunnable() {
                    public void run() {
                        if(!CommandExecutor.start||playerList.size()==0)
                            cancel();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                        }
                    }
                }.runTaskLater(ToraumaRun.INSTANCE, 10 );
                new BukkitRunnable() {
                    public void run() {
                        if(!CommandExecutor.start||playerList.size()==0)
                            cancel();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                        }
                    }
                }.runTaskLater(ToraumaRun.INSTANCE, 20 );
                new BukkitRunnable() {
                    public void run() {
                        if(!CommandExecutor.start||playerList.size()==0)
                            cancel();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.6F);
                        }
                    }
                }.runTaskLater(ToraumaRun.INSTANCE, 30);
            }
        }
        if (count == 1){
            new BukkitRunnable() {
                Location location = CommandExecutor.mainloc;
                int lx = location.getBlockX(),ly = location.getBlockY(), lz = location.getBlockZ();
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for(int i = 3;i<=52;i++){
                        for(int j = 3;j<=5;j++){
                            Location fill = new Location(location.getWorld(),lx+i,ly+j-5,lz+1);
                            fill.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 60 );
        }
        if (count % 2 == 0) {
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                    }

                    if(count<=30) {
                        Integer num = Integer.valueOf(count);
                        String str = num.toString();
                        Location location = CommandExecutor.mainloc;
                        int lx = location.getBlockX(), ly = location.getBlockY(), lz = location.getBlockZ();
                        location = new Location(location.getWorld(), lx + 52, ly - 2, lz+1);
                        try {
                            WEUtil.createPlaceOperation(location, str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Random random = new Random();
                        int randomValue = random.nextInt(29);
                        Integer num = Integer.valueOf(randomValue+1);
                        String str = num.toString();
                        Location location = CommandExecutor.mainloc;
                        int lx = location.getBlockX(), ly = location.getBlockY(), lz = location.getBlockZ();
                        location = new Location(location.getWorld(), lx + 52, ly - 2, lz+1);
                        try {
                            WEUtil.createPlaceOperation(location, str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    StageLogic.setBoard(CommandExecutor.mainloc);
                    for(Player player : playerList) {
                        Score score = objective.getScore(ChatColor.GREEN + (player.getName() + ":"));
                        score.setScore(count-1);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 60 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.33F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 90 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.18F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 120 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 150 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.93F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 180 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.88F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 210 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.79F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 240 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.71F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 270 );
        } else {
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.71F);
                    }

                    if(count<=30) {
                        Integer num = Integer.valueOf(count);
                        String str = num.toString();
                        Location location = CommandExecutor.mainloc;
                        int lx = location.getBlockX(), ly = location.getBlockY(), lz = location.getBlockZ();
                        location = new Location(location.getWorld(), lx + 52, ly - 2, lz+1);
                        try {
                            WEUtil.createPlaceOperation(location, str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Random random = new Random();
                        int randomValue = random.nextInt(29);
                        Integer num = Integer.valueOf(randomValue+1);
                        String str = num.toString();
                        Location location = CommandExecutor.mainloc;
                        int lx = location.getBlockX(), ly = location.getBlockY(), lz = location.getBlockZ();
                        location = new Location(location.getWorld(), lx + 52, ly - 2, lz+1);
                        try {
                            WEUtil.createPlaceOperation(location, str);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                        StageLogic.setBoard(CommandExecutor.mainloc);
                        for (Player player : playerList) {
                            Score score = objective.getScore(ChatColor.GREEN + (player.getName() + ":"));
                            score.setScore(count - 1);
                        }

                }
            }.runTaskLater(ToraumaRun.INSTANCE, 60 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.79F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 90 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.88F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 120 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.93F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 150 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 180 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.18F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 210 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.33F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 240 );
            new BukkitRunnable() {
                public void run() {
                    if(!CommandExecutor.start||playerList.size()==0)
                        cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                    }
                }
            }.runTaskLater(ToraumaRun.INSTANCE, 270 );
        }
    }

    /**
     * パネルの除去
     */
    public static void PanelRemove(int num){
        int lx = CommandExecutor.mainloc.getBlockX(),ly = CommandExecutor.mainloc.getBlockY(), lz = CommandExecutor.mainloc.getBlockZ();
        if(num==1){
            for (int i = 3; i <= 52; i++) {
                for (int j = 3; j <= 7; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly -3, lz + j - 6);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }else{
            for (int i = 3; i <= 52; i++) {
                for (int j = 28; j <= 32; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + j - 6);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }
        for (int i = 3; i <= 52; i++) {
            for (int j = 8; j <= 27; j++) {
                for(int k = 2;k <= 12;k++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k -5, lz + j -6);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }
    }


}
