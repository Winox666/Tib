import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server {
    private static HashMap<String, Player> players = new HashMap<>();
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Servidor Tibia iniciado en puerto 8080");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo jugador conectado: " + clientSocket.getInetAddress());
                
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    private Player player;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            // Autenticación del jugador
            out.println("Bienvenido al servidor Tibia. Introduce tu nombre:");
            String playerName = in.readLine();
            this.player = new Player(playerName, 100, 1, 1);
            Server.players.put(playerName, player);
            
            out.println("Jugador " + playerName + " creado. Salud: " + player.getHealth());
            
            // Bucle principal del juego
            String input;
            while ((input = in.readLine()) != null) {
                String response = processCommand(input);
                out.println(response);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (player != null) {
                Server.players.remove(player.getName());
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
    
    public Player(String name, int health, int x, int y) {
        this.name = name;
        this.health = health;
        this.level = 1;
        this.x = x;
        this.y = y;
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
    
    @Override
    public String toString() {
        return "Jugador: " + name + "\nNivel: " + level + "\nSalud: " + health + "\nPosición: (" + x + "," + y + ")";
    }
}