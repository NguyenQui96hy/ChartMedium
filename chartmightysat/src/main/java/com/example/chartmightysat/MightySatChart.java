package com.example.chartmightysat;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;

public class MightySatChart extends Speedometer {

    private Path markPath = new Path();
    private Paint
            markPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            chartBackGroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG),
            distanceMeasurePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private RectF chartMeasureRect = new RectF();
    private RectF positionRect = new RectF();
    private String nameChart;
    private Integer iconChart;
    private int chartBackGroundColor;
    private int colorContentChart;
    private float mediumPositionStart;
    private float mediumPositionEnd;
    private ValueAnimator mAnimator;
    private int colorPaddingChart;
    private float widthPaddingChart;
    private float paddingChart = 50;


    public MightySatChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MightySatChart(Context context, AttributeSet attrs, int defStyleAttr) {
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
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MightySatChart, 0, 0);
        nameChart = a.getString(R.styleable.MightySatChart_name_chart);
        colorPaddingChart = a.getColor(R.styleable.MightySatChart_color_padding_chart, Color.WHITE);
        colorContentChart =a.getColor(R.styleable.MightySatChart_color_content_chart,Color.WHITE);
        widthPaddingChart = a.getFloat(R.styleable.MightySatChart_width_padding_chart,0);


        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);

        updateBackgroundBitmap();
    }

    private void initDraw() {
        markPaint.setColor(getColorContentChart());
        chartBackGroundPaint.setStrokeWidth(getWidthPaddingChart());
        distanceMeasurePaint.setStrokeWidth(getWidthPaddingChart() * .2f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSpeedUnitText(canvas);
        drawIndicator(canvas);
        drawImage(canvas);
        drawTextCentered(canvas);

        drawNotes(canvas);
        drawLimit(canvas);
    }

    @Override
    protected void updateBackgroundBitmap() {
        Canvas c = createBackgroundBitmapCanvas();
        initDraw();
        float risk = getWidthPaddingChart() * .5f + getPadding();
        chartMeasureRect.set(risk+paddingChart, risk+paddingChart, getSize() - risk-paddingChart, getSize() - risk-paddingChart);
        positionRect.set(risk, risk, getSize() - risk, getSize() - risk);
        chartBackGroundPaint.setColor(getColorPaddingChart());
        c.drawArc(chartMeasureRect, getStartDegree(), getEndDegree() - getStartDegree(), false, chartBackGroundPaint);
        c.save();
        c.restore();
        if (getTickNumber() > 0)
            drawTicks(c);
        else
            drawDefMinMaxSpeedPosition(c);
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

    public void drawLimit(Canvas c) {
        float positionStart = getMediumPositionStart() / getMaxSpeed();
        float positionEnd = (getMediumPositionEnd() - getMediumPositionStart()) / getMaxSpeed();
        float angleStart = getStartDegree() + (270 * positionStart);
        float angleEnd = (getEndDegree() - getStartDegree()) * positionEnd;
        float markH = getViewSizePa() / 28f;
        markPath.reset();
        distanceMeasurePaint.setColor(getColorContentChart());
        c.drawArc(chartMeasureRect, angleStart
                , angleEnd, false, distanceMeasurePaint);
        markPath.moveTo(getSize() * .5f, getPadding()+paddingChart);
        markPath.lineTo(getSize() * .5f, markH + getPadding()+paddingChart);
        markPaint.setStrokeWidth(markH / 3f);

        c.rotate(90f + angleStart, getSize() * .5f, getSize() * .5f);
        c.drawPath(markPath, markPaint);
        c.drawText((int)getMediumPositionStart()+"",getSize()*.5f,markH+paddingChart*.5f,textPaint);
        c.rotate(angleEnd, getSize() * .5f, getSize() * .5f);
        c.drawPath(markPath, markPaint);
        c.drawText((int)getMediumPositionEnd()+"",getSize()*.5f  ,markH+paddingChart*.5f ,textPaint);
        c.save();
//        c.restore();

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
        if (!isAttachedToWindow())
            return;
        invalidate();
    }

    public float getMediumPositionEnd() {
        return mediumPositionEnd;
    }

    public void setMediumPositionEnd(float mediumPositionEnd) {
        this.mediumPositionEnd = mediumPositionEnd;
        if (!isAttachedToWindow())
            return;
        invalidate();
    }


    public int getColorPaddingChart() {
        return colorPaddingChart;
    }

    public void setColorPaddingChart(int colorPaddingChart) {
        this.colorPaddingChart = colorPaddingChart;
    }
    public float getWidthPaddingChart() {
        return widthPaddingChart;
    }

    public void setWidthPaddingChart(float widthPaddingChart) {
        this.widthPaddingChart = widthPaddingChart;
    }
}
