package me.bitnick.bully.exchange.strategy

import me.bitnick.bully.strategy.BasicStrat
import spock.lang.Specification

class BasicStratTest extends Specification {
    def "BasicStrat.compute() returns a Signal"() {
        when:
        def buySignal = BasicStrat.compute()
        def sellSignal = BasicStrat.compute(true)

        then:
        buySignal == BasicStrat.Signal.BUY
        sellSignal == BasicStrat.Signal.SELL
    }
}
