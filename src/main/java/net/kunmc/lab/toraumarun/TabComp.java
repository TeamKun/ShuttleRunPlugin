package net.kunmc.lab.toraumarun;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TabComp implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
        if (cmd.getName().equals("torauma")) {
            if (args.length == 1) {
                return (sender.hasPermission("torauma")
                        ? Stream.of("set", "stop", "remove", "start", "help")
                        : Stream.of("set", "stop", "remove", "start", "help"))
                        .filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
            }
        }
        return null;
    }
}



