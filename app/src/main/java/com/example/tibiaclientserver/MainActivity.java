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
import java.util.concurrent.*;

public class MainActivity extends AppCompatActivity {
    private TextView tvGameStatus, tvMap;
    private EditText etCommand;
    private Button btnSend;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private ExecutorService executor;
    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvGameStatus = findViewById(R.id.tvGameStatus);
        tvMap = findViewById(R.id.tvMap);
        etCommand = findViewById(R.id.etCommand);
        btnSend = findViewById(R.id.btnSend);

        // Configurar el executor para manejar conexiones
        executor = Executors.newFixedThreadPool(4);

        // Conectar al servidor
        new ConnectTask().execute();

        btnSend.setOnClickListener(v -> {
            String command = etCommand.getText().toString();
            executor.execute(new GameCommandTask(command));
            etCommand.setText("");
        });
    }

    private class ConnectTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                socket = new Socket("10.0.2.2", 8080);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
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

    private class GameCommandTask implements Runnable {
        private String command;

        public GameCommandTask(String command) {
            this.command = command;
        }

        @Override
        public void run() {
            try {
                out.println(command);
                String response = in.readLine();
                runOnUiThread(() -> {
                    if (response.startsWith(".") || response.startsWith("#") || response.startsWith("@")) {
                        tvMap.setText(response);
                    } else {
                        tvGameStatus.setText(response);
                    }
                });
            } catch (IOException e) {
                runOnUiThread(() -> tvGameStatus.setText("Error: " + e.getMessage()));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        try {
            if (socket != null) socket.close();
            if (out != null) out.close();
            if (in != null) in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}