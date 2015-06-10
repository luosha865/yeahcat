package qt.yeah.core

import scala.collection.mutable.Map

/**
 * Created by tianhaowei on 15-6-10.
 */
class Routes {

  val routesmap=Map[String,Map[String,()=>String]]()

  def get(urlpattern:String):((=>String)=>Unit)=(x=>addtomap("GET",urlpattern,()=>x))
  def put(urlpattern:String):((=>String)=>Unit)=(x=>addtomap("PUT",urlpattern,()=>x))
  def post(urlpattern:String):((=>String)=>Unit)=(x=>addtomap("POST",urlpattern,()=>x))
  def delete(urlpattern:String):((=>String)=>Unit)=(x=>addtomap("DELETE",urlpattern,()=>x))

  def addtomap(method:String,urlpattern:String,f:()=>String) {
    routesmap += (method->(routesmap.getOrElse(method,Map[String,()=>String]())+(urlpattern->f)))
  }

  def getOrElse(key : String, default: Map[String,()=>String]): Map[String,()=>String] ={
    return routesmap.getOrElse(key,default)
  }
  def routes {
    get("/wibble") {
      "HTTP/1.1 200 OK\r\n\r\nIt works!"
    }
    get("/wobble")
    {
      "HTTP/1.1 410 Gone"
    }
  }
}
