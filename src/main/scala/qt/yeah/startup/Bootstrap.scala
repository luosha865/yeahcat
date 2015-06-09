package qt.yeah.startup

/**
 * Created by tianhaowei on 15-6-9.
 */
import qt.yeah.connector.http.HttpConnector

object Bootstrap {

  def main(args: Array[String]): Unit = {
    def connector = new HttpConnector()
    connector.start()
  }

}
