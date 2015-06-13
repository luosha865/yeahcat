package qt.yeah.example

import java.net.InetSocketAddress

import akka.actor.{Props, ActorSystem}
import qt.yeah.core.{HttpHandler, Routes, HttpConnector}
import qt.yeah.core.HttpRequest.RequestInfo
import qt.yeah.startup.YeahCat

/**
 * Created by tianhaowei on 15-6-13.
 */



object ExServer {

  def test(){
    val port = 8080
    val address = new InetSocketAddress(port)
    val system=ActorSystem("ServerSystem")
    val routers = new Routes()
    routers.get("/") {"HTTP/1.1 200 OK\r\n\r\nIt really works!"}
    val dispatcher=system.actorOf(Props(HttpConnector(address,routers)),name="HttpConnector")
    val requestInfo=system.actorOf(Props(RequestInfo()),name="HeaderInfoHandler")
  }
  class indexHandler extends HttpHandler{
    override def get():(String)={"HTTP/1.1 200 OK\r\n\r\nIt really works!"}
  }


  def main(args: Array[String]): Unit = {
    val server = YeahCat(8080)
    server.addHandler("/",new indexHandler())
    server.run
  }
    //val connector = new HttpConnector()
    //connector.start()

}
