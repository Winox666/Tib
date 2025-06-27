public class Item {
    private String name;
    private String description;
    private int weight;
    private boolean canPickUp;

    public Item(String name, String description, int weight, boolean canPickUp) {
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.canPickUp = canPickUp;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getWeight() { return weight; }
    public boolean canPickUp() { return canPickUp; }

    @Override
    public String toString() {
        return name + " - " + description + " (Peso: " + weight + ")";
    }
}