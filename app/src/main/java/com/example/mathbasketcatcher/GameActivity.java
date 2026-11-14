package com.example.mathbasketcatcher;

import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class GameActivity extends AppCompatActivity {

    private CalcMode mode;
    private GameView gameView;
    private TextView tvStats;
    private TextView tvExpression;
    private TextView tvLastMove;
    private Chronometer chTime;
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        gameView = findViewById(R.id.gameView);
        tvStats = findViewById(R.id.tvStats);
        tvExpression = findViewById(R.id.tvExpression);
        tvLastMove = findViewById(R.id.tvLastMove);
        chTime = findViewById(R.id.chTime);

        Button btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(v -> pause());

        // przycisk cofnięcia zatrzymuje grę
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                pause();
            }
        });

        mode = new CalcMode();
        gameView.mode = mode;
        chTime.setBase(SystemClock.elapsedRealtime());
        chTime.start();
        refresh();
    }

    // wyjście z aplikacji zatrzymuje grę
    @Override
    protected void onPause() {
        super.onPause();
        if (!paused && !mode.isOver() && !isFinishing()) {
            pause();
        }
    }

    // zatrzymanie gry
    private void pause() {
        paused = true;
        chTime.stop();
        long time = SystemClock.elapsedRealtime() - chTime.getBase();   // czas rundy w ms
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.paused)
                .setCancelable(false)
                .setPositiveButton(R.string.resume, (dialog, which) -> {
                    paused = false;
                    chTime.setBase(SystemClock.elapsedRealtime() - time);
                    chTime.start();
                })
                .setNegativeButton(R.string.back_to_menu, (dialog, which) -> finish())
                .show();
    }

    public void chooseBall(int number) {
        mode.choose(number);
        refresh();
    }

    public void alreadyUsed() {
        mode.alreadyUsed();
        refresh();
    }

    private void refresh() {
        tvStats.setText(getString(R.string.score_errors, mode.score, mode.errors));
        if (mode.isOver()) {
            tvExpression.setText("");
        } else {
            tvExpression.setText(mode.expression + " = ?");
        }
        tvLastMove.setText(mode.lastMove);
        if (mode.lastGood) {
            tvLastMove.setTextColor(getColor(R.color.blue));
        } else {
            tvLastMove.setTextColor(getColor(R.color.orange));
        }
        gameView.invalidate();

        if (mode.isOver()) {
            endRound();
        }
    }

    private void endRound() {
        chTime.stop();
        long seconds = (SystemClock.elapsedRealtime() - chTime.getBase()) / 1000;
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.round_over)
                .setMessage(getString(R.string.round_result, mode.score, mode.errors, seconds))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> finish())
                .show();
    }
}
