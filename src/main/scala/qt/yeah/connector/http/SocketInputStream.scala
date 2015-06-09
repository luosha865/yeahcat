package qt.yeah.connector.http

/**
 * Created by tianhaowei on 15-6-9.
 */


import java.io.IOException
import java.io.InputStream
import java.io.EOFException
import org.apache.catalina.util.StringManager


class SocketInputStream(is:InputStream, bufferSize: Int) extends InputStream {
  val buf = new Array[Byte](bufferSize)


}
