package com.tugo.learn.nettytry

import io.netty.bootstrap.ServerBootstrap
import io.netty.buffer.{ByteBuf, Unpooled}
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.{NioServerSocketChannel}
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter, ChannelInitializer}
import io.netty.util.CharsetUtil

import java.net.InetSocketAddress

class EchoServer {
  def start(): Unit = {
    val grp = new NioEventLoopGroup()
    try {
      val b = new ServerBootstrap()
      b.group(grp)
        .channel(classOf[NioServerSocketChannel])
        .localAddress(new InetSocketAddress(8082))
        .childHandler(new ChannelInitializer[SocketChannel] {
          override def initChannel(ch: SocketChannel) = {
            println("initializing channel")
            ch.pipeline().addLast(new EchoHandler)
          }
        })
      val f = b.bind().sync()
      f.channel().closeFuture().sync()
    } finally {
      grp.shutdownGracefully().sync()
    }
  }
}

class EchoHandler extends ChannelInboundHandlerAdapter {

  override def channelRead(ctx: ChannelHandlerContext, msg : Any): Unit = {
    val buf : ByteBuf = msg.asInstanceOf[ByteBuf]
    println("received data: " + buf.toString(CharsetUtil.UTF_8))
    ctx.write(buf)
  }

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
    println("channel read complete called")
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
    //  addListener(ChannelFutureListener.CLOSE);
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    cause.printStackTrace()
    println("called exception caught")
    ctx.close()
  }
}

object EchoServer {
  def main(args: Array[String]): Unit = {
    print("main of echo server")
    new EchoServer().start()
  }
}