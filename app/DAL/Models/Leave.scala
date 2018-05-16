package DAL.Models

import java.sql.Timestamp

case class Leave(id: Long, reason: String, status: String, startDate: Timestamp,
                 endDate: Timestamp, evaluationComment: Option[String],
                 categoryId: Long, userId: Long)
