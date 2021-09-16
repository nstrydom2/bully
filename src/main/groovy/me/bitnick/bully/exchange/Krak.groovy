package me.bitnick.bully.exchange

import edu.self.kraken.api.KrakenApi
import groovy.json.JsonSlurper

class Krak {
    KrakenApi client
    JsonSlurper json

    def getServerTime() {
        def response = client.queryPublic(KrakenApi.Method.TIME)
        return json.parseText(response)
    }

    def getBalance() {
        def response = client.queryPrivate(KrakenApi.Method.BALANCE)
        return json.parseText(response)
    }

    def order(Map params) {
        def response = client.queryPrivate(KrakenApi.Method.ADD_ORDER, params)
        return json.parseText(response)
    }

    private def setKeys() {
        File file = new File('/home/ghost/projects/bully/107401837507139075.json')
        def jsonObj = file.withReader {reader ->
            return new JsonSlurper().parseText(reader.readLine())
        }
        client.setKey(jsonObj['key'] as String)
        client.setSecret(jsonObj['secret'] as String)
    }

    private Krak() {
        this.client = new KrakenApi()
        this.json = new JsonSlurper()

        setKeys()
    }

    static Krak instanceOf() {
        return new Krak()
    }
}
