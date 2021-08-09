package net.kunmc.lab.toraumarun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandTabCompleter implements TabCompleter {

    /**
     * Tab補完の生成
     */
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equals("torauma")){
            if(args.length==1){
                return (sender.hasPermission("torauma")
                        ? Stream.of("set","start","help","stop")
                        : Stream.of("set","start","help","stop"))
                        .filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}
