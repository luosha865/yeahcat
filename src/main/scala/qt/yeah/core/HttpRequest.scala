package qt.yeah.core

/**
 * Created by tianhaowei on 15-6-10.
 */

import akka.actor._

import scala.collection.mutable.Map

object HttpRequest
{
  case class AddHeader(name:String,value:String)
  case class GetHeaders()

  object RequestInfo{
    def apply(): RequestInfo = {
      new RequestInfo()
    }
  }

  class RequestInfo extends Actor {
    //Storage for the headers for each request
    val headers=Map[ActorRef,Map[String,String]]()

    def addHeader(sender:ActorRef,name:String,value:String) {
      headers+=(sender->(headers.getOrElse(sender,Map[String,String]())+(name->value)))
    }
    def getHeaders(sender:ActorRef) {
      val response=headers.getOrElse(sender,Map[String,String]()).mkString(" - ")
      println(headers)
    }

    def receive= {
      case "Stop" => context.system.shutdown
      case AddHeader(name:String,value:String) => addHeader(sender,name,value)
      case GetHeaders => getHeaders(sender)
      case _ => println("Received a message from a crazy person")
    }
  }
}