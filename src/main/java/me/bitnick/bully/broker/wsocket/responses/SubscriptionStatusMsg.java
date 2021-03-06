package me.bitnick.bully.broker.wsocket.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionStatusMsg {

    private int channelID;
    private String channelName;
    private String ticker;
    private String event;
    private String pair;
    private String status;
    private String errorMessage;
    private Map<String, Object> subscription;

    public String getSubscriptionName() {
        return (String) this.subscription.get("name"); // ticker, ohlc
    }
}
