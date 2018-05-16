package BLL.Enums

object LeaveStatusEnum extends Enumeration {
  type LeaveStatusEnum = Value
  val Approved, Pending, Rejected = Value
}
