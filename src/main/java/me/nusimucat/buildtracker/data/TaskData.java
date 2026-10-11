package me.nusimucat.buildtracker.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TaskData implements ConfigurationSerializable {
    private String instructions;
    private boolean isTaskDone;
    private String comment;

    public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public boolean isTaskDone() {
		return isTaskDone;
	}

	public void setTaskDone(boolean isTaskDone) {
		this.isTaskDone = isTaskDone;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TaskData (@Nullable String instructions, @NotNull boolean isTaskDone, @Nullable String comment) {
        this.instructions = instructions;
        this.isTaskDone = isTaskDone;
        this.comment = comment;
    }

    public TaskData(@NotNull Map<String, Object> map) {
        this.instructions = (String) map.get("instuctions");
        this.isTaskDone = (boolean) map.get("is_task_done");
        this.comment = (String) map.get("comment");
    }

	@Override
	public @NotNull Map<String, Object> serialize() {
	    Map<String, Object> map = new HashMap<>();
		map.put("instructions", this.instructions);
		map.put("is_task_done", this.isTaskDone);
		map.put("comment", this.comment);

		return map;
	}
}
