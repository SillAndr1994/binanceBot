import com.binance4j.core.exception.ApiException;
import com.binance4j.core.market.CandlestickInterval;
import com.binance4j.market.client.MarketDataClient;
import com.binance4j.market.tickerstatistics.TickerStatistics;
import com.binance4j.websocket.client.WebsocketCandlestickClient;
import services.Auth;
import services.ServiceFunctions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BotRunner {
    public static void main(String[] args) {
        CandlestickInterval interval = CandlestickInterval.ONE_MINUTE;

        WebsocketCandlestickClient client = new WebsocketCandlestickClient("BTCUSDT".toUpperCase(), interval);

        client.onMessage(cb -> {
            if (cb.getIsBarFinal()) {
                ArrayList<String> growCoins = getGrowCoins(3);

                if (growCoins.size() != 0) {

                }
            }
        });client.open();
    }

    private static void checkingTradingRules(ArrayList<String> symbols) {
        System.out.println();
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
