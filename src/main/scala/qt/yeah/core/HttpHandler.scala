package qt.yeah.core

/**
 * Created by tianhaowei on 15-6-13.
 */
class HttpHandler {
  def get():(String)= {"HTTP/1.1 405 Method not allowed"}
  def put():(String)= {"HTTP/1.1 405 Method not allowed"}
  def post():(String)= {"HTTP/1.1 405 Method not allowed"}
  def delete():(String)= {"HTTP/1.1 405 Method not allowed"}
}
