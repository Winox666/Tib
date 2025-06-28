public class Spell {
    private String name;
    private int manaCost;
    private int damage;
    private String effect;
    private int cooldown;
    private int currentCooldown;

    public Spell(String name, int manaCost, int damage, String effect, int cooldown) {
        this.name = name;
        this.manaCost = manaCost;
        this.damage = damage;
        this.effect = effect;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    public String cast(Player caster, Monster target) {
        if (currentCooldown > 0) {
            return name + " está en cooldown! (" + currentCooldown + " turnos restantes)";
        }
        
        if (caster.getMana() < manaCost) {
            return "No tienes suficiente maná para lanzar " + name;
        }

        caster.useMana(manaCost);
        currentCooldown = cooldown;
        
        int finalDamage = damage;
        if (effect.equals("fire")) {
            finalDamage = (int)(damage * 1.5);
        } else if (effect.equals("ice")) {
            target.setFrozen(true);
        }

        target.takeDamage(finalDamage);
        return caster.getName() + " lanza " + name + " causando " + finalDamage + " de daño!";
    }

    public void updateCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    // Getters
    public String getName() { return name; }
    public int getManaCost() { return manaCost; }
    public int getDamage() { return damage; }
    public String getEffect() { return effect; }
    public int getCooldown() { return cooldown; }
    public int getCurrentCooldown() { return currentCooldown; }
}