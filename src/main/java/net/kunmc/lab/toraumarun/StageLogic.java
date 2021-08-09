package net.kunmc.lab.toraumarun;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.IOException;

public class StageLogic {

    /**
     * メインステージの生成
     */
    static void setStage() throws IOException {
        WEUtil.createPlaceOperation(CommandExecutor.mainloc,"stage");
    }

    /**
     * ゲーム中のステージの生成
     * @param location 生成位置
     */
    static void setBoard(Location location){
        Location loc = location;
        int lx = loc.getBlockX(),ly = loc.getBlockY(), lz = loc.getBlockZ();
        for (int i = 3; i <= 52; i++) {
            for (int j = 3; j <= 6; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + j-6);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 52; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + 1);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
        for (int i = 3; i <= 52; i++) {
            for (int j = 29; j <= 32; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + j-6);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 52; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + 22);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
    }

    /**
     * ゲーム終了時のステージの生成
     * @param location 生成位置
     */
    static void setAllBoard(Location location){
        Location loc = location;
        int lx = loc.getBlockX(),ly = loc.getBlockY(), lz = loc.getBlockZ();
        for (int i = 3; i <= 52; i++) {
            for (int j = 8; j <= 27; j++) {
                for(int k = 2;k <= 12;k++) {
                    Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly + k -5, lz + j -6);
                    fill.getBlock().setType(Material.AIR);
                }
            }
        }
        for (int i = 3; i <= 52; i++) {
            for (int j = 8; j <= 27; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + j-6);
                fill.getBlock().setType(Material.POLISHED_ANDESITE);
            }
        }
        for (int i = 3; i <= 52; i++) {
            for (int j = 3; j <= 6; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + j-6);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 52; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + 1);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
        for (int i = 3; i <= 52; i++) {
            for (int j = 29; j <= 32; j++) {
                Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + j-6);
                fill.getBlock().setType(Material.BIRCH_PLANKS);
            }
        }
        for (int i = 3; i <= 52; i++) {
            Location fill = new Location(Bukkit.getWorld("world"), lx + i, ly - 3, lz + 22);
            fill.getBlock().setType(Material.WHITE_WOOL);
        }
    }
}
