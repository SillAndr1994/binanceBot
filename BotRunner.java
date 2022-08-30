import com.binance4j.core.market.CandlestickInterval;
import com.binance4j.websocket.client.WebsocketCandlestickClient;
import com.binance4j.websocket.client.WebsocketClient;
import io.reactivex.rxjava3.functions.Consumer;
import services.Auth;

import java.util.ArrayList;

public class BotRunner {
    public static void main(String[] args) {
        CandlestickInterval interval = CandlestickInterval.ONE_MINUTE;
        String API_KEY = Auth.getApi_key();
        String SECRET_KEY = Auth.getSecret_key();

        WebsocketCandlestickClient client = new WebsocketCandlestickClient("BTCUSDT".toUpperCase(), interval);

        client.onMessage(cb -> {
            if (cb.getIsBarFinal()) {

            }
        });client.open();
    }



}
