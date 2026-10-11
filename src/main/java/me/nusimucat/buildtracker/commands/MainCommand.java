package me.nusimucat.buildtracker.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import me.nusimucat.buildtracker.DataManager;
import me.nusimucat.buildtracker.Plugin;
import me.nusimucat.buildtracker.Utils;
import me.nusimucat.buildtracker.data.MarkerData;

public class MainCommand {
    public static final LiteralCommandNode<CommandSourceStack> COMMAND = Commands.literal("bt")
        // .requires(src ->
        //     src.getSender() instanceof Player player
        //     && player.hasPermission("buildtracker.user")
        // )
        .requires(Utils.isConsoleOrPlayerWithPerm("buildtracker.user"))

        .then(Commands.literal("list")
            .then(Commands.literal("markers")
                .executes(ctx -> {
                    List<String> markerIds;
					try {
						markerIds = DataManager.listMarkers();
					} catch (IOException e) {
					    ctx.getSource().getSender().sendMessage("Failed to get list");
						return 0;
					}

					if (markerIds.size() == 0) {
    					ctx.getSource().getSender().sendMessage("There are currently no markrers!");
    					return Command.SINGLE_SUCCESS;
					}

                    for (String markerId : markerIds) {
                        MarkerData data = DataManager.loadMarker(markerId);
                        if (data == null)
                            continue;
                        // TODO: make this fancier
                        ctx.getSource().getSender().sendMessage(""+data.getName());
                    }

                    return Command.SINGLE_SUCCESS;
                })
            )
            .then(Commands.literal("tasks")
            )

        )

        .then(Commands.literal("mark")
            .then(Commands.argument("marker_id", StringArgumentType.word())
                .then(Commands.argument("marker_type", StringArgumentType.word())
                    .then(Commands.argument("marker_name", StringArgumentType.string())
                        .executes(ctx -> {
                            if (!(ctx.getSource().getExecutor() instanceof Player player)) {
                                // TODO: change the text ig
                                ctx.getSource().getSender().sendMessage("not a player!");
                                return 0;
                            }

                            List<String> markerIds;
        					try {
        						markerIds = DataManager.listMarkers();
        					} catch (IOException e) {
        					    ctx.getSource().getSender().sendMessage("Failed to get list");
        						return 0;
        					}

                            String markerId = ctx.getArgument("marker_id", String.class);
                            String markerType = ctx.getArgument("marker_type", String.class);
                            String markerName = ctx.getArgument("marker_name", String.class);

                            if (markerIds.contains(markerId)) {
                                ctx.getSource().getSender().sendMessage("marker with same id already exists!");
                                return 0;
                            }
                            if (!DataManager.listMarkerTypes().contains(markerType)) {
                                ctx.getSource().getSender().sendMessage("no such marker type!");
                                return 0;
                            }

                            MarkerData markerData = new MarkerData(markerName, player.getName(), markerType, false, null, player.getLocation(), false, new HashMap<>());
                            try {
								DataManager.saveMarker(markerId, markerData);
							} catch (IOException e) {
							    Plugin.getLoggerInstance().warn("Failed to save marker "+markerId+"!", e);
							    ctx.getSource().getSender().sendMessage("Failed to save the marker!");
								return 0;
							}
							ctx.getSource().getSender().sendMessage("Marker "+markerId+" ");

							return Command.SINGLE_SUCCESS;
                        })
                    )
                )
            )
        )

        .then(Commands.literal("info")

        )

        .then(Commands.literal("reload")
            .requires(Utils.isConsoleOrPlayerWithPerm("buildtracker.admin"))
        )





        .build();
}
