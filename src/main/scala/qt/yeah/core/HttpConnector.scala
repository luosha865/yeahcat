package qt.yeah.core

/**
 * Created by tianhaowei on 15-6-9.
 */

import java.net.InetSocketAddress
import java.nio._
import java.nio.channels._
import java.nio.charset._

import akka.actor._

import scala.collection.mutable.{ArrayBuffer, Queue}

object HttpConnector {
  def apply( address: InetSocketAddress,routesmap: Routes, backlog: Int = 0): HttpConnector = {
    val server = ServerSocketChannel.open().bind(address, backlog)
    new HttpConnector(server,routesmap)
  }
}

class HttpConnector(server: ServerSocketChannel,routesmap: Routes) extends Actor {

  val actorpool:ArrayBuffer[ActorRef]=new ArrayBuffer(0)
  val availableactors:Queue[Int]=new Queue()
  var startactors=10
  var maxactors=200
  //create the actors
  for(loop<-0 to startactors){
    actorpool.append()
    actorpool.append(context.actorOf(Props(HttpProcessor(loop,routesmap)),name="RequestHandler"+loop))
    availableactors.enqueue(loop)
  }

  while(true) {
    var client=server.accept
    val next =
      if(availableactors.isEmpty) {
        if(actorpool.size < maxactors) {
          var actornum=actorpool.size
          actorpool.append(context.actorOf(Props(HttpProcessor(actornum,routesmap)),name="RequestHandler"+actornum))
          actornum
        }
        else {
          -1
        }
      }
      else {
        availableactors.dequeue
      }
    if(next!= -1) {
      actorpool(next) ! client
    }
    else {
      val encoder=Charset.forName("UTF-8").newEncoder
      client.write(encoder.encode(CharBuffer.wrap("HTTP/1.1 503 Service Unavailable")))
      client.close
    }
  }

  def receive= {
    case RequeueActor(actornum: Int) => availableactors.enqueue(actornum)
    case x:Any => println("Dispatcher received message: "+x)
  }
}

