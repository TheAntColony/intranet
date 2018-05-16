package DAL.Models

case class UserInterestingInfo(id: Long, name: String, description: String, url: Option[String], userId: Long)