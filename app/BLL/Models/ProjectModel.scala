package BLL.Models

import java.sql.Timestamp
import java.text.SimpleDateFormat

import play.api.libs.json._

case class ProjectModel (id: Option[Long], name: String, description: String, url: String,
                         startDate: Timestamp, endDate: Option[Timestamp], skills: Option[Seq[SkillModel]])

object ProjectModel {

  implicit object timestampFormat extends Format[Timestamp] {
    val format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    def reads(json: JsValue): JsSuccess[Timestamp] = {
      val str = json.as[String]
      val parsedDate = new Timestamp(format.parse(str).getTime)
      JsSuccess(parsedDate)
    }

    def writes(ts: Timestamp) = JsString(format.format(ts))
  }

  implicit val projectWrites: Writes[ProjectModel] = Json.writes[ProjectModel]
  implicit val projectReads: Reads[ProjectModel] = Json.reads[ProjectModel]
}
