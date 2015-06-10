package qt.yeah.startup

/**
 * Created by tianhaowei on 15-6-9.
 */

import java.net.InetSocketAddress
import qt.yeah.core.{HttpRequest, HttpConnector}
import HttpRequest.RequestInfo

import akka.actor._
import qt.yeah.core.HttpConnector


object Bootstrap {

  def main(args: Array[String]): Unit = {
    //val connector = new HttpConnector()
    //connector.start()
    val port = 8080
    val address = new InetSocketAddress(port)
    val system=ActorSystem("ServerSystem")
    val dispatcher=system.actorOf(Props(HttpConnector(address)),name="HttpConnector")
    val requestInfo=system.actorOf(Props(RequestInfo()),name="HeaderInfoHandler")
  }

}
