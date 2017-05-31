import cats.data.State

import org.atnos.eff.{|=, Eff, Member, Translate}
import org.atnos.eff.Interpret.translate
import org.atnos.eff.future._
import org.atnos.eff.all._

object FutureInterpreter {
  type ToyState[A] = State[Map[String, String], A]
  type _toyState[R] = ToyState |= R

  def runToy[R, U, A](effects: Eff[R, A])(
    implicit m: Member.Aux[Toy, R, U], 
    state: _toyState[U], 
    future: _future[U]
  ): Eff[U, A] =
    translate(effects)(new Translate[Toy, U]{
      def apply[T](cmd: Toy[T]): Eff[U, T] = cmd match {
        case Getting(k)    => { 
          println(s"Calling get with $k")
          gets[U, Map[String, String], Option[String]](m => m.get(k))
        }
        case Setting(k, v) => {
          println(s"Calling set with $k and $v")
          modify[U, Map[String, String]](m => m + ((k, v)))
        }
        case Doing(v) => futureDelay {
          println(s"Going to sleep for $v secs.")
          Thread.sleep(v * 1000)
          println(s"Waking up after sleeping for $v secs.")
          s"Done blocking for $v secs"
        }
      }
    })
}
