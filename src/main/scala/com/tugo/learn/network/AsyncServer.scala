package com.tugo.learn.network

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{SelectionKey, Selector, ServerSocketChannel, SocketChannel}
import scala.util.{Success, Try}

object AsyncServer {

  class Client(val sc: SocketChannel, selector: Selector) {

    var sk: SelectionKey = sc.register(selector, SelectionKey.OP_READ)
    val buf = ByteBuffer.allocate(1024)

    def handleRead(): Unit = {
      val ret = sc.read(buf)
      if (ret <= 0) {
        close()
      } else {
        buf.flip()
        changeBuf
        sk.interestOps(SelectionKey.OP_WRITE)
      }
    }

    def close(): Unit = {
      sc.close()
    }

    def handleWrite() = {
      sc.write(buf)
      if (buf.hasRemaining) {
        buf.compact()
        sk.interestOps(SelectionKey.OP_WRITE)
      }
    }

    def changeBuf = {
      (buf.position() to buf.limit()).foreach(i => {
        var c: Int = buf.get(i)
        c = if (Character.isLetter(c)) (c ^ ' ') else c
        buf.put(i, c.asInstanceOf[Byte])
      })
    }
  }

  def main(args: Array[String]): Unit = {
    val ssc = ServerSocketChannel.open
    val selector = Selector.open

    def handleAccept(sk: SelectionKey) = {
      val ssc = sk.channel().asInstanceOf[ServerSocketChannel]
      val sc: SocketChannel = ssc.accept()
      sc.configureBlocking(false)
      val client = new Client(sc, selector)
      val key = sc.register(selector, SelectionKey.OP_READ, client)
      client.sk = key
    }

    def start(): Try[Unit] = {
      Try {
        ssc.configureBlocking(false)
        ssc.bind(new InetSocketAddress(8082))
        ssc.register(selector, SelectionKey.OP_ACCEPT)
        loop
      } recoverWith (th => Success(()))
    }

    def loop = {
      selector.select()
      val iter = selector.selectedKeys().iterator
      while (iter.hasNext) {
        val key = iter.next
        if (key.isAcceptable) {
          handleAccept(key)
        } else if (key.isReadable) {
          val client = key.attachment().asInstanceOf[Client]
          client.handleRead()
        } else if (key.isWritable) {
          val client = key.attachment().asInstanceOf[Client]
          client.handleWrite()
        }
      }
    }
  }
}
