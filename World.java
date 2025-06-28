public class World {
    private int width, height;
    private char[][] map;
    private boolean[][] protectedZones;
    private boolean[][] safeZones;
    private boolean[][] pvpZones;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[width][height];
        this.protectedZones = new boolean[width][height];
        this.safeZones = new boolean[width][height];
        this.pvpZones = new boolean[width][height];
        initializeMap();
        initializeZones();
    }

    private void initializeMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = '.';
            }
        }
    }

    private void initializeZones() {
        // Zona central protegida (ciudad)
        for (int x = width/2 - 15; x < width/2 + 15; x++) {
            for (int y = height/2 - 15; y < height/2 + 15; y++) {
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    protectedZones[x][y] = true;
                    safeZones[x][y] = true;
                    map[x][y] = 'P'; // 'P' para zona protegida
                }
            }
        }

        // Zonas PvP (ejemplo: arenas)
        for (int x = 10; x < 20; x++) {
            for (int y = 10; y < 20; y++) {
                if (x < width && y < height) {
                    pvpZones[x][y] = true;
                    map[x][y] = 'A'; // 'A' para arena PvP
                }
            }
        }
    }

    public boolean isProtected(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return true;
        return protectedZones[x][y];
    }

    public boolean isSafeZone(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return true;
        return safeZones[x][y];
    }

    public boolean isPvpZone(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        return pvpZones[x][y];
    }

    public String getZoneType(int x, int y) {
        if (isProtected(x, y)) return "Ciudad (Protegida)";
        if (isPvpZone(x, y)) return "Arena PvP";
        if (isSafeZone(x, y)) return "Zona Segura";
        return "Territorio Peligroso";
    }

    public String getMapAroundPlayer(Player player) {
        StringBuilder sb = new StringBuilder();
        int playerX = player.getX();
        int playerY = player.getY();

        sb.append("Ubicación: ").append(getZoneType(playerX, playerY)).append("\n\n");

        for (int y = playerY - 5; y <= playerY + 5; y++) {
            for (int x = playerX - 5; x <= playerX + 5; x++) {
                if (x == playerX && y == playerY) {
                    sb.append('@');
                } else if (x >= 0 && x < width && y >= 0 && y < height) {
                    sb.append(map[x][y]);
                } else {
                    sb.append('#');
                }
                sb.append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public boolean movePlayer(Player player, String direction) {
        int newX = player.getX();
        int newY = player.getY();

        switch(direction.toLowerCase()) {
            case "norte": newY--; break;
            case "sur": newY++; break;
            case "este": newX++; break;
            case "oeste": newX--; break;
            default: return false;
        }

        if (newX < 0 || newX >= width || newY < 0 || newY >= height) {
            return false;
        }

        player.setPosition(newX, newY);
        return true;
    }
}