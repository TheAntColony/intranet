package WebApi.Models

import play.api.libs.json.{Json, Reads, Writes}

case class UserJwtPayload(email: String, userId: Long, role: String)

object UserJwtPayload {
  implicit val objectReads: Reads[UserJwtPayload] = Json.reads[UserJwtPayload]
  implicit val objectWrites: Writes[UserJwtPayload] = Json.writes[UserJwtPayload]
}
