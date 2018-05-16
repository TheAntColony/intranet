package BLL.Models

case class OperationResult[R](isSuccess: Boolean, message: String, result: R)