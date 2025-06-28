package com.example.tibiaclientserver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    private TextView tvGameStatus, tvMap, tvQuests, tvGuildInfo;
    private EditText etCommand;
    private Button btnSend;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Player player;
    private World gameWorld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        tvGameStatus = findViewById(R.id.tvGameStatus);
        tvMap = findViewById(R.id.tvMap);
        tvQuests = findViewById(R.id.tvQuests);
        tvGuildInfo = findViewById(R.id.tvGuildInfo);
        etCommand = findViewById(R.id.etCommand);
        btnSend = findViewById(R.id.btnSend);

        // Conectar al servidor
        new ConnectTask().execute();

        btnSend.setOnClickListener(v -> {
            String command = etCommand.getText().toString();
            new GameCommandTask().execute(command);
            etCommand.setText("");
        });
    }

    private class ConnectTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                socket = new Socket("10.0.2.2", 7171);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                // Recibir datos iniciales del jugador
                String playerData = in.readLine();
                player = Player.fromString(playerData);
                
                // Recibir mapa inicial
                String mapData = in.readLine();
                gameWorld = World.fromString(mapData);
                
                return "Conectado al servidor";
            } catch (IOException e) {
                return "Error de conexión: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            tvGameStatus.setText(result);
            updateGameView();
        }
    }

    private class GameCommandTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... commands) {
            try {
                out.println(commands[0]);
                return in.readLine();
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            tvGameStatus.setText(result);
            updateGameView();
        }
    }

    private void updateGameView() {
        // Actualizar vista del mapa
        tvMap.setText(gameWorld.getMapAroundPlayer(player));
        
        // Actualizar estado del jugador
        tvGameStatus.setText(player.getStatus());
        
        // Actualizar misiones
        tvQuests.setText(player.getQuestsStatus());
        
        // Actualizar información del clan
        if (player.getGuild() != null) {
            tvGuildInfo.setText(player.getGuild().getInfo());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}