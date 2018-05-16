package BLL.Enums

// I tough adding roles via configuration but startup service will be called once when server is started so it is better to add it as
// enum as I will need to restart server for changes to take place and it is easier to work with enum in code then seq of string
object RoleEnum extends Enumeration {
  type RoleEnum = Value
  val Employee, Admin, Owner, Consultant = Value
}
