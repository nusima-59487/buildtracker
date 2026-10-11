package me.nusimucat.buildtracker;

import java.util.function.Predicate;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public class Utils {
    public static Predicate<CommandSourceStack> isConsoleOrPlayerWithPerm(String perm) {
        return src -> {
            CommandSender sender = src.getSender();
            if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender)
                return true;
            if (!(sender instanceof Player player))
                return false;
            return player.hasPermission(perm);
        };
    }
}
