import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {
    private static final int PORT = 8080;
    private static final int MAX_PLAYERS = 1000;
    private static final int THREAD_POOL_SIZE = 200;
    
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private Map<String, Player> players;
    private ScheduledExecutorService scheduler;

    public Server() {
        this.players = new ConcurrentHashMap<>();
        this.threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.scheduler = Executors.newScheduledThreadPool(4);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor Tibia optimizado iniciado en puerto " + PORT);
            
            // Tarea programada para limpieza periódica
            scheduler.scheduleAtFixedRate(this::cleanup, 1, 1, TimeUnit.MINUTES);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.execute(new ClientHandler(clientSocket, this));
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        } finally {
            shutdown();
        }
    }

    private void cleanup() {
        // Limpiar jugadores desconectados
        players.values().removeIf(player -> !player.isConnected());
    }

    public void shutdown() {
        try {
            if (serverSocket != null) serverSocket.close();
            threadPool.shutdown();
            scheduler.shutdown();
            saveGameState();
        } catch (IOException e) {
            System.err.println("Error al cerrar el servidor: " + e.getMessage());
        }
    }

    private void saveGameState() {
        // Implementar lógica de guardado
    }

    public void addPlayer(Player player) {
        players.put(player.getName(), player);
    }

    public void removePlayer(String playerName) {
        players.remove(playerName);
    }

    public Player getPlayer(String playerName) {
        return players.get(playerName);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Server server;
    private Player player;

    public ClientHandler(Socket socket, Server server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            // Autenticación del jugador
            out.println("Bienvenido al servidor Tibia. Introduce tu nombre:");
            String playerName = in.readLine();
            this.player = new Player(playerName, 100, 1, 1, clientSocket);
            server.addPlayer(player);
            
            out.println("Jugador " + playerName + " creado. Salud: " + player.getHealth());
            
            // Bucle principal del juego
            String input;
            while ((input = in.readLine()) != null) {
                String response = processCommand(input);
                out.println(response);
            }
            
        } catch (IOException e) {
            System.err.println("Error con el cliente: " + e.getMessage());
        } finally {
            if (player != null) {
                server.removePlayer(player.getName());
            }
        }
    }
    
    private String processCommand(String command) {
        // Lógica básica del juego
        if (command.equalsIgnoreCase("estado")) {
            return player.toString();
        } else if (command.startsWith("mover ")) {
            String direction = command.substring(6);
            return player.move(direction);
        } else {
            return "Comando no reconocido. Comandos disponibles: estado, mover [norte/sur/este/oeste]";
        }
    }
}

class Player {
    private String name;
    private int health;
    private int level;
    private int x, y;
    private Socket socket;
    private boolean connected;

    public Player(String name, int health, int x, int y, Socket socket) {
        this.name = name;
        this.health = health;
        this.level = 1;
        this.x = x;
        this.y = y;
        this.socket = socket;
        this.connected = true;
    }
    
    public String move(String direction) {
        switch(direction.toLowerCase()) {
            case "norte": y++; break;
            case "sur": y--; break;
            case "este": x++; break;
            case "oeste": x--; break;
            default: return "Dirección no válida";
        }
        return "Te has movido a (" + x + "," + y + ")";
    }
    
    public String getName() { return name; }
    public int getHealth() { return health; }
    public boolean isConnected() { return connected; }

    public void disconnect() {
        this.connected = false;
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Error al desconectar al jugador: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Jugador: " + name + "\nNivel: " + level + "\nSalud: " + health + "\nPosición: (" + x + "," + y + ")";
    }
}