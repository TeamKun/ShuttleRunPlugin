package net.kunmc.lab.toraumarun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class ToraumaRun extends JavaPlugin {

    public static ToraumaRun INSTANCE;
    public File schematicDirectory;

    /**
     * プラグイン起動時の処理
     */
    @Override
    public void onEnable() {
        this.getCommand("torauma").setExecutor(new CommandExecutor());
        this.getCommand("torauma").setTabCompleter(new CommandTabCompleter());
        Bukkit.getPluginManager().registerEvents(new EventHandler(),this);
        getServer().getLogger().info(ChatColor.AQUA+"ToraumaRunPlugin by Yanaaaaa");
        INSTANCE = this;
        //スケム用ディレクトリの生成
        schematicDirectory = new File(getDataFolder(),"schematics");
        schematicDirectory.mkdirs();
    }




}
