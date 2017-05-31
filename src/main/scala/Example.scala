import java.util.concurrent.Executors

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


import org.atnos.eff.{Fx, TimedFuture}
import org.atnos.eff.syntax.all._
import org.atnos.eff.syntax.future._

import FutureInterpreter.ToyState

object Example1 {
  type Stack = Fx.fx3[Toy, ToyState, TimedFuture]
  implicit val scheduler = Executors.newScheduledThreadPool(1)

  def main(args: Array[String]): Unit = {

    val vs = 1 to 20 map(_.toString) toList

    val x = Toy.groupCycleA[Stack](vs, 5)

    println(Await.result(
      FutureInterpreter.runToy(x)
        .runState(Map.empty[String, String])
        .runAsync,
      10 minute
    ))
  }
}