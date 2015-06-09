package qt.yeah.connector.http

/**
 * Created by tianhaowei on 15-6-9.
 */

import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class HttpConnector(port: Int) extends Runnable{

  var stopped : Boolean = false
  private val scheme = "http"

  def getScheme():String ={
    return scheme
  }

  def run(): Unit =   {
    val serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"))
    while (!stopped) {
      // Accept the next incoming connection from the server socket
      //Socket socket = null
      try {
        val socket = serverSocket.accept()
      }
      catch{
        case _ =>
      }
      // Hand this socket off to an HttpProcessor
      val processor = new HttpProcessor(this)
      processor.process(socket)
    }
  }

  def start() :Unit ={
    val thread = new Thread(this)
    thread.start()
  }
}
