package WebApi.Models

import play.api.libs.json.{Json, Reads, Writes}

case class JwtToken(token: String, schema: String, expires_in: Option[Int])

object JwtToken {
  implicit val credentialsRead: Reads[JwtToken] = Json.reads[JwtToken]
  implicit val credentialsWrite: Writes[JwtToken] = Json.writes[JwtToken]
}

