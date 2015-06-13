package qt.yeah.startup

/**
 * Created by tianhaowei on 15-6-9.
 */

import java.net.InetSocketAddress
import qt.yeah.core.{Routes, HttpRequest, HttpConnector,HttpHandler}
import HttpRequest.RequestInfo

import akka.actor._


object YeahCat {
  def apply(port: Int): YeahCat = {
    new YeahCat(port)
  }

  def main(args: Array[String]): Unit = {
    val server = YeahCat(8080)
    server.addHandler("/",new HttpHandler())
    server.run
  }
}

class YeahCat(port :Int){
  val routers = new Routes()
  def addHandler(url:String,handler : HttpHandler): Unit ={
    routers.get(url){handler.get()}
    routers.post(url){handler.post()}
    routers.delete(url){handler.delete()}
    routers.put(url){handler.put()}
  }
  def run: Unit = {
    //val connector = new HttpConnector()
    //connector.start()
    val address = new InetSocketAddress(port)
    val system=ActorSystem("ServerSystem")
    val dispatcher=system.actorOf(Props(HttpConnector(address,routers)),name="HttpConnector")
    val requestInfo=system.actorOf(Props(RequestInfo()),name="HeaderInfoHandler")
  }


}
