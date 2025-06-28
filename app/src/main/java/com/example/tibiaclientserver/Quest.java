package com.example.tibiaclientserver;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    private String id;
    private String name;
    private String description;
    private List<String> objectives;
    private List<String> rewards;
    private boolean completed;

    public Quest(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.objectives = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.completed = false;
    }

    public void addObjective(String objective) {
        objectives.add(objective);
    }

    public void addReward(String reward) {
        rewards.add(reward);
    }

    public void complete() {
        this.completed = true;
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(description).append("\n");
        sb.append("Objetivos:\n");
        for (String objective : objectives) {
            sb.append("- ").append(objective).append("\n");
        }
        sb.append("Recompensas:\n");
        for (String reward : rewards) {
            sb.append("- ").append(reward).append("\n");
        }
        sb.append("Estado: ").append(completed ? "Completada" : "En progreso");
        return sb.toString();
    }
}