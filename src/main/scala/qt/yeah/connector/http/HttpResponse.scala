package qt.yeah.connector.http

/**
 * Created by tianhaowei on 15-6-9.
 */
import java.io.OutputStream
import java.io.IOException
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.File
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.Iterator
import javax.servlet.ServletOutputStream
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse
import org.apache.catalina.util.CookieTools
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map


class HttpResponse(output:OutputStream) extends HttpServletResponse{
  var request:HttpRequest = null
  var committed : Boolean = false
  val headers = Map[String,ListBuffer[String]]()

  def setHeader(name :String, value:String): Unit = {
    if (committed)
      return
    val values = ListBuffer[String]()
    values.append(value)
    headers.synchronized{
      headers+=(name->values)
    }
    if (name.equalsIgnoreCase("content-length")) {
      val contentLength = try {
        Integer.parseInt(value)
      }
      catch{
        case _ => -1
      }
      if (contentLength >= 0)
        setContentLength(contentLength)
    }
    else if (name.equalsIgnoreCase("content-type")) {
      setContentType(value)
    }
  }


}
