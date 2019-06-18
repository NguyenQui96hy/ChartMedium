package com.example.chartmightysat;

public interface OnPrintTickLabel {
    /**
     * @param tickPosition position of ticks, start from 0.
     * @param tick speed value at the tick.
     * @return label to draw.
     */
    CharSequence getTickLabel(int tickPosition, float tick);
}
