package com.google.shinyay.client

import com.google.shinyay.logger
import com.google.shinyay.model.Message
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod

@ShellComponent
class RSocketShellClient(val rsocketRequestBuilder: RSocketRequester.Builder) {

    val client = "Client"
    val request = "Request"
    val fire_and_forget = "Fire-And-Forget"
    val stream = "Stream"
    val channel = "Channel"

    val rsocketRequester: RSocketRequester? = rsocketRequestBuilder.connectTcp("localhost", 7000).block()

    @ShellMethod
    fun requestResponse(): Unit {
        logger.info("Sending one request. Waiting for one response...")
        val message = this.rsocketRequester
                ?.route("request-response")
                ?.data(Message(client, request))
                ?.retrieveMono(Message::class.java)
                ?.block()
        logger.info("Response was: $message")
    }
}