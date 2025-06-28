import java.util.ArrayList;
import java.util.List;

public class Quest {
    public enum QuestType {
        HUNTING, GATHERING, EXPLORATION, DELIVERY
    }

    private String id;
    private String name;
    private String description;
    private QuestType type;
    private boolean completed;
    private List<String> objectives;
    private List<Item> rewards;
    private int experienceReward;
    private int goldReward;

    public Quest(String id, String name, String description, QuestType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.completed = false;
        this.objectives = new ArrayList<>();
        this.rewards = new ArrayList<>();
        this.experienceReward = 0;
        this.goldReward = 0;
    }

    public void addObjective(String objective) {
        objectives.add(objective);
    }

    public void addReward(Item item) {
        rewards.add(item);
    }

    public void setExperienceReward(int experience) {
        this.experienceReward = experience;
    }

    public void setGoldReward(int gold) {
        this.goldReward = gold;
    }

    public void complete(Player player) {
        this.completed = true;
        giveRewards(player);
    }

    private void giveRewards(Player player) {
        player.addExperience(experienceReward);
        player.addGold(goldReward);
        for (Item item : rewards) {
            player.getInventory().addItem(item, 1);
        }
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" (").append(type).append(")\n");
        sb.append(description).append("\n");
        for (String objective : objectives) {
            sb.append("- ").append(objective).append("\n");
        }
        sb.append("Recompensas:\n");
        sb.append("- Experiencia: ").append(experienceReward).append("\n");
        sb.append("- Oro: ").append(goldReward).append("\n");
        for (Item item : rewards) {
            sb.append("- ").append(item.getName()).append("\n");
        }
        sb.append("Estado: ").append(completed ? "Completada" : "En progreso");
        return sb.toString();
    }
}