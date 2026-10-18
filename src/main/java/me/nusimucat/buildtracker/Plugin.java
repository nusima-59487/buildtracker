package me.nusimucat.buildtracker;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.nusimucat.buildtracker.commands.MainCommand;
import me.nusimucat.buildtracker.commands.MarkerCommand;
import me.nusimucat.buildtracker.commands.TaskCommand;
import me.nusimucat.buildtracker.data.MarkerData;
import me.nusimucat.buildtracker.data.TaskData;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;

public class Plugin extends JavaPlugin {
    private static ComponentLogger loggerInstance;
    private static Plugin pluginInstance;

    public void onEnable() {
        pluginInstance = this;
        loggerInstance = this.getComponentLogger();
        String pluginVersion = this.getPluginMeta().getVersion();

        saveDefaultConfig();

        ConfigurationSerialization.registerClass(MarkerData.class);
        ConfigurationSerialization.registerClass(TaskData.class);

        DataManager.init();
        MessageManager.init();

        loggerInstance.info("BuildTracker v"+pluginVersion+" enabled!");
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(MainCommand.COMMAND);
            commands.registrar().register(MarkerCommand.COMMAND);
            commands.registrar().register(TaskCommand.COMMAND);
        });

        // this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
        //     commands.registrar().register(TextDisplayTools.textDisplayMainCommand);
        // });

        // check version to remove help files
        //File versionFile = new File(Plugin.getInstance().getDataFolder(), "PLUGIN_VERSION");
        //if (versionFile.exists()) {
        //    byte[] shit;
        //    try {
        //        shit = Files.readAllBytes(versionFile.toPath());
        //        String content = new String(shit);

        //        if (content.equals(pluginVersion)) {
        //            return;
        //        }
        //    } catch (IOException e) {
        //        Plugin.getLoggerInstance().warn("Failed to read file PLUGIN_VERSION");
        //        Plugin.getLoggerInstance().warn(e.toString());
        //    }
        //} else {
        //    try {
        //        versionFile.createNewFile();
        //    } catch (IOException e) {
        //        Plugin.getLoggerInstance().error("Failed to create file PLUGIN_VERSION");
        //        Plugin.getLoggerInstance().error(e.toString());
        //    }
        //}

        //try (FileWriter versionFileWriter = new FileWriter(versionFile)) {
        //    versionFileWriter.write(pluginVersion);
        //    versionFileWriter.flush();
        //} catch (Exception e) {
        //    Plugin.getLoggerInstance().error("Failed to write file PLUGIN_VERSION");
        //    Plugin.getLoggerInstance().error(e.toString());
        //}

        //Plugin.getLoggerInstance().info("Deleting help files from older versions...");
        //File commandHelpFile = new File(Plugin.getInstance().getDataFolder(), "commandHelp.txt");
        //if (commandHelpFile.exists()) {
        //    commandHelpFile.delete();
        //}
        //File textHelpFile = new File(Plugin.getInstance().getDataFolder(), "textHelp.txt");
        //if (textHelpFile.exists()) {
        //    textHelpFile.delete();
        //}
    }

    public void onDisable() {
        loggerInstance.info("TextDisplayTools disabled");
    }

    public static Plugin getInstance () {
        return pluginInstance;
    }
    public static ComponentLogger getLoggerInstance () {
        return loggerInstance;
    }
}
