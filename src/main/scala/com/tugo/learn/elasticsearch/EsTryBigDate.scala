package com.tugo.learn.elasticsearch

import java.util.Date

object EsTryBigDate {
  def main(args: Array[String]): Unit = {
    import io.searchbox.client.JestClientFactory
    import io.searchbox.client.config.HttpClientConfig
    val factory = new JestClientFactory
    factory.setHttpClientConfig(
      new HttpClientConfig.Builder("http://localhost:9200")
        .multiThreaded(true)
        .defaultMaxTotalConnectionPerRoute(2)
        .maxTotalConnection(10)
        .build)
    val jestClient = factory.getObject()
    import io.searchbox.core.Index
    import java.util
    val doc = new util.HashMap[String, AnyRef]()
    doc.put("message", "message from jest library")
    doc.put("indexTime", new Date());
    val t = 1628238043999006467L;
    doc.put("rawIndexTime", new Date(t))
    try {
      val result = jestClient.execute(new Index.Builder(doc).index("exabeam-test1").`type`("logs").build)
      print(s"Result ${result}")
    } catch {
      case ex: Exception => {
        println("Error while sending document to the index")
      }
    }
    jestClient.close()
  }
}
