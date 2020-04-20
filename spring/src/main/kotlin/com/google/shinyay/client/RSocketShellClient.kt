package com.google.shinyay.client

import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.shell.standard.ShellComponent

@ShellComponent
class RSocketShellClient(val rsocketRequestBuilder: RSocketRequester.Builder) {

    val rSocketRequester: RSocketRequester? = rsocketRequestBuilder.connectTcp("localhost", 7000).block()
}