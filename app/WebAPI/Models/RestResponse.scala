package WebApi.Models

import play.api.libs.json._

case class RestResponse(data : JsValue, error: Option[String]) {
  def toJson:JsValue = {
    Json.toJson(this)
  }
}


object RestResponse {
  implicit val read: Reads[RestResponse] = Json.reads[RestResponse]
  implicit val write: Writes[RestResponse] = Json.writes[RestResponse]
}
