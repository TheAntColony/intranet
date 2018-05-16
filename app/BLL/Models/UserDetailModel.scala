package BLL.Models

import play.api.libs.json.{Json, Reads, Writes}

case class UserDetailModel (id: Option[Long], description: String, country: String, religion:String,
                       height: Double, weight: Double, skin: String, hair: String, gender: String,
                            age: Short, var userId: Option[Long])

object UserDetailModel {
  implicit val userDetailModelReads: Reads[UserDetailModel] = Json.reads[UserDetailModel]
  implicit val userDetailModelWrites: Writes[UserDetailModel] = Json.writes[UserDetailModel]
}