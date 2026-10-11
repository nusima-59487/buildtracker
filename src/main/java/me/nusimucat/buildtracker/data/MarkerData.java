package me.nusimucat.buildtracker.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import me.nusimucat.buildtracker.DataManager;

public class MarkerData implements ConfigurationSerializable {
    private String name;
    private String creator;
    private String type;
    private boolean isCompleted;
    private String description;
    private Location location;
    private boolean dynmapVisible;
    private Map<String, TaskData> tasks;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) throws IllegalArgumentException {
        if (!DataManager.listMarkerTypes().contains(type)) {
            throw new IllegalArgumentException("Invalid marker type: " + type);
        }
		this.type = type;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isDynmapVisible() {
		return dynmapVisible;
	}

	public void setDynmapVisible(boolean dynmap_visible) {
		this.dynmapVisible = dynmap_visible;
	}

	public Map<String, TaskData> getTasks() {
		return tasks;
	}

	public void setTasks(Map<String, TaskData> assignees) {
		this.tasks = assignees;
	}

	public MarkerData (@Nullable String name, @NotNull String creator, @NotNull String type, @NotNull boolean isCompleted, @Nullable String description, @NotNull Location location, @NotNull boolean dynmap_visible, @NotNull Map<String, TaskData> assignees) {
        this.name = name;
        this.creator = creator;
        this.type = type;
        this.isCompleted = isCompleted;
        this.description = description;
        this.location = location;
        this.dynmapVisible = dynmap_visible;
        this.tasks = assignees;
    }

    public MarkerData (Map<String, Object> map) {
        this.name = (String) map.get("name");
        this.creator = (String) map.get("creator");
        this.type = (String) map.get("type");
        this.isCompleted = (boolean) map.get("is_completed");
        this.description = (String) map.get("description");
        this.location = (Location) map.get("location");
        this.dynmapVisible = (boolean) map.get("dynmap_visible");
        // this.assignees = (Map<String, ReviewData>) map.get("assignees");
        this.tasks = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        if (map.get("assignees") instanceof Map) {
            Map<String, Object> rawMap = (Map<String, Object>) map.get("assignees");

            for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                if (entry.getValue() instanceof TaskData data) {
                    this.tasks.put(entry.getKey(), data);
                }
            }
        }
    }

	@Override
	public @NotNull Map<String, Object> serialize() {
	    Map<String, Object> map = new HashMap<>();
		// Map<String, Object> serializedAssigneesMap = this.assignees.entrySet().stream()
		//     .collect(Collectors.toMap(
		// 		Map.Entry::getKey,
		// 		entry -> entry.getValue().serialize()
		// 	)
		// );
		map.put("name", this.name);
		map.put("creator", this.creator);
		map.put("type", this.type);
		map.put("is_completed", this.isCompleted);
		map.put("description", this.description);
		map.put("location", this.location);
		map.put("dynmap_visible", this.dynmapVisible);
		map.put("assignees", this.tasks);
		return map;
	}


	public boolean validateEdit(@NotNull String player) {
	    return !this.isCompleted && this.creator.equalsIgnoreCase(player);
	}

	public boolean validateTaskAssignee(@NotNull String player) {
	    return !this.isCompleted && this.tasks.containsKey(player);
	}

	@Nullable
	public String advance() throws IllegalArgumentException {
	    if (this.isCompleted)
			return null;
	    List<String> markerTypes = DataManager.listMarkerTypes();
		if (markerTypes == null) {
		    throw new IllegalArgumentException("There are no configured marker types!");
		}
		if (!markerTypes.contains(this.type)) {
		    throw new IllegalArgumentException("Invalid marker type: " + this.type);
		}

		int index = markerTypes.indexOf(this.type);
		if (index <= 0) { // shd not be smaller but just to make sure
		    this.isCompleted = true;
			return null;
		} else {
		    String newType = markerTypes.get(index -1);
		    this.type = newType;
		    this.isCompleted = false;
			return newType;
		}
	}
}
