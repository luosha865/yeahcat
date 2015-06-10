package qt.yeah.startup

/**
 * Created by tianhaowei on 15-6-9.
 */

import java.net.InetSocketAddress
import qt.yeah.core.{HttpRequest, HttpConnector}
import HttpRequest.RequestInfo

import java.nio._
import java.nio.channels._
import java.nio.charset._
import akka.actor._
import qt.yeah.core.HttpConnector
import collection.mutable.{ArrayBuffer,Map,Queue}
import scala.util.matching.Regex


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
