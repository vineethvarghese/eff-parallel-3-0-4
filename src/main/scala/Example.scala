import java.util.concurrent.Executors

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global


import org.atnos.eff.{Fx, TimedFuture, ExecutorServices}
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

// /** No concurrency. Uses Eff.traverseA */
// object Example01 {
//   def main(args: Array[String]): Unit = {
//     type Stack = Fx.fx2[ToyState, Toy]

//     val x = Toy.groupCycleA[Stack](List("a", "b", "c"))

//     // should be List(N/A, N/A, N/A)
//     println(
//       ToyStateInterpreter.runToy(x)
//         .runState(SimState(List.empty, Map.empty))
//         .run
//         ._2.actions
//     )
//   }
// }


// /** Uses the future effect. */
// object Example1 {
//   implicit val sexs = Executors.newScheduledThreadPool(3)
//   //implicit val ec   = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))

//   def main(args: Array[String]): Unit = {
//     type Stack = Fx.fx3[TimedFuture, ToyState, Toy]

//     val x = Toy.parGroupCycle[Stack](List("a", "b", "c"))

//     // should be List(N/A, N/A, N/A)
//     println(Await.result(
//       ToyStateInterpreter.runToy(x)
//         .runState(SimState(List.empty, Map.empty))
//         .runAsync,
//       1 minute
//     )._2.actions)
//   }
// }

// /** Uses future explicitly without the future effect. */
// object Example2 {
//   implicit val sexs = Executors.newScheduledThreadPool(3)
//   //implicit val ec   = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))

//   def main(args: Array[String]): Unit = {
//     type Stack = Fx.fx2[ToyState, Toy]

//     val x = Toy.parGroupCycle2[Stack](List("a", "b", "c"))

//     // should be List(N/A, N/A, N/A)
//     println(
//       ToyStateInterpreter.runToy(x)
//         .runState(SimState(List.empty, Map.empty))
//         .run
//         ._2.actions
//     )
//   }
// }

// /** Uses the future effect. */
// object Example3 {
//   implicit val sexs = Executors.newScheduledThreadPool(3)
//   //implicit val ec   = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(5))

//   def main(args: Array[String]): Unit = {
//     type Stack = Fx.fx3[TimedFuture, ToyState, Toy]

//     val x = Toy.groupCycleA[Stack](List("a", "b", "c"))

//     // should be List(N/A, N/A, N/A)
//     println(Await.result(
//       FutureInterpreter.runToy(x)
//         .runState(SimState(List.empty, Map.empty))
//         .runAsync,
//       1 minute
//     )._2.actions)
//   }
// }

