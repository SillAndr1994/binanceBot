package services;

import candles.Candle;
import com.binance4j.core.exception.ApiException;
import com.binance4j.core.market.CandlestickBar;
import com.binance4j.core.market.CandlestickInterval;
import com.binance4j.market.client.MarketDataClient;
import com.binance4j.market.kline.KlinesRequest;

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

}
