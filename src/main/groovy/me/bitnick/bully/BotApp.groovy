package me.bitnick.bully

import groovy.util.logging.Slf4j
import me.bitnick.bully.broker.SampleClass

@Slf4j
class BotApp {
    static void main(String[] args) {
        var test = new SampleClass()
        test.websocketTickDataExample()

        while (true) {
            Thread.sleep(850)
        }
    }
}
