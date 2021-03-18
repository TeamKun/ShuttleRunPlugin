package net.kunmc.lab.toraumarun;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.*;

public final class ToraumaRun extends JavaPlugin implements Listener {

    static private boolean stage = false; static boolean start = false;
    static private Location location;
    static private List<Player> playerList = new ArrayList<>();
    static private List<Player> playerListAll = new ArrayList<>();
    static private int c ;
    static private Random rand = new Random();
    static private BukkitTask task1,task2,task3,task4,task5,task6;
    static private Player Last;

    @Override
    public void onEnable() {
        this.getCommand("torauma").setExecutor(this);
        this.getCommand("torauma").setTabCompleter(new TabComp());
        Bukkit.getPluginManager().registerEvents(this,this);
        getServer().getLogger().info(ChatColor.AQUA+"ToraumaRun by Yanaaaaa");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String Label, String[] args) {
        if (cmd.getName().equals("torauma")) {
            if (!(sender.isOp())) {
                sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:このコマンドの実行にはOP権限が必要だよ~！");
            } else if(args.length == 1){
                if(args[0].equals("set")){
                    if(stage==false) {
                        Player p = (Player) sender;
                        SetStage(p);
                        stage = true;
                        sender.sendMessage(ChatColor.GREEN + "[ToraumaRun]:ステージを生成します！");
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:ステージはすでに生成されています！ステージの削除:/torauma remove");
                    }
                }else if(args[0].equals("start")){
                    if(stage==true){
                        if(start==false) {
                            start = true;
                            location.getWorld().setDifficulty(Difficulty.PEACEFUL);
                            Player p = (Player) sender;
                            MainGame(p);
                        }else{
                            sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:ゲームはすでにスタートしています!" );
                        }
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:ステージが生成されていません。/torauma setコマンドでステージを生成してください。");
                    }
                }else if(args[0].equals("stop")){
                    if(start) {
                        start = false;
                        sender.sendMessage(ChatColor.GREEN + "[ToraumaRun]:実行中のゲームに終了命令を出しました。");
                    }else{
                        sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:ゲームは現在実行されていません。");
                    }
                }else if(args[0].equals("remove")){
                        if(stage==true&&location!=null){
                            start = false;
                            int lx = location.getBlockX(),ly = location.getBlockY(), lz = location.getBlockZ();
                            for (int i = 2; i <= 52; i++) {
                                for (int j = 0; j <= 10; j++) {
                                    for(int k = 2;k<=33;k++) {
                                        Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + j, lz + k);
                                        fill.getBlock().setType(Material.AIR);
                                    }
                                }
                            }
                            stage = false;
                            sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:ステージを削除します！");
                        }else{
                            sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:ステージは生成されていません。");
                        }
                }else if(args[0].equals("help")){
                    sender.sendMessage(ChatColor.GOLD + "・/torauma set");
                    sender.sendMessage("ステージの生成");
                    sender.sendMessage(ChatColor.GOLD + "・/torauma remove");
                    sender.sendMessage("生成されたステージの削除");
                    sender.sendMessage(ChatColor.GOLD + "・/torauma start");
                    sender.sendMessage("ゲームをスタートさせる");
                    sender.sendMessage(ChatColor.GOLD + "・/torauma stop");
                    sender.sendMessage("実行中のゲームの停止");
                    sender.sendMessage(ChatColor.GOLD + "・/torauma help");
                    sender.sendMessage("コマンドの一覧を表示");

                }else{
                    sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:引数が違うよ~！コマンド一覧の確認:/torauma help");
                }
            }else if (args.length > 1) {
                sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:引数が多いよ~！コマンド一覧の確認:/torauma help");
            }else {
                sender.sendMessage(ChatColor.YELLOW + "[ToraumaRun]:引数が少ないよ~！コマンド一覧の確認:/torauma help");
            }
        }
        return  true;
    }

    @EventHandler
    public void CheckLocation(PlayerMoveEvent event){
        if(start&&stage){
            if(event.getPlayer().getLocation().getY()<location.getY()+2&&event.getPlayer().getGameMode()!=GameMode.SPECTATOR){
                if(playerListAll.contains(event.getPlayer())) {
                    event.getPlayer().setGameMode(GameMode.SPECTATOR);
                    playerListAll.remove(event.getPlayer());
                    if(playerListAll.size()==0){
                        Last = event.getPlayer();
                    }
                }
            }
        }
    }

    public void MainGame(Player p){
        Location tp = location;
        int lx = location.getBlockX(),ly = location.getBlockY(), lz = location.getBlockZ();
        int tpx = tp.getBlockX()+27,tpy = tp.getBlockY()+3, tpz = tp.getBlockZ()+3;
        for(int i = 3;i<=51;i++){
            for(int j = 3;j<=5;j++){
                Location fill = new Location(Bukkit.getWorld("world"),lx+i,ly+j,lz+7);
                fill.getBlock().setType(Material.WHITE_WOOL);
            }
        }
        playerList = new ArrayList<>();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        for(Player player : Bukkit.getOnlinePlayers()){
            player.setScoreboard(board);
            playerList.add(player);
            playerListAll.add(player);
        }
        Objective objective = board.registerNewObjective("run","dummy","回数");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for(Player player : playerList) {
            Score score = objective.getScore(ChatColor.GREEN + (player.getName() + ":"));
            score.setScore(0);
            p.chat("/tp "+player.getName()+" "+tpx+" "+tpy+" "+tpz);
        }
        new BukkitRunnable() {
            public void run() {
                for(Player player : playerList) {
                    player.sendTitle(ChatColor.GREEN+"開始まで、3秒",null,0,15,0);
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE,100,1);
                }
            }
        }.runTaskLater(this,100);
        new BukkitRunnable() {
            public void run() {
                for(Player player : playerList) {
                    player.sendTitle(ChatColor.GREEN+"開始まで、2秒",null,0,15,0);
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE,100,1);
                }
            }
        }.runTaskLater(this,120);
        new BukkitRunnable(){
            public void run() {
                for(Player player : playerList) {
                    player.sendTitle(ChatColor.GREEN+"開始まで、1秒",null,0,15,0);
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE,100,1);
                }
            }
        }.runTaskLater(this,140);
        new BukkitRunnable() {
            public void run() {
                for (Player player : playerList) {
                    player.sendTitle(ChatColor.GREEN + "スタート!!!", null, 0, 15, 0);
                    player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME,100,1);
                }
                    for (int i = 3; i <= 51; i++) {
                        for (int j = 3; j <= 5; j++) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + j, lz + 7);
                            fill.getBlock().setType(Material.AIR);
                        }
                    }
            }
        }.runTaskLater(this,160);
        c = 1;
            BukkitRunnable runnable1 =new BukkitRunnable() {
                public void run() {
                    if (!start ||playerListAll.size()==0) {
                        task1.cancel();
                        task2.cancel();
                        task3.cancel();
                        task4.cancel();
                        task5.cancel();
                        task6.cancel();
                        for (int i = 3; i <= 51; i++) {
                            for (int j = 8; j <= 27; j++) {
                                for(int k = 2;k <= 7;k++) {
                                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k, lz + j);
                                    fill.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                        if(playerListAll.size()==0){
                            Score score = objective.getScore(ChatColor.GREEN + (Last.getName() + ":"));
                            int sc = score.getScore();
                            for(Player player : playerList) {
                                player.sendTitle(ChatColor.GOLD+("優勝者: "+Last.getName()+" !!!"),ChatColor.AQUA+("スコア:"+sc+"回"),10,100,20);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,100,1);
                            }
                        }
                        Last=null;
                        Athletic(0);
                        start=false;
                    }else if(c == 6){
                        task1.cancel();
                    }else {
                        GameLogic(c,objective);
                        c++;
                    }
                }
            };
            task1 = runnable1.runTaskTimer(this, 160L, 243L);
            BukkitRunnable runnable2 =new BukkitRunnable() {
                public void run() {
                    if (!start ||playerListAll.size()==0) {
                        task2.cancel();
                        task3.cancel();
                        task4.cancel();
                        task5.cancel();
                        task6.cancel();
                        for (int i = 3; i <= 51; i++) {
                            for (int j = 8; j <= 27; j++) {
                                for(int k = 2;k <= 7;k++) {
                                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k, lz + j);
                                    fill.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                        if(playerListAll.size()==0){
                            Score score = objective.getScore(ChatColor.GREEN + (Last.getName() + ":"));
                            int sc = score.getScore();
                            for(Player player : playerList) {
                                player.sendTitle(ChatColor.GOLD+("優勝者: "+Last.getName()+" !!!"),ChatColor.AQUA+("スコア:"+sc+"回"),10,100,20);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,100,1);
                            }
                        }
                        Last=null;
                        Athletic(0);
                        start=false;
                    }else if( c == 16){
                        this.cancel();
                    }else {
                        GameLogic(c,objective);
                        c++;
                    }
                }
            };
             task2 = runnable2.runTaskTimer(this, 1375L, 216L);
             BukkitRunnable runnable3 =new BukkitRunnable() {
                public void run() {
                    if (!start ||playerListAll.size()==0) {
                        task3.cancel();
                        task4.cancel();
                        task5.cancel();
                        task6.cancel();
                        for (int i = 3; i <= 51; i++) {
                            for (int j = 8; j <= 27; j++) {
                                for(int k = 2;k <= 7;k++) {
                                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k, lz + j);
                                    fill.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                        if(playerListAll.size()==0){
                            Score score = objective.getScore(ChatColor.GREEN + (Last.getName() + ":"));
                            int sc = score.getScore();
                            for(Player player : playerList) {
                                player.sendTitle(ChatColor.GOLD+("優勝者: "+Last.getName()+" !!!"),ChatColor.AQUA+("スコア:"+sc+"回"),10,100,20);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,100,1);
                            }
                        }
                        Last=null;
                        Athletic(0);
                        start=false;
                    }else if(c == 26){
                        this.cancel();
                    }else {
                        GameLogic(c,objective);
                        c++;
                    }
                }
            };
             task3 = runnable3.runTaskTimer(this, 3535L, 189L);
             BukkitRunnable runnable4 = new BukkitRunnable() {
                public void run() {
                    if (!start ||playerListAll.size()==0) {
                        task4.cancel();
                        task5.cancel();
                        task6.cancel();
                        for (int i = 3; i <= 51; i++) {
                            for (int j = 8; j <= 27; j++) {
                                for(int k = 2;k <= 7;k++) {
                                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k, lz + j);
                                    fill.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                        if(playerListAll.size()==0){
                            Score score = objective.getScore(ChatColor.GREEN + (Last.getName() + ":"));
                            int sc = score.getScore();
                            for(Player player : playerList) {
                                player.sendTitle(ChatColor.GOLD+("優勝者: "+Last.getName()+" !!!"),ChatColor.AQUA+("スコア:"+sc+"回"),10,100,20);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,100,1);
                            }
                        }
                        Last=null;
                        Athletic(0);
                        start=false;
                    }else if(c==51){
                        this.cancel();
                    }else {
                        GameLogic(c,objective);
                        c++;
                    }
                }
            };
            task4 = runnable4.runTaskTimer(this, 5425L, 162L);
             BukkitRunnable runnable5 = new BukkitRunnable() {
                public void run() {
                    if (!start ||playerListAll.size()==0) {
                        task5.cancel();
                        task6.cancel();
                        for (int i = 3; i <= 51; i++) {
                            for (int j = 8; j <= 27; j++) {
                                for(int k = 2;k <= 7;k++) {
                                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k, lz + j);
                                    fill.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                        if(playerListAll.size()==0){
                            Score score = objective.getScore(ChatColor.GREEN + (Last.getName() + ":"));
                            int sc = score.getScore();
                            for(Player player : playerList) {
                                player.sendTitle(ChatColor.GOLD+("優勝者: "+Last.getName()+" !!!"),ChatColor.AQUA+("スコア:"+sc+"回"),10,100,20);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,100,1);
                            }
                        }
                        Last=null;
                        Athletic(0);
                        start=false;
                    }else if(c==101){
                        this.cancel();
                    }else {
                        GameLogic(c,objective);
                        c++;
                    }
                }
            };
            task5 = runnable5.runTaskTimer(this, 9475L, 135L);
            BukkitRunnable runnable6 =new BukkitRunnable() {
                public void run() {
                    if (!start||playerListAll.size()==0) {
                        task6.cancel();
                        for (int i = 3; i <= 51; i++) {
                            for (int j = 8; j <= 27; j++) {
                                for(int k = 2;k <= 7;k++) {
                                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k, lz + j);
                                    fill.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                        if(playerListAll.size()==0){
                            Score score = objective.getScore(ChatColor.GREEN + (Last.getName() + ":"));
                            int sc = score.getScore();
                            for(Player player : playerList) {
                                player.sendTitle(ChatColor.GOLD+("優勝者: "+Last.getName()+" !!!"),ChatColor.AQUA+("スコア:"+sc+"回"),10,100,20);
                                player.getLocation().getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,100,1);
                            }
                        }
                        Last=null;
                        Athletic(0);
                        start=false;
                    }else {
                        GameLogic(c,objective);
                        c++;
                    }
                }
            };
            task6 = runnable6.runTaskTimer(this, 16225L, 108L);
    }

    public void PanelLogic(int count){
        if(count<=5){
            int num = 0;
            Athletic(num);
        }else if(count<=15){
            int num = rand.nextInt(9);
            Athletic(num);
        }else if(count<=25){
            int num = rand.nextInt(14);
            Athletic(num);
        }else if(count<=50){
            int num = rand.nextInt(19);
            Athletic(num);
        }else{
            int num = rand.nextInt(24);
            Athletic(num);
        }
    }


    public void PanelRemove(int num){
        int lx = location.getBlockX(),ly = location.getBlockY(), lz = location.getBlockZ();
        if(num==1){
            for (int i = 3; i <= 51; i++) {
                for (int j = 3; j <= 7; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }else{
            for (int i = 3; i <= 51; i++) {
                for (int j = 28; j <= 32; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }
        for (int i = 3; i <= 51; i++) {
            for (int j = 8; j <= 27; j++) {
                for(int k = 2;k <= 7;k++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k, lz + j);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }
    }



    public long DelayMain(int num){
        long sum;
        if(num<=5){
            sum=9;
        }else if(num<=15){
            sum=8;
        }else if(num<=25){
            sum=7;
        }else if(num<=50){
            sum=6;
        }else if(num<=100){
            sum=5;
        }else{
            sum=4;
        }
        return sum;
    }

    public void SetStage(Player player){
        Location loc = player.getLocation();
        location = loc;
        int lx = loc.getBlockX(),ly = loc.getBlockY(), lz = loc.getBlockZ();
        for (int i = 2; i <= 52; i++) {
            for (int j = 0; j <= 10; j++) {
                for(int k = 2;k<=33;k++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + j, lz + k);
                    fill.getBlock().setType(Material.DARK_OAK_PLANKS);
                }
            }
        }
        for (int i = 3; i <= 51; i++) {
            for (int j = 1; j <= 10; j++) {
                for(int k = 3;k<=32;k++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + j, lz + k);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }
        for (int i = 3; i <= 51; i++) {
            for (int j = 3; j <= 6; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 51; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 7);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
        for (int i = 3; i <= 51; i++) {
            for (int j = 8; j <= 27; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
        }
        for (int i = 3; i <= 51; i++) {
            for (int j = 29; j <= 32; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 51; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 28);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
    }
    public void GameLogic(int count,Objective objective){
        if (count == 6 || count == 16 || count == 26 || count == 51 || count == 101) {
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 2 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.6F);
                    }
                    if(count<50) {
                        PanelRemove(1);
                    }else{
                        PanelRemove(2);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 3 );
        } else {
            if (count >= 2) {
                new BukkitRunnable() {
                    public void run() {
                        for (Player player : playerList) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.71F);
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                        }
                        if(count%2==0){
                            PanelRemove(1);
                        }else{
                            PanelRemove(2);
                        }
                    }
                }.runTaskLater(this, DelayMain(count) * 3 );
            } else {
                new BukkitRunnable() {
                    public void run() {
                        for (Player player : playerList) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                        }
                    }
                }.runTaskLater(this, DelayMain(count) );
                new BukkitRunnable() {
                    public void run() {
                        for (Player player : playerList) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                        }
                    }
                }.runTaskLater(this, DelayMain(count) * 2 );
                new BukkitRunnable() {
                    public void run() {
                        for (Player player : playerList) {
                            player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.6F);
                        }
                    }
                }.runTaskLater(this, DelayMain(count) * 3);
            }
        }
        if (count % 2 == 0) {
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                    }
                    PanelLogic(count);
                    for(Player player : playerListAll) {
                        Score score = objective.getScore(ChatColor.GREEN + (player.getName() + ":"));
                        score.setScore(count-1);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 6 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.33F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 9 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.18F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 12 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 15 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.93F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 18 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.88F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 21 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.79F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 24 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.71F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 27 );
        } else {
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.71F);
                    }
                    PanelLogic(count);
                    for(Player player : playerListAll) {
                        Score score = objective.getScore(ChatColor.GREEN + (player.getName() + ":"));
                        score.setScore(count-1);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 6 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.79F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 9 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.88F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 12 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 0.93F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 15 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.05F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 18 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.18F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 21 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.33F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 24 );
            new BukkitRunnable() {
                public void run() {
                    for (Player player : playerList) {
                        player.getLocation().getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 100, 1.41F);
                    }
                }
            }.runTaskLater(this, DelayMain(count) * 27 );
        }
    }
    public void Athletic(int num){
        int lx = location.getBlockX(),ly = location.getBlockY(), lz = location.getBlockZ();
        if(num==0){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.POLISHED_ANDESITE);
                }
            }
        }else if(num==1){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.POLISHED_ANDESITE);
                    j=j+1;
                }
            }
        }else if(num==2){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.POLISHED_ANDESITE);
                    j=j+2;
                }
            }
        }else if(num==3){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.POLISHED_ANDESITE);
                    j=j+3;
                }
            }
        }else if(num==4){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    if(j%2!=0) {
                        Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                        fill.getBlock().setType(Material.POLISHED_ANDESITE);
                    }
                }
            }
        }else if(num==5){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.JUNGLE_FENCE);
                }
            }
        }else if(num==6){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    if(j%2==0) {
                        if (i % 2 == 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.POLISHED_ANDESITE);
                        }
                    }else{
                        if (i % 2 != 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.POLISHED_ANDESITE);
                        }
                    }
                }
            }
        }else if(num==7){
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 9);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 12);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 4, lz + 15);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 5, lz + 18);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 4, lz + 21);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 24);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 27);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
        }else if(num==8){
            for (int i = 3; i <= 51; i++) {
                for (int j = 15; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.POLISHED_ANDESITE);
                    j=j+3;
                }
            }
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 14; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.POLISHED_ANDESITE);
                    j=j+1;
                }
            }
        }else if(num==9){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    if(j%2==0) {
                        if (i % 2 == 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.GLASS);
                        }
                    }else{
                        if (i % 2 != 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.GLASS_PANE);
                        }
                    }
                }
            }
        }else if(num==10){
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 9);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 12);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 15);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 18);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 4, lz + 21);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 24);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 27);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
        }else if(num==11){
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 9);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 12);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 15);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 18);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 21);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 24);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 27);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
        }else if(num==12){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.BIRCH_FENCE_GATE);
                }
            }
        }else if(num==13){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.IRON_BARS);
                }
            }
        }else if(num==14){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.ACACIA_FENCE);
                }
            }
        }else if(num==15){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.IRON_BARS);
                    j=j+1;
                }
            }
        }else if(num==16){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.GLASS_PANE);
                    j=j+1;
                }
            }
        }else if(num==17){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    if(j%2==0) {
                        if (i % 2 == 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.BLUE_ICE);
                        }
                    }else{
                        if (i % 2 != 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.GLASS);
                        }
                    }
                }
            }
        }else if(num==18){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    if(j%2==0) {
                        if (i % 2 == 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.BLUE_ICE);
                        }
                    }else{
                        if (i % 2 != 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.SOUL_SAND);
                        }
                    }
                }
            }
        }else if(num==19){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    if(j%2==0) {
                        if (i % 2 == 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.IRON_BARS);
                        }
                    }else{
                        if (i % 2 != 0) {
                            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                            fill.getBlock().setType(Material.IRON_BARS);
                        }
                    }
                }
            }
        }else if(num==20){
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 9);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 12);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 4, lz + 15);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 5, lz + 18);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 4, lz + 21);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 24);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 27);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
        }else if(num==21){
            for (int i = 3; i <= 51; i++) {
                for (int j = 8; j <= 27; j++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                    fill.getBlock().setType(Material.BLUE_ICE);
                    j=j+3;
                }
            }
        }else if(num==22){
            for (int j = 8; j <= 27; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + 24, ly + 2, lz + j);
                fill.getBlock().setType(Material.IRON_BARS);
            }
        }else if(num==23){
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 8);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 12);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 4, lz + 16);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 20);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 24);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 27);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
        }else if(num==24){
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 9);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 12);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 4, lz + 15);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 18);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 21);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 3, lz + 24);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
            for (int i = 3; i <= 51; i++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 27);
                fill.getBlock().setType(Material.BLUE_ICE);
            }
        }
        for (int i = 3; i <= 51; i++) {
            for (int j = 3; j <= 6; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 51; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 7);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
        for (int i = 3; i <= 51; i++) {
            for (int j = 29; j <= 32; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + j);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 51; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + 2, lz + 28);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
    }
}
