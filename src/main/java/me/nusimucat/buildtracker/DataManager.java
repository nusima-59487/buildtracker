package me.nusimucat.buildtracker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.nusimucat.buildtracker.data.MarkerData;

public class DataManager {
    static final String MARKER_DATA_FOLDER = "markers";

    public static void init() {
        // create marker data folder
        Path markerDirPath = Paths.get(Plugin.getInstance().getDataFolder().getPath(), MARKER_DATA_FOLDER);
        try {
			Files.createDirectories(markerDirPath);
		} catch (IOException e) {
		    Plugin.getLoggerInstance().error("Failed to create markers directory!", e);
		}
    }

    /**
    * Loads an existing MarkerData
    * @param markerId id of marker to load
    * @return the loaded MarkerData, or null if not found or invalid
    */
    @Nullable
    public static MarkerData loadMarker(@NotNull String markerId) {
        String filepath = String.format(MARKER_DATA_FOLDER+"/%s.yml", markerId);
        File file = new File(Plugin.getInstance().getDataFolder(), filepath);
        if (!file.exists()) {
            return null;
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> configRootValues = config.getValues(true);
        if (configRootValues.isEmpty()) {
            return null;
        }


        return new MarkerData(configRootValues);
    }


    /**
     * Saves a MarkerData to disk
     * @param markerId id of marker to save
     * @param markerData MarkerData to save
     * @throws IOException if an error occurs while saving
     */
    public static void saveMarker(
        @NotNull String markerId,
        @NotNull MarkerData markerData
    ) throws IOException {
        String filepath = String.format(MARKER_DATA_FOLDER+"/%s.yml", markerId);
        File file = new File(Plugin.getInstance().getDataFolder(), filepath);
        YamlConfiguration config = new YamlConfiguration();

        for (Map.Entry<String, Object> entry : markerData.serialize().entrySet()) {
            config.set(entry.getKey(), entry.getValue());
        }

        config.save(file);
    }


    public static void deleteMarker(@NotNull String markerId) {
        String filepath = String.format(MARKER_DATA_FOLDER+"/%s.yml", markerId);
        File file = new File(Plugin.getInstance().getDataFolder(), filepath);
        file.delete();
    }

    public static List<String> listMarkers() throws IOException {
        Path markerDirPath = Paths.get(Plugin.getInstance().getDataFolder().getPath(), MARKER_DATA_FOLDER);
        try (Stream<Path> paths = Files.walk(markerDirPath)) {
			List<String> collect = paths.filter(path ->
			        Files.isRegularFile(path) && path.toString().endsWith(".yml")
			    )
			    .map(path -> path.getFileName().toString().replace(".yml", ""))
			    .collect(Collectors.toList());
			return collect;
		}
    }

    public static List<String> listMarkersFilterAuthor(String playerFilter) throws IOException {
        Path markerDirPath = Paths.get(Plugin.getInstance().getDataFolder().getPath(), MARKER_DATA_FOLDER);
        try (Stream<Path> paths = Files.walk(markerDirPath)) {
			List<String> collect = paths.filter(path ->
			        Files.isRegularFile(path) && path.toString().endsWith(".yml")
			    )
			    .map(path -> path.getFileName().toString().replace(".yml", ""))
				.filter(markerId -> {
				    MarkerData e = loadMarker(markerId);
					if (e == null) return false;
					return e.getCreator().equalsIgnoreCase(playerFilter);
				})
			    .collect(Collectors.toList());
			return collect;
		}
    }

    public static List<String> listMarkersFilterAssignee(String playerFilter) throws IOException {
        Path markerDirPath = Paths.get(Plugin.getInstance().getDataFolder().getPath(), MARKER_DATA_FOLDER);
        try (Stream<Path> paths = Files.walk(markerDirPath)) {
			List<String> collect = paths.filter(path ->
			        Files.isRegularFile(path) && path.toString().endsWith(".yml")
			    )
			    .map(path -> path.getFileName().toString().replace(".yml", ""))
				.filter(markerId -> {
				    MarkerData e = loadMarker(markerId);
					if (e == null) return false;
					return e.getTasks().containsKey(playerFilter);
				})
			    .collect(Collectors.toList());
			return collect;
		}
    }

    @Nullable
    public static List<String> listMarkerTypes() {
		ConfigurationSection configurationSection = Plugin.getInstance().getConfig().getConfigurationSection("marker_type_track");
		if (configurationSection == null) {
		    return null;
		}

		List<String> list = configurationSection.getKeys(false).stream().toList();
		return list;
		// for (String key : configurationSection.getkey)
    }
}
