package com.google.shinyay.client

import com.google.shinyay.logger
import com.google.shinyay.model.Message
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

@ShellComponent
class RSocketShellClient(rsocketRequestBuilder: RSocketRequester.Builder) {

    val client = "Client"
    val request = "Request"
    val fireAndForget = "Fire-And-Forget"
    val stream = "Stream"
    lateinit var disposable: Disposable

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

    @ShellMethod("Send One request and No response will be printed")
    fun fineAndForget(): Unit {
        logger.info("Fire-And-Forget. Sending one request. Expecting no response...")
        this.rsocketRequester
                ?.route("fire-and-forget")
                ?.data(Message(client, fireAndForget))
                ?.send()
                ?.block()
    }

    @ShellMethod("Send One request and Many response will be printed")
    fun stream(): Unit {
        this.disposable = this.rsocketRequester
                ?.route("stream")
                ?.data(Message(client, stream))
                ?.retrieveFlux(Message::class.java)
                ?.subscribe { it -> logger.info("Response received: $it") }!!
    }

    @ShellMethod("Stop Streaming messages from the Server")
    fun stop()= disposable?.dispose()

    @ShellMethod("Stream configurations to Server. Stream of responses will be printed")
    fun channel(): Unit {
        val config1: Mono<Duration> = Mono.just(Duration.ofSeconds(1))
        val config: Flux<Duration> = Flux.concat(config1)

        this.disposable = this.rsocketRequester
                ?.route("channel")
                ?.data(config)
                ?.retrieveFlux(Message::class.java)
                ?.subscribe{message -> logger.info(("Response received: $message"))}!!
    }
}