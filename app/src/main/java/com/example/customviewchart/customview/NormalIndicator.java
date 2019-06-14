package com.example.customviewchart.customview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;

public class NormalIndicator extends Indicator<NormalIndicator> {

    private Path indicatorPath = new Path();
    private float bottomY;

    public NormalIndicator(Context context) {
        super(context);
        updateIndicator();
    }

    @Override
    protected float getDefaultIndicatorWidth() {
        return dpTOpx(12f);
    }

    @Override
    public float getBottom() {
        return bottomY;
    }

    @Override
    public void draw(Canvas canvas, float degree) {
        canvas.save();
        canvas.rotate(90f + degree, getCenterX(), getCenterY());
        canvas.drawPath(indicatorPath, indicatorPaint);
        canvas.restore();
    }

    @Override
    protected void updateIndicator() {
        indicatorPath.reset();
        indicatorPath.moveTo(getCenterX(), getPadding());
        bottomY = getViewSize()*1f/13f + getPadding();
        indicatorPath.lineTo(getCenterX() - getIndicatorWidth(), bottomY);
        indicatorPath.lineTo(getCenterX() + getIndicatorWidth(), bottomY);


    }

    @Override
    protected void setWithEffects(boolean withEffects) {
        if (withEffects && !isInEditMode()) {
            indicatorPaint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.SOLID));
        }
        else {
            indicatorPaint.setMaskFilter(null);
        }
    }
}
