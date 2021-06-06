package me.bitnick.bully.broker

import com.njkim.reactivecrypto.core.ExchangeClientFactory
import com.njkim.reactivecrypto.core.common.model.ExchangeVendor
import com.njkim.reactivecrypto.core.common.model.currency.CurrencyPair
import com.njkim.reactivecrypto.core.websocket.ExchangePublicWebsocketClient
import groovy.util.logging.Slf4j

@Slf4j
class SampleClass {
    void websocketTickDataExample() {
        // create websocketClient for each crypto currency exchange
        var websocketClient = ExchangeClientFactory.publicWebsocket(ExchangeVendor.KRAKEN)

        var targetPairs = Collections.singletonList(CurrencyPair.parse("BTC", "USDT"))
        websocketClient.createTradeWebsocket(targetPairs)
                .doOnNext(tickData -> log.info("new tick data " + tickData))
                .subscribe()
    }

    void httpLimitOrderExample() {
        var orderPlaceResult = ExchangeClientFactory.http(ExchangeVendor.BINANCE)
                .privateApi("accessKey", "secretKey")
                .order().limitOrder()
                .limitOrder(CurrencyPair(Currency.BTC, Currency.KRW),
                        TradeSideType.BUY,
                        BigDecimal.valueOf(10000000.0),
                        BigDecimal.valueOf(10.0)
                )
                .block();

        log.info("{}", orderPlaceResult);
    }
}