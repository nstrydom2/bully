package me.bitnick.bully.broker.rest.api

import com.squareup.okhttp.Request
import groovy.json.JsonSlurper
import me.bitnick.bully.broker.rest.entities.*
import me.bitnick.bully.broker.rest.entities.results.*
import me.bitnick.bully.broker.rest.tools.ApiSign
import me.bitnick.bully.broker.rest.tools.LocalPropLoader
import org.apache.commons.lang3.StringUtils
import org.springframework.http.MediaType

import static me.bitnick.bully.broker.rest.api.KrakenEndpoints.*

class KrakenRestService {
    private final LocalPropLoader properties
    //private final RestTemplate restTemplate

    KrakenRestService() {
        this.properties = new LocalPropLoader()
        //this.restTemplate = new RestTemplate()
    }

    static ServerDateInfo getTime () {
        return new JsonSlurper().parseText(new Request.Builder()
                .url(url(SERVER_TIME))
                .build()
                .body()
                .toString()) as ServerDateInfo
    }

    static AssetInfo getAssets () {
        return new JsonSlurper().parseText(new Request.Builder()
                .url(url(ASSET_INFO))
                .build()
                .body()
                .toString()) as AssetInfo
    }

    static AssetPairs getAssetPairs () {
        return new JsonSlurper().parseText(new Request.Builder()
                .url(url(ASSET_PAIRS))
                .build()
                .body()
                .toString()) as AssetPairs
    }

    static TickersInfo getTicker (AssetPairsEnum.AssetPairs... pairs) {
        var url = url(TICKER_INFO)
        if (pairs.length > 0) {
            url = StringUtils.join(url, "?pair=", StringUtils.join(pairs, ","))
        }

        return new JsonSlurper().parseText(new Request.Builder()
                .url(url)
                .build()
                .body()
                .toString()) as TickersInfo
    }

    static OHLCValues getOHLC (int interval, int since, String... pairs) {
        var url = "https://api.kraken.com/0/public/OHLC"
        if (pairs.length > 0) {
            url = StringUtils.join(url, "?pair=", StringUtils.join(pairs, ","))
        }

        String fullUrl = String.format("%s&interval=%s", url, interval)
        return new JsonSlurper().parseText(new Request.Builder()
                .url(fullUrl)
                .build()
                .body()
                .toString()) as OHLCValues
    }

    /**
     * @param pair       asset pair
     * @param maxAskBids maximum number of ask/bids
     * @return
     */
    static Depth getOrderBook(AssetPairsEnum.AssetPairs pair, int maxAskBids) {
        var url = String.format("%s?pair=%s&count=%s", url(ORDER_BOOK), pair, maxAskBids)

        return new JsonSlurper().parseText(new Request.Builder()
                .url(url)
                .build()
                .body()
                .toString()) as Depth
    }

    static RecentTradesInfo getRecentTrades (AssetPairsEnum.AssetPairs pair, long sinceTradeId) {
        var url = url(RECENT_TRADES)
        url = StringUtils.join(url, "?pair=", pair)
        if (sinceTradeId != 0) {
            url = StringUtils.join(url, "&since=", sinceTradeId)
        }
        return new JsonSlurper().parseText(new Request.Builder()
                .url(url)
                .build()
                .body()
                .toString()) as RecentTradesInfo
    }

    static SpreadInfo getSpread (AssetPairsEnum.AssetPairs pair, long sinceTradeId) {
        var url = url(SPREAD_DATA)
        url = StringUtils.join(url, "?pair=",pair)
        if (sinceTradeId != 0) {
            url = StringUtils.join(url, "&since=", sinceTradeId)
        }

        return new JsonSlurper().parseText(new Request.Builder()
                .url(url)
                .build()
                .body()
                .toString()) as SpreadInfo
    }

    // private trading methods

    AccountBalanceInfo getBalance() {
        ApiSign.availableKeys(this.properties)
        var url = url(PRIVATE_BALANCE)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as AccountBalanceInfo
    }

    TradeBalanceInfo getTradeBalance () {
        ApiSign.availableKeys(this.properties)
        var url = url(PRIVATE_TRADE_BALANCE)
        var requestsEntity = ApiSign.getRequest(url, PRIVATE_TRADE_BALANCE, this.properties)
        //ResponseEntity<TradeBalanceInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradeBalanceInfo.class)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as TradeBalanceInfo
    }

    OpenOrdersInfo getOpenOrders () {
        ApiSign.availableKeys(this.properties)
        var url = url(OPEN_ORDERS)
        var requestsEntity = ApiSign.getRequest(url, OPEN_ORDERS, this.properties)
        //ResponseEntity<OpenOrdersInfo> response = new RestTemplate().postForEntity(url, requestsEntity, OpenOrdersInfo.class)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as AccountBalanceInfo
    }

    ClosedOrdersInfo getClosedOrders (double startTime, double endTime) {
        var params = new HashMap<String,Object>()
        if (startTime != 0)
            params.put("start", startTime)
        if (endTime != 0)
            params.put("end", endTime)
        ApiSign.availableKeys(this.properties)
        var url = KrakenEndpoints.url(KrakenEndpoints.CLOSED_ORDERS)
        var requestsEntity = ApiSign.getRequest(url, params, KrakenEndpoints.CLOSED_ORDERS, this.properties)
        //ResponseEntity<ClosedOrdersInfo> response = new RestTemplate().postForEntity(url, requestsEntity, ClosedOrdersInfo.class)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as AccountBalanceInfo
    }

    OpenPositionsInfo getOpenPositions () {
        ApiSign.availableKeys(this.properties)
        var url = KrakenEndpoints.url(KrakenEndpoints.OPEN_POSITION)
        var requestsEntity = ApiSign.getRequest(url, KrakenEndpoints.OPEN_POSITION, this.properties)
        //ResponseEntity<OpenPositionsInfo> response = new RestTemplate().postForEntity(url, requestsEntity, OpenPositionsInfo.class)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as AccountBalanceInfo
    }

    TradesHistoryInfo getTradesHistory () {
        ApiSign.availableKeys(this.properties)
        var url = url(TRADES_HISTORY)
        var requestsEntity = ApiSign.getRequest(url, TRADES_HISTORY, this.properties)
        //ResponseEntity<TradesHistoryInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradesHistoryInfo.class)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as AccountBalanceInfo
    }

    TradeVolumeInfo getTradeVolume(String ...pairs) {
        ApiSign.availableKeys(this.properties)
        var data = new HashMap<String,Object>()
        data.put("pair", StringUtils.join(pairs,","))
        var url = url(TRADE_VOLUME)
        var requestsEntity = ApiSign.getRequest(url, data, TRADE_VOLUME, this.properties)
        //ResponseEntity<TradeVolumeInfo> response = new RestTemplate().postForEntity(url, requestsEntity, TradeVolumeInfo.class)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as AccountBalanceInfo
    }

    AddOrderInfo addOrder (Order order) {
        ApiSign.availableKeys(this.properties)
        var data = order.asMap()
        var url = url(ADD_ORDER)
        var requestEntity = ApiSign.getRequest(url, data, ADD_ORDER, this.properties)
        //ResponseEntity<AddOrderInfo> response = restTemplate.postForEntity(url, requestEntity, AddOrderInfo.class)
        return new JsonSlurper()(new Request.Builder()
                .url(url)
                .addHeader("API-Key", this.properties.getApi())
                .addHeader("API-Sign", ApiSign.calculateSignature(System.currentTimeMillis().toString(),
                        new HashMap<>(), this.properties.getApiSecret(), PRIVATE_BALANCE))
                .addHeader("User-Agent", "Kraken REST API")
                .build()
                .body()) as AccountBalanceInfo
    }
}
