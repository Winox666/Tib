import java.io.*;
import java.nio.file.*;
import java.util.*;

public class MapLoader {
    private static final String MAP_FOLDER = "maps/";
    private static final String MAP_EXTENSION = ".otbm";

    public static World loadWorld(String mapName) throws IOException {
        Path path = Paths.get(MAP_FOLDER + mapName + MAP_EXTENSION);
        byte[] data = Files.readAllBytes(path);
        
        // Parsear datos del mapa (formato OpenTibia)
        int width = data[0] & 0xFF | (data[1] & 0xFF) << 8;
        int height = data[2] & 0xFF | (data[3] & 0xFF) << 8;
        
        World world = new World(width, height);
        
        int offset = 4;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char tile = (char) data[offset++];
                boolean protectedZone = (data[offset++] & 0x01) != 0;
                
                world.setTile(x, y, tile, protectedZone);
            }
        }
        
        return world;
    }

    public static List<String> getAvailableMaps() {
        List<String> maps = new ArrayList<>();
        File folder = new File(MAP_FOLDER);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(MAP_EXTENSION));
        
        if (files != null) {
            for (File file : files) {
                maps.add(file.getName().replace(MAP_EXTENSION, ""));
            }
        }
        
        return maps;
    }
}