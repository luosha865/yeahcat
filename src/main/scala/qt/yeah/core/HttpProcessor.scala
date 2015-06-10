package qt.yeah.core

/**
 * Created by tianhaowei on 15-6-9.
 */

import java.nio._
import java.nio.channels._
import java.nio.charset._

import akka.actor._

import scala.collection.mutable.Map
import scala.util.matching.Regex

object HttpProcessor{
  def apply(actornum: Int,routesmap:Routes = new Routes()): HttpProcessor = {
    new HttpProcessor(actornum ,routesmap)
  }
}

class HttpProcessor(actornum: Int,routesmap:Routes) extends Actor{

  val encoder=Charset.forName("UTF-8").newEncoder

  def parseRequest(client: SocketChannel):String= {
    val decoder=Charset.forName("UTF-8").newDecoder
    val requestraw=ByteBuffer.allocateDirect(512)
    val requestpart=CharBuffer.allocate(512)
    var request=""
    var httpversion=Array("")
    var numbytes=512 // ensure loop runs at least once

    while(numbytes==512) {
      numbytes=client.read(requestraw)
      requestraw.flip
      decoder.decode(requestraw,requestpart,false)
      requestpart.flip
      request+=requestpart
      requestpart.clear
      requestraw.compact
    }

    if(request.isEmpty)
      return "HTTP/1.1 400 Bad Request"

    var headers=request.split("\r\n")
    var methodline=headers(0).split(" ")

    httpversion=methodline(2).split("/")(1).split("\\.")

    val response=
      if(httpversion(0).toInt > 1)
        "HTTP/1.1 505 HTTP Version Not Supported\r\n\r\nThis server only supports up to HTTP version 1.1"
      else if(httpversion(1).toInt > 1)
        "HTTP/1.1 505 HTTP Version Not Supported\r\n\r\nThis server only supports up to HTTP version 1.1"
      else
        methodline(0) match {
          case "GET" => doget(methodline(1))
          case "POST" => dopost(methodline(1))
          case "PUT" => doput(methodline(1))
          case "DELETE" => dodelete(methodline(1))
          case _ => "HTTP/1.1 501 Not Implemented"
        }

    return response
  }

  def doget(url:String):String=findroute("GET",url)
  def dopost(url:String):String=findroute("POST",url)
  def doput(url:String):String=findroute("PUT",url)
  def dodelete(url:String):String=findroute("DELETE",url)

  def findroute(method:String,url:String):String= {
    val methodroutes=routesmap.getOrElse(method,Map[String,()=>String]())
    methodroutes.
      filterKeys(pattern => new Regex("^"+pattern+"$").findFirstIn(url).isDefined).
      headOption.
      getOrElse(("",()=>"HTTP/1.1 404 Not Found")).
      _2()
  }

  def receive= {
    case client: SocketChannel =>{
      var response=parseRequest(client)
      client.write(encoder.encode(CharBuffer.wrap(response)))
      client.close
      sender ! RequeueActor(actornum)
    }
  }



}
