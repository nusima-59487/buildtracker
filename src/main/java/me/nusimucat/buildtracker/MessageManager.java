package me.nusimucat.buildtracker;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.nusimucat.buildtracker.data.MarkerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public class MessageManager {
    private static final String MESSAGE_CONFIG_FILE = "messages.yml";
    private static YamlConfiguration config;

    public static void init() {
        // TODO: `replace` param to false
        Plugin.getInstance().saveResource(MESSAGE_CONFIG_FILE, true);
        config = YamlConfiguration.loadConfiguration(new File(Plugin.getInstance().getDataFolder(), MESSAGE_CONFIG_FILE));
    }

    public static Component getMarkerInfoComponent(String id, MarkerData data, Player player) {
        if (config == null) {
            Plugin.getLoggerInstance().warn("MessageManager not initialized!");
            return null;
        }

        String template = config.getConfigurationSection("messages").getString("marker_list_info");
        Component infoMessage = MiniMessage.miniMessage().deserialize(
            template,
            Placeholder.unparsed("id", id),
            Placeholder.unparsed("name", data.getName()),
            Placeholder.unparsed("creator", data.getCreator()),
            Placeholder.unparsed("type", data.getType()),
            // Placeholder.unparsed("isCompleted", data.isCompleted()),
            Placeholder.unparsed("description", data.getDescription() == null ? "" : data.getDescription()),
            Placeholder.component("location", parseLocation(data.getLocation())),
            Placeholder.component("location_action", parseLocationAction(data.getLocation()))

        );

        Component actionsMessage;
        if (player.hasPermission("bt.reviewer")|| data.validateTaskAssignee(player.getName())) {
            actionsMessage = MiniMessage.miniMessage().deserialize("<location_action> <info_action> <task_mark_action>",
                Placeholder.component("location_action", parseLocationAction(data.getLocation())),
                Placeholder.component("info_action", parseInfoAction(id)),
                Placeholder.component("task_mark_action", parseTaskMarkAction(id))
            );
        } else {
            actionsMessage = MiniMessage.miniMessage().deserialize("<location_action> <info_action>",
                Placeholder.component("location_action", parseLocationAction(data.getLocation())),
                Placeholder.component("info_action", parseInfoAction(id))
            );
        }

        return infoMessage.appendNewline().append(actionsMessage);
    }





    // templates
    public static Component parseLocation(Location location) {
        return MiniMessage.miniMessage().deserialize(
            config.getConfigurationSection("templates").getString("location"),
            Placeholder.parsed("x", String.valueOf(location.getBlockX())),
            Placeholder.parsed("y", String.valueOf(location.getBlockY())),
            Placeholder.parsed("z", String.valueOf(location.getBlockZ()))
        );
    }


    public static Component parseLocationAction(Location location) {
        return MiniMessage.miniMessage().deserialize(
            config.getConfigurationSection("templates").getString("location_action"),
            Placeholder.parsed("x", String.valueOf(location.getBlockX())),
            Placeholder.parsed("y", String.valueOf(location.getBlockY())),
            Placeholder.parsed("z", String.valueOf(location.getBlockZ()))
        );
    }
    public static Component parseInfoAction(String markerId) {
        return MiniMessage.miniMessage().deserialize(
            config.getConfigurationSection("templates").getString("info_action"),
            Placeholder.parsed("marker_id", markerId)
        );
    }
    public static Component parseTaskMarkAction(String markerId) {
            return MiniMessage.miniMessage().deserialize(
                config.getConfigurationSection("templates").getString("task_mark_action"),
                Placeholder.parsed("marker_id", markerId)
            );
        }
}
