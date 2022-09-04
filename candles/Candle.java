package candles;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * The class describes the candle
 */
public class Candle implements CandleComparable{
    private double open;
    private double close;
    private double high;
    private double low;
    private double volume;
    private Date openTIme;
    private Date closeTime;
    private String candleType;
    private double candleBodySize;
    private double topShadowSize;
    private double bottomShadowSize;
    public Candle(double open, double close, double high, double low, double volume, long openTIme, long closeTime) {
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.openTIme = new Date(new Timestamp(openTIme).getTime());
        this.closeTime = new Date(new Timestamp(closeTime).getTime());
        this.candleType = candleTypeCalc();
        this.candleBodySize = Math.abs(this.close - this.open);
        this.topShadowSize = Math.abs(this.high - this.close);
        this.bottomShadowSize = Math.abs(this.open - this.low);
    }


    public Candle(BigDecimal open, BigDecimal close, BigDecimal high, BigDecimal low, BigDecimal volume, long openTIme, long closeTime) {
        this.open = open.doubleValue();
        this.close = close.doubleValue();
        this.high = high.doubleValue();
        this.low = low.doubleValue();
        this.volume = volume.doubleValue();
        this.openTIme = new Date(new Timestamp(openTIme).getTime());
        this.closeTime = new Date(new Timestamp(closeTime).getTime());
        this.candleType = candleTypeCalc();
        this.candleBodySize = Math.abs(this.close - this.open);
        this.topShadowSize = Math.abs(this.high - this.close);
        this.bottomShadowSize = Math.abs(this.open - this.low);
    }



    /**
     * Calculate candle type
     * bear/bull
     *
     * @return candle type result
     */
    private String candleTypeCalc() {
        String result = "";

        if (this.close > this.open) {
            result = "bull";
        } else if (this.close < this.open) {
            result = "bear";
        } else {
            result = "doji";
        }

        return result;
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


    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getVolume() {
        return volume;
    }

    public Date getOpenTIme() {
        return openTIme;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public String getCandleType() {
        return candleType;
    }

    public double getCandleBodySize() {
        return candleBodySize;
    }

    public double getTopShadowSize() {
        return topShadowSize;
    }

    public double getBottomShadowSize() {
        return bottomShadowSize;
    }


    /**
     * Comparison of one candle with another to check the condition
     * @param candle class object Candle
     * @return boolean true/false
     */
    @Override
    public boolean compare(Candle candle) {
        boolean result = false;

        if (this.candleType.equals("bull") && candle.candleType.equals("bear")
            && (this.candleBodySize > candle.candleBodySize && this.candleBodySize < candle.candleBodySize * 20)
            && this.volume >= candle.volume*2 && this.topShadowSize < this.candleBodySize && this.close > candle.open && candle.open != candle.close)

            result = true;

        return result;
    }
}
