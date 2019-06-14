package com.example.customviewchart.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.example.customviewchart.R;

public class SpeedView extends Speedometer {

    private Path markPath = new Path();
    private Paint
            markPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            chartBackGroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            distanceMeasurePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF chartMeasureRect = new RectF();
    private RectF positionRect = new RectF();
    private String nameChart;
    private Integer iconChart;
    private float chartBackGroundWidth;
    private int chartBackGroundColor;
    private int colorContentChart;
    private float mediumPositionStart;
    private float mediumPositionEnd;

    public SpeedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttributeSet(context, attrs);
    }

    @Override
    protected void defaultGaugeValues() {
    }

    @Override
    protected void defaultSpeedometerValues() {
        super.setIndicator(new NormalIndicator(getContext()));
        super.setBackgroundCircleColor(0);
    }

    private void init() {
        markPaint.setStyle(Paint.Style.STROKE);
        chartBackGroundPaint.setStyle(Paint.Style.STROKE);
        distanceMeasurePaint.setStyle(Paint.Style.STROKE);

    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SpeedView, 0, 0);
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        updateBackgroundBitmap();
    }

    private void initDraw() {
        markPaint.setColor(getColorContentChart());
        chartBackGroundPaint.setStrokeWidth(getChartBackGroundWidth());
        distanceMeasurePaint.setStrokeWidth(getChartBackGroundWidth() * .2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        drawImage(canvas);
        drawTextCentered(canvas);

        drawNotes(canvas);
    }

    @Override
    protected void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        initDraw();
        float markH = getViewSizePa()/28f;
        markPath.reset();



        float risk = getChartBackGroundWidth() * .5f + getPadding();
        chartMeasureRect.set(risk, risk, getSize() - risk, getSize() - risk);
        positionRect.set(risk, risk , getSize() - risk, getSize() - risk );

        float positionStart = getStartDegree() + (270 * getMediumPositionStart());
        float positionEnd = (getEndDegree() - getStartDegree()) * (getMediumPositionEnd() - getMediumPositionStart());
        chartBackGroundPaint.setColor(getChartBackGroundColor());
        c.drawArc(chartMeasureRect, getStartDegree(), getEndDegree() - getStartDegree(), false, chartBackGroundPaint);
        distanceMeasurePaint.setColor(getColorContentChart());
        c.drawArc(chartMeasureRect, positionStart
                , positionEnd, false, distanceMeasurePaint);
//         c.drawArc(positionRect,positionStart,2,false,distanceMeasurePaint);
//        c.drawArc(positionRect, positionStart + positionEnd, 9, false, distanceMeasurePaint);
//
//


        c.save();
        //c.rotate(90f + getStartDegree(), getSize() * .5f, getSize() * .5f);
//        float everyDegree = (getEndDegree() - getStartDegree()) * .111f;
//        for (float i = getStartDegree(); i < getEndDegree()-(2f*everyDegree); i+=everyDegree) {
//            c.rotate(everyDegree, getSize() *.5f, getSize() *.5f);
//            c.drawPath(markPath, markPaint);
//        }

        markPath.moveTo(getSize() *.5f, getPadding());
        markPath.lineTo(getSize() *.5f, markH + getPadding());
        markPaint.setStrokeWidth(markH/3f);
        c.rotate(90f + positionStart, getSize() *.5f, getSize() *.5f);
        c.drawPath(markPath, markPaint);
        c.rotate(positionEnd, getSize() *.5f, getSize() *.5f);
        c.drawPath(markPath, markPaint);
        c.restore();
        if (getTickNumber() > 0)
            drawTicks(c);
        else
            drawDefMinMaxSpeedPosition(c);
    }


    /**
     * change the color of the center circle (if exist),
     * <b>this option is not available for all Speedometers</b>.
     *
     * @param centerCircleColor new color.
     */
    public void setCenterCircleColor(int centerCircleColor) {
        if (!isAttachedToWindow())
            return;
        invalidate();
    }


    private void drawTextCentered(Canvas canvas) {
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30);
        textPaint.setColor(getColorContentChart());
        int xPos = (canvas.getWidth() / 2);
        int yPos = canvas.getHeight();
        if (!TextUtils.isEmpty(getNameChart())) {
            canvas.drawText(getNameChart(), xPos, yPos, textPaint);
        }
    }

    private void drawImage(Canvas canvas) {
        if (null != getIconChart()) {
            Bitmap canvasBitmap = BitmapFactory.decodeResource(getResources(), getIconChart()) //-->here load your image
                    .copy(Bitmap.Config.ARGB_8888, true);
            int xPos = ((canvas.getWidth() / 2) - (canvasBitmap.getWidth() / 2));
            int yPos = canvas.getHeight() - 120;
            canvas.drawBitmap(canvasBitmap, xPos, yPos, null);

        }
    }


    public Integer getIconChart() {
        return iconChart;
    }

    public void setIconChart(Integer iconChart) {
        this.iconChart = iconChart;
    }

    public String getNameChart() {
        return nameChart;
    }

    public void setNameChart(String nameChart) {
        this.nameChart = nameChart;
    }

    public float getChartBackGroundWidth() {
        return chartBackGroundWidth;
    }

    public void setChartBackGroundWidth(float chartBackGroundWidth) {
        this.chartBackGroundWidth = chartBackGroundWidth;
    }

    public int getChartBackGroundColor() {
        return chartBackGroundColor;
    }

    public void setChartBackGroundColor(int chartBackGroundColor) {
        this.chartBackGroundColor = chartBackGroundColor;
    }

    public int getColorContentChart() {
        return colorContentChart;
    }

    public void setColorContentChart(int colorContentChart) {
        this.colorContentChart = colorContentChart;
    }

    public float getMediumPositionStart() {
        return mediumPositionStart;
    }

    public void setMediumPositionStart(float mediumPositionStart) {
        this.mediumPositionStart = mediumPositionStart;
    }

    public float getMediumPositionEnd() {
        return mediumPositionEnd;
    }

    public void setMediumPositionEnd(float mediumPositionEnd) {
        this.mediumPositionEnd = mediumPositionEnd;
    }
}
