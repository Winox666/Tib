import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static HashMap<String, Player> players = new HashMap<>();
    private static World world = new World(100, 100);
    private static List<NPC> npcs = new ArrayList<>();
    private static List<Monster> monsters = new ArrayList<>();
    private static List<Item> worldItems = new ArrayList<>();

    public static void main(String[] args) {
        initializeGameWorld();
        
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor Tibia mejorado iniciado en puerto 8080");
            System.out.println("Características implementadas:");
            System.out.println("- Sistema de combate");
            System.out.println("- NPCs interactivos");
            System.out.println("- Sistema de objetos e inventario");
            System.out.println("- Gráficos mejorados");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeGameWorld() {
        initializeNPCs();
        initializeMonsters();
        initializeItems();
    }

    private static void initializeItems() {
        worldItems.add(new Item("Espada corta", "Una espada básica para principiantes", 30, true));
        worldItems.add(new Item("Poción de salud", "Restaura 50 puntos de salud", 10, true));
    }

    // ... (Resto de las implementaciones existentes)
}