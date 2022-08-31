package candles;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * The class describes the candle
 */
public class Candle {
    private double open;
    private double close;
    private double high;
    private double low;
    private double volume;
    private long openTIme;
    private long closeTime;

    public Candle(double open, double close, double high, double low, double volume, long openTIme, long closeTime) {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.openTIme = openTIme;
        this.closeTime = closeTime;
    }


    public Candle(BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low, BigDecimal volume, long openTIme, long closeTime) {
        this.open = open.doubleValue();
        this.close = close.doubleValue();
        this.high = high.doubleValue();
        this.low = low.doubleValue();
        this.volume = volume.doubleValue();
        this.openTIme = openTIme;
        this.closeTime = closeTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Candle candle = (Candle) o;
        return Double.compare(candle.open, open) == 0 && Double.compare(candle.close, close) == 0 && Double.compare(candle.high, high) == 0 && Double.compare(candle.low, low) == 0 && Double.compare(candle.volume, volume) == 0 && openTIme == candle.openTIme && closeTime == candle.closeTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(open, close, high, low, volume, openTIme, closeTime);
    }

    @Override
    public String toString() {
        return "Candle{" +
                "open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", openTIme=" + openTIme +
                ", closeTime=" + closeTime +
                '}';
    }
}
