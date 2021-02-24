package com.tugo.learn.network

import java.net.InetSocketAddress
import java.nio.channels.SocketChannel

object BadClient {
  def main(args: Array[String]): Unit = {
    println("this is a bad client")
    for (i <- (0 to 3000)) {
      val sc = SocketChannel.open(new InetSocketAddress(8082))
    }
    Thread.sleep(600000)
  }
}
