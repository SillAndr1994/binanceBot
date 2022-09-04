package services;

import candles.Candle;
import com.binance4j.core.market.CandlestickInterval;

import java.util.ArrayList;
import java.util.List;

public class TradingStrategy {
    private CandlestickInterval interval;

    public TradingStrategy(CandlestickInterval interval) {
        this.interval = interval;

    }

    /**
     * checking the situation on the market suitable for the trading strategy
     * @param symbols
     * @return
     */
    public ArrayList<String> checkTradingRules(ArrayList<String> symbols) {
        ArrayList<String> result = new ArrayList<>();

        for (String symbol : symbols) {
            List<Candle> candles = ServiceFunctions.getTwoLatestCandles(this.interval, symbol);
            double ema20 = ServiceFunctions.getLatestCandleEmaValue(this.interval, symbol, 20);
            if (candles.get(1).compare(candles.get(0))) {
                if (candles.get(1).getOpen() > ema20) {
                    result.add(symbol);
                }
            }
        }

        return result;
    }

}
