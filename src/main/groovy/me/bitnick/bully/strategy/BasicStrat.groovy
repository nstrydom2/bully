package me.bitnick.bully.strategy

class BasicStrat {
    // TODO revisit this method we need the
    //  target parameters for the strategy
    //  fill in the conditional then 100% complete
    static def compute(Boolean closePosition = false) {
        if (closePosition)
            return Signal.SELL

        // Open position conditions goes here
        def buyCondition = true
        if (buyCondition)
            return Signal.BUY

        return Signal.NONE
    }

    static enum Signal {
        BUY, SELL, NONE
    }
}
