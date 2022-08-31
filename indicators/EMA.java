package indicators;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import candles.Candle;
import com.binance4j.core.market.CandlestickInterval;
import services.ServiceFunctions;

/**
 * Calculate ema value for last close candle
 */
public class EMA implements Indicator {
    private double currentEMA;
    private final int period;
    private final double multiplier;
    private final List<Double> EMAhistory;
    private final boolean historyNeeded;
    private String fileName;

    public EMA(List<Double> closingPrices, int period, boolean historyNeeded) {
        currentEMA = 0;
        this.period = period;
        this.historyNeeded = historyNeeded;
        this.multiplier = 2.0 / (double) (period + 1);
        this.EMAhistory = new ArrayList<>();
        init(closingPrices);
    }


    @Override
    public double get() {
        return currentEMA;
    }

    @Override
    public double getTemp(double newPrice) {
        return (newPrice - currentEMA) * multiplier + currentEMA;
    }

    @Override
    public void init(List<Double> closingPrices) {
        if (period > closingPrices.size()) return;

        //Initial SMA
        for (int i = 0; i < period; i++) {
            currentEMA += closingPrices.get(i);
        }

        currentEMA = currentEMA / (double) period;
        if (historyNeeded) EMAhistory.add(currentEMA);
        //Dont use latest unclosed candle;
        for (int i = period; i < closingPrices.size() - 1; i++) {
            update(closingPrices.get(i));
        }
    }

    @Override
    public void update(double newPrice) {
        // EMA = (Close - EMA(previousBar)) * multiplier + EMA(previousBar)
        currentEMA = (newPrice - currentEMA) * multiplier + currentEMA;

        if (historyNeeded) EMAhistory.add(currentEMA);
    }

    @Override
    public int check(double newPrice) {
        return 0;
    }

    @Override
    public String getExplanation() {
        return null;
    }

    public List<Double> getEMAhistory() {
        return EMAhistory;
    }

    public int getPeriod() {
        return period;
    }

    public static void main(String[] args) {
        List<Double> closePriceHistory = ServiceFunctions.getClosingPrices(CandlestickInterval.DAILY, "BTCUSDT");
        List<Double> emaValues = new ArrayList<>();

        EMA ema = new EMA(closePriceHistory, 20, true);

        for (Double aDouble : closePriceHistory) {
            ema.update(aDouble);
            emaValues.add(ema.currentEMA);
        }

        double emaValue = emaValues.get(emaValues.size() - 2);
        System.out.println(emaValue);
    }

}
