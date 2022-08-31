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

        for (int i = 0; i < symbols.size(); i++) {
            if (checkAbsorptionModel(symbols.get(i))) {
                if (checkEmaValue(symbols.get(i))) {
                    result.add(symbols.get(i));
                }
            }
        }

        return result;
    }

    /**
     * checking the bullish engulfing pattern and matching candle parameters
     * @return
     */
    private boolean checkAbsorptionModel(String symbol) {
        boolean result = false;

        List<Candle> candles = ServiceFunctions.getTwoLatestCandles(this.interval, symbol);

        if (candles.get(0).compare(candles.get(1))) {
            result = true;
        }

        return result;
    }


    /**
     * Check close value > ema
     * @param symbol
     * @return
     */
    private boolean checkEmaValue(String symbol) {
        boolean result = false;

        double ema = ServiceFunctions.getLatestCandleEmaValue(this.interval, symbol); // get latest candle ema value
        double open = ServiceFunctions.roundingValue(symbol, ServiceFunctions.getLatestCandle(this.interval, symbol).getOpen()); // get latestCandle open price

        if (open >= ema) {
            result = true;
        }

        return result;

    }


}
