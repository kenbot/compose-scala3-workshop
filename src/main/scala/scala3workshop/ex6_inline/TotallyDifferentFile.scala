package scala3workshop.ex6_inline

object TotallyDifferentFile {
  def somethingElse(): Unit = {
    Logger.log("Whoa!")(1 + 1)
  }
}