package com.abir.cameraabir.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.abir.cameraabir.R;

public class RectangleOverlayView extends LinearLayout {
    private Bitmap bitmap;

    public RectangleOverlayView(Context context) {
        super(context);
    }

    public RectangleOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleOverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RectangleOverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            createWindowFrame();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    protected void createWindowFrame() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.app_hint1));
        paint.setAlpha(99);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        //float centerX = getWidth() / 2;
        //float centerY = getHeight() / 2;
        //float radius = getResources().getDimensionPixelSize(R.dimen.radius);
        //osCanvas.drawCircle(centerX, centerY, radius, paint);

        int height = getHeight();
        int width = getWidth();
        int left, right, top, bottom, diameter;
        diameter = getWidth();
        if (height < width) {
            diameter = height;
        }

        int offset = (int) (0.05 * diameter);
        diameter -= offset;

        left = width / 2 - diameter / 3;
        top = height / 2 - diameter / 3;
        right = width / 2 + diameter / 3;
        bottom = height / 2 + diameter / 3;
        //Changing the value of x in diameter/x will change the size of the box ; inversely proportionate to x
        osCanvas.drawRect(left, top, right, bottom, paint);
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
    }
}