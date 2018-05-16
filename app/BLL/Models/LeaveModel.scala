package BLL.Models

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._

case class LeaveModel(id: Option[Long], reason: String, startDate: Timestamp, endDate: Timestamp,
                      var status: Option[String], evaluationComment: Option[String],
                      category: Option[LeaveCategoryModel], user: Option[UserModel])

object LeaveModel {
  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    def reads(json: JsValue): JsSuccess[Timestamp] = {
      val str = json.as[String]
      val parsedDate = new Timestamp(format.parse(str).getTime)
      JsSuccess(parsedDate)
    }

    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  // Need this because Leave Model has reference on user model and NPE error can be caused because UserModel read and writes are not bootstraped on time
  implicit val userModelWrites: Writes[UserModel] = Json.writes[UserModel]
  implicit val userModelReads: Reads[UserModel] = Json.reads[UserModel]

  implicit val leavesFormat: Format[LeaveModel] = Json.format[LeaveModel]
  implicit val vacationsRead: Reads[LeaveModel] = Json.reads[LeaveModel]
  implicit val vacationsWrite: Writes[LeaveModel] = Json.writes[LeaveModel]
}