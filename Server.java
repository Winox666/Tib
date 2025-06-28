import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 7171;
    private static final int MAX_PLAYERS = 1000;
    
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private World gameWorld;
    private List<Player> onlinePlayers;
    private List<Guild> guilds;
    private Map<String, Quest> quests;
    private Map<String, Monster> monsters;
    private TradeSystem tradeSystem;
    
    public Server() {
        try {
            // Cargar mapa de OpenTibia
            this.gameWorld = MapLoader.loadWorld("main");
            this.threadPool = Executors.newFixedThreadPool(MAX_PLAYERS);
            this.onlinePlayers = new CopyOnWriteArrayList<>();
            this.guilds = new ArrayList<>();
            this.quests = new HashMap<>();
            this.monsters = new ConcurrentHashMap<>();
            initializeGameData();
        } catch (IOException e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            System.exit(1);
        }
    }
    
    private void initializeGameData() {
        // Inicializar misiones
        quests.put("rat_hunt", new Quest("rat_hunt", "Caza de ratas", "Elimina 10 ratas", Quest.QuestType.HUNTING));
        
        // Inicializar monstruos
        monsters.put("rat", new Monster("Rata", 0, 0, 5, 2, 20, "Mordisco", Monster.AIBehavior.AGGRESSIVE, 5));
        
        // Inicializar zonas especiales
        List<int[]> depotArea = new ArrayList<>();
        depotArea.add(new int[]{100, 100});
        depotArea.add(new int[]{101, 100});
        gameWorld.addSpecialArea("depot", depotArea);
    }
    
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor de OpenTibia iniciado en puerto " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(clientSocket, this);
                threadPool.execute(handler);
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        } finally {
            shutdown();
        }
    }
    
    public void shutdown() {
        try {
            if (serverSocket != null) serverSocket.close();
            threadPool.shutdown();
            saveGameState();
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }
    
    private void saveGameState() {
        // Implementar lógica de guardado
    }
    
    // Getters para acceso a los sistemas
    public World getGameWorld() { return gameWorld; }
    public List<Player> getOnlinePlayers() { return onlinePlayers; }
    public Map<String, Quest> getQuests() { return quests; }
    public Map<String, Monster> getMonsters() { return monsters; }
    public List<Guild> getGuilds() { return guilds; }
    
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}