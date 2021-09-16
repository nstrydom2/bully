package me.bitnick.bully.exchange

import spock.lang.Ignore
import spock.lang.Specification

class KrakTest extends Specification {
    def "one plus one is two"() {
        expect:
        1 + 1 == 2
    }

    def "returns base pair(ZUSD) balance info"() {
        given:
        def client = Krak.instanceOf()

        when:
        def jsonObj = client.getBalance()
        def error = jsonObj['error'] as List
        def balance = jsonObj['result']['ZUSD'] as float

        then:
        error.size() < 1
        balance > 0.0

        println(balance)
    }

    def "returns base pair(USDC) balance"() {
        given:
        def client = Krak.instanceOf()

        when:
        def jsonObj = client.getBalance()
        def error = jsonObj['error'] as List
        def balance = jsonObj['result']['USDC'] as float

        then:
        error.size() < 1
        balance > 0.0
    }

    def "returns base pair(XXDG) balance"() {
        given:
        def client = Krak.instanceOf()

        when:
        def jsonObj = client.getBalance()
        def error = jsonObj['error'] as List
        def balance = jsonObj['result']['XXDG'] as float

        then:
        error.size() < 1
        balance > 0.0

        println balance
    }

    @Ignore
    def "submit buy market order returns no errors"() {
        given:
        def client = Krak.instanceOf()
        def params = ['ordertype': 'market', 'type': 'buy', 'volume': '50.0', 'pair': 'XDGUSD']

        when:
        def jsonObj = client.order(params)
        def error = jsonObj['error'] as List
        def result = jsonObj['result'] as Map

        then:
        error.size() < 1
        result != null

        println(jsonObj['result'])
    }

    @Ignore
    def "submit sell market order returns no errors"() {
        given:
        def client = Krak.instanceOf()
        def params = ['ordertype': 'market', 'type': 'sell', 'volume': '10', 'pair': 'XDGUSD']

        when:
        def jsonObj = client.order(params)
        def error = jsonObj['error'] as List
        def result = jsonObj['result'] as Map

        then:
        error.size() < 1
        result != null

        println(jsonObj['result'])
    }
}
