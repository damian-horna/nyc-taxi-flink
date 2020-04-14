package sink

import datatypes.DeparturesArrivalsAggResult
import org.apache.flink.api.common.functions.RuntimeContext
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

class ElasticSystemImageSink extends ElasticsearchSinkFunction[DeparturesArrivalsAggResult] {
  def process(element: DeparturesArrivalsAggResult, ctx: RuntimeContext, indexer: RequestIndexer) {
    val json = new java.util.HashMap[String, Any]
    json.put("hour", element.hour)
    json.put("borough", element.borough)
    json.put("day", element.day)
    json.put("arrivals_count", element.arrivalsCnt)
    json.put("departures_count", element.departuresCnt)
    json.put("arriving_people_count", element.arrivingPeopleCnt)
    json.put("departing_people_count", element.departingPeopleCnt)

    val rqst: IndexRequest = Requests.indexRequest
      .index("nyc-index")
      .source(json)

    indexer.add(rqst)
  }
}