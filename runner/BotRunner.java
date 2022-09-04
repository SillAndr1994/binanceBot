package runner;

import com.binance4j.core.exception.ApiException;
import com.binance4j.core.market.CandlestickInterval;
import com.binance4j.market.client.MarketDataClient;
import com.binance4j.market.tickerstatistics.TickerStatistics;
import com.binance4j.websocket.client.WebsocketCandlestickClient;
import services.Auth;
import services.ServiceFunctions;
import services.TradingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Simple binance bot
 */
public class BotRunner {
    public static void main(String[] args) {
        CandlestickInterval interval = CandlestickInterval.THREE_MINUTES;

        WebsocketCandlestickClient client = new WebsocketCandlestickClient("BTCUSDT".toUpperCase(), interval);

        client.onMessage(cb -> {
            if (cb.getIsBarFinal()) {
                ArrayList<String> growCoins = getGrowCoins(3);

                TradingStrategy strategy = null;
                ArrayList<String> processedSymbols = null;

                if (growCoins.size() != 0) {
                    strategy = new TradingStrategy(interval);
                    processedSymbols = strategy.checkTradingRules(growCoins);
                }

                if (processedSymbols != null && processedSymbols.size() != 0) {
                    System.out.println(processedSymbols);
                }
            }
        });client.open();
    }


    /**
     * Get 24 hour statistics growing coins
     * @return
     */
    private static ArrayList<String> getGrowCoins(double growValue) {
        String api_key = Auth.getApi_key();
        String secret_key = Auth.getSecret_key();

        MarketDataClient client = new MarketDataClient(api_key, secret_key);

        List<TickerStatistics> tickers = null;

        try{
            tickers =  client.get24hTickerStatistics().execute();
        }catch(ApiException e){
            System.out.println("error");
        }

        HashMap<String, Double> growTickers = new HashMap<>();

        for (TickerStatistics ticker : tickers) {
            if ((ticker.getSymbol().contains("usdt".toUpperCase())) && (ticker.getPriceChangePercent().doubleValue()) > growValue) {
                growTickers.put(ticker.getSymbol(), ticker.getPriceChangePercent().doubleValue());
            }
        }

        LinkedHashMap<String, Double> sortedGrowCoins = ServiceFunctions.sortHashMapByValue(growTickers);


        return new ArrayList<>(sortedGrowCoins.keySet());

    }
}
