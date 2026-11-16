package com.example.mathbasketcatcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.graphics.ColorUtils;

import com.example.mathbasketcatcher.modes.FillTheGapsMode;
import com.example.mathbasketcatcher.modes.GameMode;

// plansza z kulkami 0-9 i przyciskiem Already used, w Fill the gaps nad nimi działania z luką
public class GameView extends View {

    public GameMode mode;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
    }

    // rząd działań ma odstęp od rzędu kulek
    private float topSpace() {
        return mode instanceof FillTheGapsMode ? 1.5f : 0;
    }

    // 4 kulki w rzędzie
    private float cell() {
        return Math.min(getWidth() / 4f, getHeight() / (3 + topSpace()));
    }

    private float ballX(int place) {
        int col = place % 4;
        return (getWidth() - 4 * cell()) / 2 + (col + 0.5f) * cell();
    }

    private float ballY(int place) {
        int row = place / 4;
        return (getHeight() - (3 + topSpace()) * cell()) / 2 + (topSpace() + row + 0.5f) * cell();
    }

    private float radius() {
        return cell() * 0.44f;
    }

    // przycisk Already used zajmuje miejsca 10 i 11
    private RectF buttonRect() {
        float r = radius();
        return new RectF(ballX(10) - r, ballY(10) - r, ballX(11) + r, ballY(10) + r);
    }

    // działanie zajmuje 2 miejsca w górnym rzędzie
    private RectF equationRect(int i) {
        float r = radius();
        float y = ballY(0) - topSpace() * cell();
        return new RectF(ballX(2 * i) - r, y - r, ballX(2 * i + 1) + r, y + r);
    }

    // kółko albo przycisk, zaznaczony jest ciemniejszy, ma grubszą ramkę i większy tekst z cieniem
    private void drawShape(Canvas canvas, RectF rect, float r, String text, float textSize, boolean selected, int border) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getContext().getColor(selected ? R.color.light_blue_selected : R.color.light_blue));
        canvas.drawRoundRect(rect, r, r, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(selected ? r / 6 : r / 10);
        int color = getContext().getColor(border);
        if (selected) {
            color = ColorUtils.blendARGB(color, Color.BLACK, 0.25f);
        }
        paint.setColor(color);
        canvas.drawRoundRect(rect, r, r, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        if (selected) {
            textSize *= 1.2f;
            paint.setShadowLayer(r / 8, r / 16, r / 16, Color.GRAY);
        }
        paint.setTextSize(textSize);
        canvas.drawText(text, rect.centerX(), rect.centerY() + textSize / 3, paint);
        paint.clearShadowLayer();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mode == null) {
            return;
        }
        FillTheGapsMode gaps = null;
        int selectedBall = -1;
        if (mode instanceof FillTheGapsMode) {
            gaps = (FillTheGapsMode) mode;
            selectedBall = gaps.selectedBall;
        }

        float r = radius();
        for (int i = 0; i < 10; i++) {
            float x = ballX(i);
            float y = ballY(i);
            drawShape(canvas, new RectF(x - r, y - r, x + r, y + r), r, "" + mode.order[i], r, mode.order[i] == selectedBall, R.color.blue);
        }
        drawShape(canvas, buttonRect(), r, getContext().getString(R.string.already_used), r * 0.5f,
                selectedBall == FillTheGapsMode.ALREADY_USED, R.color.blue);

        if (gaps != null) {
            for (int i = 0; i < gaps.equations.length; i++) {
                drawShape(canvas, equationRect(i), r, gaps.equations[i].withGap("?"), r * 0.5f, i == gaps.selectedEquation, R.color.purple);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            GameActivity activity = (GameActivity) getContext();
            if (mode instanceof FillTheGapsMode) {
                for (int i = 0; i < ((FillTheGapsMode) mode).equations.length; i++) {
                    if (equationRect(i).contains(event.getX(), event.getY())) {
                        activity.chooseEquation(i);
                        return true;
                    }
                }
            }
            if (buttonRect().contains(event.getX(), event.getY())) {
                activity.alreadyUsed();
                return true;
            }
            float r = radius();
            for (int i = 0; i < 10; i++) {
                float dx = event.getX() - ballX(i);
                float dy = event.getY() - ballY(i);
                if (dx * dx + dy * dy <= r * r) {
                    activity.chooseBall(mode.order[i]);
                    break;
                }
            }
        }
        return true;
    }
}
