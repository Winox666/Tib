package com.example.tibiaclientserver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText etCommand;
    private Button btnSend;
    private TextView tvGameStatus, tvMap;
    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCommand = findViewById(R.id.etCommand);
        btnSend = findViewById(R.id.btnSend);
        tvGameStatus = findViewById(R.id.tvGameStatus);
        tvMap = findViewById(R.id.tvMap);

        // Conexión inicial al servidor para registro
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
            try (Socket socket = new Socket("10.0.2.2", 8080);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Recibir mensaje de bienvenida
                String welcomeMsg = in.readLine();
                
                // Enviar nombre de jugador
                playerName = "JugadorAndroid";
                out.println(playerName);
                
                // Recibir confirmación
                return in.readLine();
                
            } catch (IOException e) {
                return "Error de conexión: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            tvGameStatus.setText(result);
        }
    }

    private class GameCommandTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... commands) {
            try (Socket socket = new Socket("10.0.2.2", 8080);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(commands[0]);
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line).append("\n");
                }
                return response.toString();
                
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith(".") || result.startsWith("#") || result.startsWith("@")) {
                tvMap.setText(result);
            } else {
                tvGameStatus.setText(result);
            }
        }
    }
}