package com.example.mathbasketcatcher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SettingsActivity extends AppCompatActivity {

    private int mode = 0;
    private int difficulty = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] modes = getResources().getStringArray(R.array.game_modes);
        String[] descriptions = getResources().getStringArray(R.array.game_mode_descriptions);
        String[] levels = getResources().getStringArray(R.array.difficulty_levels);

        String[] items = new String[modes.length];
        for (int i = 0; i < modes.length; i++) {
            items[i] = modes[i] + "\n" + descriptions[i];
        }

        Button btnGameMode = findViewById(R.id.btnGameMode);
        btnGameMode.setText(getString(R.string.game_mode_value, modes[mode]));
        btnGameMode.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.game_mode)
                    .setSingleChoiceItems(items, mode, (dialog, which) -> {
                        mode = which;
                        btnGameMode.setText(getString(R.string.game_mode_value, modes[mode]));
                        dialog.dismiss();
                    })
                    .show();
        });

        Button btnDifficulty = findViewById(R.id.btnDifficulty);
        btnDifficulty.setText(getString(R.string.difficulty_value, levels[difficulty]));
        btnDifficulty.setOnClickListener(v -> {
            difficulty = (difficulty + 1) % levels.length;
            btnDifficulty.setText(getString(R.string.difficulty_value, levels[difficulty]));
        });

        Button btnAdvancedSettings = findViewById(R.id.btnAdvancedSettings);
        btnAdvancedSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdvancedSettingsActivity.class);
            startActivity(intent);
        });

        Button btnBackToMenu = findViewById(R.id.btnBackToMenu);
        btnBackToMenu.setOnClickListener(v -> finish());
    }
}