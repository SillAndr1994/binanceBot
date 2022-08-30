import com.binance4j.core.exception.ApiException;
import com.binance4j.market.client.MarketDataClient;
import com.binance4j.market.tickerstatistics.TickerStatistics;
import services.Auth;

import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String API_KEY = Auth.getApi_key();
        String SECRET_KEY = Auth.getSecret_key();

        MarketDataClient client = new MarketDataClient(API_KEY, SECRET_KEY);

        List<TickerStatistics> tickers = null;

        try{
            tickers =  client.get24hTickerStatistics().execute();
        }catch(ApiException e){
            System.out.println("error");
        }

        if (tickers == null) System.exit(0);

        HashMap<String, Double> growTickers = new HashMap<>();

        for (TickerStatistics ticker : tickers) {
            if ((ticker.getSymbol().contains("usdt".toUpperCase())) && (ticker.getPriceChangePercent().doubleValue()) > 0) {
                System.out.println(ticker);
            }
        }

    }
}
