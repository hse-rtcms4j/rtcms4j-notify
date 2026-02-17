package ru.enzhine.rtcms4j.notify.config

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.slf4j.LoggerFactory
import org.springframework.boot.web.embedded.netty.NettyServerCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.netty.http.server.HttpServer
import java.net.SocketException

@Configuration
class NettyConfig {
    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.declaringClass)
    }

    @Bean
    fun webServerFactoryCustomizer() =
        NettyServerCustomizer { httpServer: HttpServer ->
            httpServer
                .doOnConnection { connection ->
                    connection.addHandlerLast(
                        object : ChannelInboundHandlerAdapter() {
                            override fun exceptionCaught(
                                ctx: ChannelHandlerContext,
                                cause: Throwable,
                            ) {
                                if (cause is SocketException && cause.message == "Connection reset") {
                                    logger.info("Client connection reset.")
                                    ctx.close()
                                } else {
                                    super.exceptionCaught(ctx, cause)
                                }
                            }
                        },
                    )
                }
        }
}
