package qt.yeah.connector.http

/**
 * Created by tianhaowei on 15-6-9.
 */

import java.net.Socket
import java.io.OutputStream
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.Cookie

import org.apache.catalina.util.RequestUtil
import org.apache.catalina.util.StringManager


class HttpProcessor(connector: HttpConnector) {


  def process(socket : Socket) :Unit = {
    try {
      val input = new SocketInputStream(socket.getInputStream(), 2048)
      val output = socket.getOutputStream()

      // create HttpRequest object and parse
      val request = new HttpRequest(input)

      // create HttpResponse object
      val response = new HttpResponse(output)
      response.request = request

      response.setHeader("Server", "Pyrmont Servlet Container")

      parseRequest(input, output)
      parseHeaders(input)

      //check if this is a request for a servlet or a static resource
      //a request for a servlet begins with "/servlet/"
      if (request.getRequestURI().startsWith("/servlet/")) {
        val processor = new ServletProcessor()
        processor.process(request, response)
      }
      else {
        val processor = new StaticResourceProcessor()
        processor.process(request, response)
      }
      // Close the socket
      socket.close();
      // no shutdown for this application
    }
    catch{
      case _ =>
    }
  }


}
