package com.tugo.learn.nettytry

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.{ChannelHandlerContext, ChannelInitializer, SimpleChannelInboundHandler}
import io.netty.util.CharsetUtil

import java.net.InetSocketAddress

class EchoClientHandler extends SimpleChannelInboundHandler[ByteBuf] {
  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    println("Channel active called")
    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks", CharsetUtil.UTF_8))
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    println("client received " + msg.toString(CharsetUtil.UTF_8))
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
  }
}

class EchoClient {
  def start(): Unit = {
    val grp = new NioEventLoopGroup();
    try {
      val b = new Bootstrap();
      b.group(grp)
        .channel(classOf[NioSocketChannel])
        .remoteAddress(new InetSocketAddress(8082))
        .handler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel): Unit =
            ch.pipeline().addLast(new EchoClientHandler())
        });
      val cf = b.connect().sync();
      val chnl  = cf.channel();
      var loop = true;
      while (loop) {
        val line = scala.io.StdIn.readLine()
        if (line.startsWith("quit")) loop = false else chnl.writeAndFlush(line)
      }
      cf.channel().closeFuture().sync()
    } finally {
      grp.shutdownGracefully().sync()
    }
  }
}

object EchoClient {
  def main(args: Array[String]): Unit = {
    new EchoClient().start()
  }
}