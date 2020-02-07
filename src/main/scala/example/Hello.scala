package example

import com.softwaremill.diffx.Diff

object TestError extends App {
  type @@[T, TAG] = T with TAG

  trait TaggedType[T, TAG] {
    private val tagger: TaggedType.Tagger[TAG] = TaggedType[TAG]
    def apply(t: T): T @@ TAG                  = tagger(t)
  }

  object TaggedType {
    class Tagger[TAG] {
      @SuppressWarnings(Array("asinstanceof"))
      def apply[T](t: T): @@[T, TAG] = t.asInstanceOf[@@[T, TAG]]
    }

    def apply[TAG]: Tagger[TAG] = new Tagger
  }

  trait Minute
  object Minute extends TaggedType[Int, Minute]

  case class Foo(x: Int @@ Minute)

  val foo = Foo(Minute(5))

  Diff[Foo].apply(foo, foo)
}
