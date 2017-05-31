
import cats.implicits._

import org.atnos.eff.{Eff, |=}
import org.atnos.eff.all._

sealed trait Toy[A]
final case class Getting(key: String)                extends Toy[Option[String]]
final case class Setting(key: String, value: String) extends Toy[Unit]
final case class Doing(block: Int)                   extends Toy[String]

object Toy {
  type _toy[R] = Toy |= R

  def get[R  : _toy](key: String): Eff[R, Option[String]] =
    Eff.send(Getting(key))

  def set[R  : _toy](k: String, v: String): Eff[R, Unit] =
    Eff.send(Setting(k, v))

  def doing[R  : _toy](block: Int): Eff[R, String] =
    Eff.send(Doing(block))

  def cycle[R  : _toy](x: String, blockTime: Int): Eff[R, String] = for {
    v <- get(x)
    _ <- if (v.isDefined) pure[R, Unit](()) else set(x, x)
    b <- doing(blockTime)
  } yield b

  def groupCycleA[R : _toy](xs: List[String], blockTime: Int): Eff[R, List[String]] =
    Eff.traverseA(xs)(cycle[R](_, blockTime))
}
