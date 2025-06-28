import java.util.List;
import java.util.Random;

public class Monster {
    public enum AIBehavior {
        PASSIVE, AGGRESSIVE, DEFENSIVE, FLEEING
    }

    private String name;
    private int x, y;
    private int attack;
    private int defense;
    private int health;
    private int maxHealth;
    private String specialAbility;
    private AIBehavior behavior;
    private int detectionRange;
    private Player currentTarget;
    private Random random;

    public Monster(String name, int x, int y, int attack, int defense, int health, 
                  String specialAbility, AIBehavior behavior, int detectionRange) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.maxHealth = health;
        this.specialAbility = specialAbility;
        this.behavior = behavior;
        this.detectionRange = detectionRange;
        this.random = new Random();
    }

    public void update(List<Player> players) {
        switch(behavior) {
            case AGGRESSIVE:
                updateAggressive(players);
                break;
            case DEFENSIVE:
                updateDefensive(players);
                break;
            case FLEEING:
                updateFleeing(players);
                break;
            default:
                // PASSIVE no hace nada
                break;
        }
    }

    private void updateAggressive(List<Player> players) {
        // Buscar jugador más cercano
        Player closest = findClosestPlayer(players);
        
        if (closest != null && isInRange(closest)) {
            currentTarget = closest;
            moveTowards(closest);
        }
    }

    private void updateDefensive(List<Player> players) {
        if (health < maxHealth * 0.3) {
            behavior = AIBehavior.FLEEING;
            updateFleeing(players);
        } else {
            updateAggressive(players);
        }
    }

    private void updateFleeing(List<Player> players) {
        Player closest = findClosestPlayer(players);
        if (closest != null) {
            moveAwayFrom(closest);
        }
    }

    private Player findClosestPlayer(List<Player> players) {
        Player closest = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Player player : players) {
            double distance = Math.sqrt(Math.pow(player.getX() - x, 2) + Math.pow(player.getY() - y, 2));
            if (distance < minDistance && distance <= detectionRange) {
                minDistance = distance;
                closest = player;
            }
        }
        
        return closest;
    }

    private boolean isInRange(Player player) {
        return Math.abs(player.getX() - x) <= detectionRange && 
               Math.abs(player.getY() - y) <= detectionRange;
    }

    private void moveTowards(Player player) {
        // Lógica de movimiento hacia el jugador
        if (player.getX() > x) x++;
        else if (player.getX() < x) x--;
        
        if (player.getY() > y) y++;
        else if (player.getY() < y) y--;
    }

    private void moveAwayFrom(Player player) {
        // Lógica de huida del jugador
        if (player.getX() > x) x--;
        else if (player.getX() < x) x++;
        
        if (player.getY() > y) y--;
        else if (player.getY() < y) y++;
    }

    // Resto de métodos existentes...
}