public class Quest {
    private String id;
    private String name;
    private String description;
    private boolean completed;
    private List<String> objectives;

    public Quest(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.completed = false;
        this.objectives = new ArrayList<>();
    }

    public void addObjective(String objective) {
        objectives.add(objective);
    }

    public void complete() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(": ").append(description).append("\n");
        for (String objective : objectives) {
            sb.append("- ").append(objective).append("\n");
        }
        sb.append("Estado: ").append(completed ? "Completada" : "En progreso");
        return sb.toString();
    }
}