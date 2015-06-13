package qt.yeah.core

import scala.collection.mutable.Map
import scala.util.matching.Regex

/**
 * Created by tianhaowei on 15-6-10.
 */
class Routes {

  val routesmap=Map[String,Map[String,()=>String]]()

  def get(urlpattern:String):((=>String)=>Unit)=(x=>addtomap(urlpattern,"GET",()=>x))
  def put(urlpattern:String):((=>String)=>Unit)=(x=>addtomap(urlpattern,"PUT",()=>x))
  def post(urlpattern:String):((=>String)=>Unit)=(x=>addtomap(urlpattern,"POST",()=>x))
  def delete(urlpattern:String):((=>String)=>Unit)=(x=>addtomap(urlpattern,"DELETE",()=>x))

  def addtomap(urlpattern:String,method:String,f:()=>String) {
    routesmap += (urlpattern->(routesmap.getOrElse(urlpattern,Map[String,()=>String]())+(method->f)))
  }

  def findroute(url:String,method:String):String= {
    val methodroutes=routesmap.filterKeys(pattern => new Regex("^"+pattern+"$").findFirstIn(url).isDefined)
      .headOption.getOrElse(("",Map[String,()=>String]()))._2
    methodroutes.getOrElse(method,()=>"HTTP/1.1 404 Not Found")()
  }

  def getOrElse(key : String, default: Map[String,()=>String]): Map[String,()=>String] ={
    return routesmap.getOrElse(key,default)
  }
  /*
  def routes {
    get("/wibble") {
      "HTTP/1.1 200 OK\r\n\r\nIt works!"
    }
    get("/wobble")
    {
      "HTTP/1.1 410 Gone"
    }
  }
  */
}
