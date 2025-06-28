public class Monster {
    private String name;
    private int x, y;
    private int attack;
    private int defense;
    private int health;
    private String specialAbility;
    private boolean isAggressive;

    public Monster(String name, int x, int y, int attack, int defense, int health, String specialAbility, boolean isAggressive) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.attack = attack;
        this.defense = defense;
        this.health = health;
        this.specialAbility = specialAbility;
        this.isAggressive = isAggressive;
    }

    // Getters y setters
    public String getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public int getHealth() { return health; }
    public String getSpecialAbility() { return specialAbility; }
    public boolean isAggressive() { return isAggressive; }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0;
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public String attack(Player player) {
        if (!isAggressive) return name + " no es agresivo";
        
        int damage = Math.max(1, attack - player.getDefense());
        player.takeDamage(damage);
        return name + " ataca causando " + damage + " de daño!";
    }

    @Override
    public String toString() {
        return name + " (Salud: " + health + ", Ataque: " + attack + 
               ", Defensa: " + defense + ", Habilidad: " + specialAbility + 
               ", " + (isAggressive ? "Agresivo" : "Pasivo") + ")";
    }
}