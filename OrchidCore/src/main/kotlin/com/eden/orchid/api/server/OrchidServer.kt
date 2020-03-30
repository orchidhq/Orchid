package com.eden.orchid.api.server

import com.eden.orchid.api.OrchidContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@JvmSuppressWildcards
class OrchidServer
@Inject
constructor(
    val controllers: Set<OrchidController>,
    val fileController: OrchidFileController
) {
    var httpServerPort = 0
    var websocketPort = 0

    var server: OrchidWebserver? = null
    var websocket: OrchidWebsocket? = null

    @Throws(IOException::class)
    fun start(context: OrchidContext, port: Int) {
        server = OrchidWebserver(context, controllers, fileController, port)
        httpServerPort = server!!.listeningPort

        websocket = OrchidWebsocket(context, port)
        websocketPort = websocket!!.listeningPort
    }

}
