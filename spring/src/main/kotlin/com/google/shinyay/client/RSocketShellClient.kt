package com.google.shinyay.client

import com.google.shinyay.logger
import com.google.shinyay.model.Message
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class RSocketShellClient(rsocketRequestBuilder: RSocketRequester.Builder) {

    val client = "Client"
    val request = "Request"
    val fireAndForget = "Fire-And-Forget"

    val rsocketRequester = rsocketRequestBuilder.connectTcp("localhost", 7000).block()

    @ShellMethod("Send One request and One response will be printed.")
    fun requestResponse(): Unit {
        logger.info("Sending one request. Waiting for one response...")
        val message = this.rsocketRequester
                ?.route("request-response")
                ?.data(Message(client, request))
                ?.retrieveMono(Message::class.java)
                ?.block()
        logger.info("Response was: $message")
    }

    @ShellMethod("Send One request and No response will be ")
    fun fineAndForget(): Unit {
        logger.info("Fire-And-Forget. Sending one request. Expecting no response...")
        this.rsocketRequester
                ?.route("fire-and-forget")
                ?.data(Message(client, fireAndForget))
                ?.send()
                ?.block()
    }
}