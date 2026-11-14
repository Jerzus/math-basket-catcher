package com.example.mathbasketcatcher;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

// plansza z kulkami 0-9 i przyciskiem Already used
public class GameView extends View {

    public CalcMode mode;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setFakeBoldText(true);
    }

    // 4 kulki w rzędzie, w ostatnim rzędzie 2 kulki, a obok przycisk na 2 miejsca
    private float cell() {
        return Math.min(getWidth() / 4f, getHeight() / 3f);
    }

    private float ballX(int place) {
        int col = place % 4;
        return (getWidth() - 4 * cell()) / 2 + (col + 0.5f) * cell();
    }

    private float ballY(int place) {
        int row = place / 4;
        return (getHeight() - 3 * cell()) / 2 + (row + 0.5f) * cell();
    }

    private float radius() {
        return cell() * 0.44f;
    }

    // przycisk Already used zajmuje miejsca 10 i 11
    private RectF buttonRect() {
        float r = radius();
        return new RectF(ballX(10) - r, ballY(10) - r, ballX(11) + r, ballY(10) + r);
    }

    // kółko albo przycisk w tych samych kolorach
    private void drawShape(Canvas canvas, RectF rect, float r) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getContext().getColor(R.color.light_blue));
        canvas.drawRoundRect(rect, r, r, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(r / 10);
        paint.setColor(getContext().getColor(R.color.blue));
        canvas.drawRoundRect(rect, r, r, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mode == null) {
            return;
        }
        float r = radius();
        paint.setTextSize(r);
        for (int i = 0; i < 10; i++) {
            float x = ballX(i);
            float y = ballY(i);
            drawShape(canvas, new RectF(x - r, y - r, x + r, y + r), r);
            canvas.drawText("" + mode.order[i], x, y + r / 3, paint);
        }

        RectF rect = buttonRect();
        drawShape(canvas, rect, r);
        paint.setTextSize(r * 0.5f);
        canvas.drawText(getContext().getString(R.string.already_used), rect.centerX(), rect.centerY() + r / 6, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mode != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            GameActivity activity = (GameActivity) getContext();
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
