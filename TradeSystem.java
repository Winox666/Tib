import java.util.HashMap;
import java.util.Map;

public class TradeSystem {
    private Player player1;
    private Player player2;
    private Map<Item, Integer> player1Offer;
    private Map<Item, Integer> player2Offer;
    private boolean player1Accepted;
    private boolean player2Accepted;

    public TradeSystem(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Offer = new HashMap<>();
        this.player2Offer = new HashMap<>();
        this.player1Accepted = false;
        this.player2Accepted = false;
    }

    public void addItem(Player player, Item item, int quantity) {
        if (player == player1) {
            player1Offer.put(item, quantity);
        } else if (player == player2) {
            player2Offer.put(item, quantity);
        }
    }

    public void acceptTrade(Player player) {
        if (player == player1) {
            player1Accepted = true;
        } else if (player == player2) {
            player2Accepted = true;
        }
        
        if (player1Accepted && player2Accepted) {
            executeTrade();
        }
    }

    private void executeTrade() {
        // Transferir items de player1 a player2
        for (Map.Entry<Item, Integer> entry : player1Offer.entrySet()) {
            player2.getInventory().addItem(entry.getKey(), entry.getValue());
            player1.getInventory().removeItem(entry.getKey(), entry.getValue());
        }

        // Transferir items de player2 a player1
        for (Map.Entry<Item, Integer> entry : player2Offer.entrySet()) {
            player1.getInventory().addItem(entry.getKey(), entry.getValue());
            player2.getInventory().removeItem(entry.getKey(), entry.getValue());
        }

        resetTrade();
    }

    private void resetTrade() {
        player1Offer.clear();
        player2Offer.clear();
        player1Accepted = false;
        player2Accepted = false;
    }
}