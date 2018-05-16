package DAL.Models

import com.mohiva.play.silhouette.api.{ Identity, LoginInfo }
import com.mohiva.play.silhouette.api.util.PasswordInfo

case class User(id: Long,
                firstName: String,
                lastName: String,
                email: String,
                isVerified: Boolean,
                isDisabled: Boolean,
                // loginInfo: LoginInfo,
                password: Option[String]) extends Identity

/*
class User(_name: String, _duration: String, _octave: Int) extends Serializable {

  // Constructor parameters are promoted to members
  val name = _name
  val duration = _duration
  val octave = _octave

  // Equality redefinition
  override def equals(other: Any): Boolean = other match {
    case that: Note =>
      (that canEqual this) &&
        name == that.name &&
        duration == that.duration &&
        octave == that.octave
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Note]

  // Java hashCode redefinition according to equality
  override def hashCode(): Int = {
    val state = Seq(name, duration, octave)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  // toString redefinition to return the value of an instance instead of its memory addres
  override def toString = s"Note($name, $duration, $octave)"

  // Create a copy of a case class, with potentially modified field values
  def copy(name: String = name, duration: String = duration, octave: Int = octave): Note =
    new Note(name, duration, octave)

}

object Note {

  // Constructor that allows the omission of the `new` keyword
  def apply(name: String, duration: String, octave: Int): Note =
    new Note(name, duration, octave)

  // Extractor for pattern matching
  def unapply(note: Note): Option[(String, String, Int)] =
    if (note eq null) None
    else Some((note.name, note.duration, note.octave))

}
*/
