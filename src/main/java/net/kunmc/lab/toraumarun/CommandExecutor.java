package net.kunmc.lab.toraumarun;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    public static boolean stage = false, start = false;
    public static Location mainloc  = null;

    /**
     * コマンド周りの処理
     */
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equals("torauma")){
            if(args.length==1){
                //ステージ設置の処理
                if(args[0].equals("set")){
                    if(!stage){
                        stage = true;
                        sender.sendMessage(ChatColor.GREEN +"[ToraumaRun]:ステージを設置します。");
                        Player player = (Player)sender;
                        mainloc = player.getLocation();
                        try {
                            StageLogic.setStage();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sender.sendMessage(ChatColor.YELLOW +"[ToraumaRun]:すでにステージは設置されています。");
                    }
                }
                //ゲーム開始の処理
                else if(args[0].equals("start")){
                    if(start){
                        sender.sendMessage(ChatColor.YELLOW +"[ToraumaRun]:ゲームはすでに開始しています。/torauma stopでゲームを終了してください。");
                    }else if(stage){
                        start = true;
                        sender.sendMessage(ChatColor.GREEN +"[ToraumaRun]:ゲームを開始します。");
                        GameLogic.startGame(mainloc);
                    }else{
                        sender.sendMessage(ChatColor.YELLOW +"[ToraumaRun]:ステージが設置されていません。/torauma setでステージを設置してください。");
                    }

                }
                //ゲーム終了の処理
                else if(args[0].equals("stop")){
                    if(start){
                        start = false;
                        sender.sendMessage(ChatColor.GREEN +"[ToraumaRun]:実行中のゲームに終了命令を出しました。");
                    }else{
                        sender.sendMessage(ChatColor.YELLOW +"[ToraumaRun]:ゲームは実行されていません。");
                    }

                }
                //コマンド一覧の生成
                else if(args[0].equals("help")){
                    sender.sendMessage(ChatColor.GOLD + "・/torauma set");
                    sender.sendMessage("ステージの生成");
                    sender.sendMessage(ChatColor.GOLD + "・/torauma start");
                    sender.sendMessage("ゲームをスタートさせる");
                    sender.sendMessage(ChatColor.GOLD + "・/torauma stop");
                    sender.sendMessage("実行中のゲームの停止");
                    sender.sendMessage(ChatColor.GOLD + "・/torauma help");
                    sender.sendMessage("コマンドの一覧を表示");
                }else{
                    sender.sendMessage(ChatColor.YELLOW +"[ToraumaRun]:コマンドの形式が異なります。/torauma helpでコマンド一覧を確認できます。");
                }
            }
        }
        return true;
    }

}
