package BLL.Models

import play.api.libs.json.{Json, Reads, Writes}

case class UserSkillModel (userId: Long, skillId: Long, level: String, yearsExperience: Short)


object UserSkillModel {
  implicit val userModelWrites: Writes[UserSkillModel] = Json.writes[UserSkillModel]
  implicit val userModelReads: Reads[UserSkillModel] = Json.reads[UserSkillModel]
}