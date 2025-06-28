import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Guild {
    public enum Rank {
        LEADER, VICE_LEADER, MEMBER, RECRUIT
    }

    private String name;
    private String description;
    private Player leader;
    private Map<Player, Rank> members;
    private List<String> announcements;
    private int level;
    private int experience;

    public Guild(String name, String description, Player leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.members = new HashMap<>();
        this.announcements = new ArrayList<>();
        this.level = 1;
        this.experience = 0;
        
        members.put(leader, Rank.LEADER);
    }

    public void addMember(Player player, Rank rank) {
        members.put(player, rank);
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public void promoteMember(Player player) {
        Rank current = members.get(player);
        if (current != null && current != Rank.LEADER) {
            members.put(player, Rank.values()[current.ordinal() - 1]);
        }
    }

    public void demoteMember(Player player) {
        Rank current = members.get(player);
        if (current != null && current != Rank.RECRUIT) {
            members.put(player, Rank.values()[current.ordinal() + 1]);
        }
    }

    public void addAnnouncement(String message) {
        announcements.add(0, message); // Agregar al inicio
        if (announcements.size() > 10) {
            announcements.remove(announcements.size() - 1);
        }
    }

    public void addExperience(int amount) {
        experience += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredExp = level * 1000;
        if (experience >= requiredExp) {
            level++;
            experience -= requiredExp;
            addAnnouncement("¡El clan ha subido al nivel " + level + "!");
        }
    }

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Clan: ").append(name).append("\n");
        sb.append("Nivel: ").append(level).append("\n");
        sb.append("Descripción: ").append(description).append("\n");
        sb.append("Líder: ").append(leader.getName()).append("\n");
        sb.append("Miembros: ").append(members.size()).append("\n");
        return sb.toString();
    }
}