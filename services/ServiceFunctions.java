package services;

import candles.Candle;
import com.binance4j.core.exception.ApiException;
import com.binance4j.core.market.CandlestickBar;
import com.binance4j.core.market.CandlestickInterval;
import com.binance4j.market.client.MarketDataClient;
import com.binance4j.market.kline.KlinesRequest;
import com.binance4j.market.priceticker.PriceTicker;
import com.binance4j.market.priceticker.PriceTickerRequest;
import indicators.EMA;

import java.text.DecimalFormat;
import java.util.*;

/**
 * auxiliary service functions
 */
public class ServiceFunctions {

    /**
     * Sort hashmap by value
     * @param hashMap
     * @return
     */
    public static LinkedHashMap<String, Double> sortHashMapByValue(HashMap<String, Double> hashMap) {
        List mapKeys = new ArrayList(hashMap.keySet());
        List mapValues = new ArrayList(hashMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap sortedMap = new LinkedHashMap();

        Iterator valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Object val = valueIt.next();
            Iterator keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Object key = keyIt.next();
                String comp1 = hashMap.get(key).toString();
                String comp2 = val.toString();

                if (comp1.equals(comp2)){
                    hashMap.remove(key);
                    mapKeys.remove(key);
                    sortedMap.put((String)key, (Double)val);
                    break;
                }
            }
        }

        return sortedMap;
    }


    /**
     * Get two latest candles parameters
     * @param interval candle interval
     * @param symbol candle symbol
     * @return list of candles
     */
    public static List<Candle> getTwoLatestCandles(CandlestickInterval interval, String symbol) {
        MarketDataClient client = new MarketDataClient(Auth.getApi_key(), Auth.getSecret_key());

        List<CandlestickBar> klines = null;

        try {
            klines = client.getKlines(new KlinesRequest(symbol, interval)).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        List<CandlestickBar> candlestickBars = new ArrayList<>();

        CandlestickBar lc = klines.get(klines.size()-3);
        CandlestickBar pc = klines.get(klines.size()-2);

        List<Candle> candles = new ArrayList<>();

        Candle latestCandle = new Candle(lc.getOpen(), lc.getClose(), lc.getHigh(), lc.getLow(), lc.getVolume(), lc.getOpenTime(), lc.getCloseTime());
        Candle previousCandle = new Candle(pc.getOpen(), pc.getClose(), pc.getHigh(), pc.getLow(), pc.getVolume(), pc.getOpenTime(), pc.getCloseTime());

        candles.add(0, previousCandle);
        candles.add(1, latestCandle);

        return candles;
    }


    /**
     * return candles(default=500)
     */
    public static ArrayList<Candle> getCandles(CandlestickInterval interval, String symbol) {
        MarketDataClient client = new MarketDataClient(Auth.getApi_key(), Auth.getSecret_key());

        List<CandlestickBar> klines = null;

        try {
            klines = client.getKlines(new KlinesRequest(symbol, interval)).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Candle> candles = new ArrayList<>();

        ArrayList<CandlestickBar> candlestickBars = new ArrayList<>();

        for (CandlestickBar kline : klines) {
            int i = 0;
            Candle candle = new Candle(kline.getOpen(), kline.getClose(), kline.getHigh(), kline.getLow(), kline.getVolume(), kline.getOpenTime(), kline.getCloseTime());
            candles.add(i, candle);
            ++i;
        }

        return candles;
    }

    /**
     * Get latest candle closed values
     * @param interval inteval
     * @param symbol coin symbol
     * @return
     */
    public static Candle getLatestCandle(CandlestickInterval interval, String symbol) {
        MarketDataClient client = new MarketDataClient(Auth.getApi_key(), Auth.getSecret_key());

        List<CandlestickBar> klines = null;

        try {
            klines = client.getKlines(new KlinesRequest(symbol.toUpperCase(), interval)).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        CandlestickBar lc = klines.get(klines.size()-3);
        Candle latestCandle = new Candle(lc.getOpen(), lc.getClose(), lc.getHigh(), lc.getLow(), lc.getVolume(), lc.getOpenTime(), lc.getCloseTime());

        return latestCandle;

    }

    /**
     * Get previous closed candle
     * @param interval candle interval
     * @param symbol candle symbol
     * @return
     */
    public static Candle getPreviousCandle(CandlestickInterval interval, String symbol) {
        MarketDataClient client = new MarketDataClient(Auth.getApi_key(), Auth.getSecret_key());

        List<CandlestickBar> klines = null;

        try {
            klines = client.getKlines(new KlinesRequest(symbol.toUpperCase(), interval)).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        CandlestickBar pc = klines.get(klines.size()-2);
        Candle previousCandle = new Candle(pc.getOpen(), pc.getClose(), pc.getHigh(), pc.getLow(), pc.getVolume(), pc.getOpenTime(), pc.getCloseTime());

        return previousCandle;
    }

    /**
     * Get Candles closing prices history (default=500)
     * @param interval candle interval
     * @param symbol candle symbol
     * @return
     */
    public static List<Double> getClosingPrices(CandlestickInterval interval, String symbol) {
        MarketDataClient client = new MarketDataClient(Auth.getApi_key(), Auth.getSecret_key());

        List<CandlestickBar> klines = null;

        try {
            klines = client.getKlines(new KlinesRequest(symbol.toUpperCase(), interval)).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        List<Double> closePriceHistory = new ArrayList<>();

        for (CandlestickBar kline : klines) {
            closePriceHistory.add(kline.getClose().doubleValue());
        }

        return closePriceHistory;
    }


    /**
     * Get ema value for latest candle
     * @param interval
     * @param symbol
     * @return
     */
    public static double getLatestCandleEmaValue(CandlestickInterval interval, String symbol) {
        List<Double> closePricesHistory = ServiceFunctions.getClosingPrices(interval, symbol.toUpperCase());
        List<Double> emaValues = new ArrayList<>();

        EMA ema = new EMA(closePricesHistory, 20, false);

        double emaValue = ema.get();

        return roundingValue(symbol, emaValue);

    }


    /**
     * rounding value for a specific coin per binance
     * @param symbol coin symbol
     * @param interval coin interval
     * @param value value for rounding
     * @return double rounded number
     */
    public static double roundingValue(String symbol, double value) {
        double closePrice = getLatestCandle(CandlestickInterval.DAILY, symbol.toUpperCase()).getClose();
        String singsNumbers = Double.toString(closePrice);
        singsNumbers = singsNumbers.substring(singsNumbers.indexOf(".")+1);
        int singsCount = singsNumbers.length();

        String pattern = "#" + "." + "#".repeat(singsCount);
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        double result = Double.valueOf(decimalFormat.format(value));

        return result;
    }
}
