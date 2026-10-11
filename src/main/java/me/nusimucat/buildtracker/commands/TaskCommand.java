package me.nusimucat.buildtracker.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

public class TaskCommand {
    public static final LiteralCommandNode<CommandSourceStack> COMMAND = Commands.literal("task")



        .build();

}
