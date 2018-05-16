package BLL.Models

import play.api.libs.json.{Json, Reads, Writes}

case class SkillModel (id: Option[Long], name: String, description: String, level: Option[String], yearsExperience: Option[Short])

object SkillModel {
  implicit val read: Reads[SkillModel] = Json.reads[SkillModel]
  implicit val write: Writes[SkillModel] = Json.writes[SkillModel]
}