package com.example.mathbasketcatcher;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String[] modes = getResources().getStringArray(R.array.game_modes);
        String[] levels = getResources().getStringArray(R.array.difficulty_levels);

        Button btnGameMode = findViewById(R.id.btnGameMode);
        btnGameMode.setText(getString(R.string.game_mode_value, modes[mode]));
        btnGameMode.setOnClickListener(v -> {
            // okno na cały ekran
            Dialog dialog = new Dialog(this, R.style.Theme_MathBasketCatcher);
            dialog.setContentView(R.layout.dialog_game_mode);
            int[] ids = {R.id.modeBaskets, R.id.modeFlying, R.id.modeCalc};
            for (int i = 0; i < ids.length; i++) {
                int which = i;
                dialog.findViewById(ids[i]).setOnClickListener(view -> {
                    mode = which;
                    btnGameMode.setText(getString(R.string.game_mode_value, modes[mode]));
                    dialog.dismiss();
                });
            }
            dialog.findViewById(R.id.btnBack).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
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