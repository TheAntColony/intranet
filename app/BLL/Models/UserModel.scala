package BLL.Models

import play.api.libs.json.{Json, Reads, Writes}
import com.mohiva.play.silhouette.api.LoginInfo

case class UserModel(id: Option[Long],
                     firstName: String,
                     lastName: String,
                     // loginInfo: LoginInfo,
                     email: String,
                     password: Option[String],
                     detail: Option[UserDetailModel], skills: Option[Seq[SkillModel]],
                     projects: Option[Seq[ProjectModel]], leaves: Option[Seq[LeaveModel]])

object UserModel {
  implicit val userModelWrites: Writes[UserModel] = Json.writes[UserModel]
  implicit val userModelReads: Reads[UserModel] = Json.reads[UserModel]
}