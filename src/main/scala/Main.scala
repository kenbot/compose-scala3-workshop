import scala.quoted._



object Main {

  def main(args: Array[String]): Unit = {
    //println("size eql size? " + Size.Small.eql(Size.Small)) given Eql[Size]
    println(msg)
  }

  val size = Size.Small
  def msg = s"My size is $size"

  inline def foo(a: Any): Expr[Int] = '{ 1 + 1 }

}


trait HasBanana[A] {
  def (s: A) banana: String 
}

given StringBanana as HasBanana[String] { override def (s: String) banana = "ababababana"}
given String2Banana as HasBanana[String] { override def (s: String) banana = "boonoonoo"}

def handle[A: HasBanana](a: A): String  = a.banana

val large = Size.Large

enum Size {//derives Eql {
  case Small(u: Int)
  case Medium(u: Int)
  case Large(u: Int)
}