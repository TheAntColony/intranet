package BLL.Models

import play.api.libs.json.{Json, OWrites, Reads}

case class InterestingInfoModel(id: Option[Long], name: String, description: String, url: Option[String])

object InterestingInfoModel {
  implicit val vacationsRead: Reads[InterestingInfoModel] = Json.reads[InterestingInfoModel]
  implicit val vacationsWrite: OWrites[InterestingInfoModel] = Json.writes[InterestingInfoModel]
}
