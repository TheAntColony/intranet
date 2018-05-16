package DAL.Models

import java.sql.Timestamp

case class Project (id: Long, name: String, description: String, url: String, startDate: Timestamp, endDate: Option[Timestamp])
