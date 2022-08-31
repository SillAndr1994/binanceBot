import candles.Candle;
import com.binance4j.core.exception.ApiException;
import com.binance4j.core.market.CandlestickBar;
import com.binance4j.core.market.CandlestickInterval;
import com.binance4j.market.client.MarketDataClient;
import com.binance4j.market.kline.KlinesRequest;
import com.binance4j.market.tickerstatistics.TickerStatistics;
import services.Auth;
import services.ServiceFunctions;

import java.util.*;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        List<Candle> candles = getTwoLatestCandles(CandlestickInterval.DAILY, "BTCUSDT");
        System.out.println("previous candle: " + candles.get(0));
        System.out.println("latest candle: " + candles.get(1));
    }

    public static List<Candle> getTwoLatestCandles(CandlestickInterval interval, String symbol) {
        MarketDataClient client = new MarketDataClient(Auth.getApi_key(), Auth.getSecret_key());

        List<CandlestickBar> klines = null;

        try {
            klines = client.getKlines(new KlinesRequest(symbol, interval, 3)).execute();
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }

        List<CandlestickBar> candlestickBars = new ArrayList<>();

        CandlestickBar lc = klines.get(klines.size()-2);
        CandlestickBar pc = klines.get(klines.size()-3);

        List<Candle> candles = new ArrayList<>();

        Candle latestCandle = new Candle(lc.getOpen(), lc.getClose(), lc.getHigh(), lc.getLow(), lc.getVolume(), lc.getOpenTime(), lc.getCloseTime());
        Candle previousCandle = new Candle(pc.getOpen(), pc.getClose(), pc.getHigh(), pc.getLow(), pc.getVolume(), pc.getOpenTime(), pc.getCloseTime());

        candles.add(0, previousCandle);
        candles.add(1, latestCandle);

        return candles;
    }
}
