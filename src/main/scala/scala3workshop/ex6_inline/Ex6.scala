package scala3workshop.ex6_inline


// There is a new `inline` keyword, which forces (not recommends - forces!) the compiler to recompile the code inline at every use-site.
//
// This exists for 2 reasons:
// 1. Advanced users can, with care, optimise code to remove indirection costs.
// 2. The inline functionality enables Scala 3's macro system.

// To experiment with the code in this chapter, either run `console` in sbt and 
// scala> import scala3workshop.ex6_inline._
// or run your arbitrary code here with `run`, selecting `Ex6`.
object Ex6 {
  def main(args: Array[String]): Unit = {
    // Put whatever code you want here to play around with the stuff chapter
  }
}


// Ex 6.1
object Logger {
  private var indent = 0
  
  inline def log[T](msg: String)(thunk: => T): T = {
    println(s"${"  " * indent}start $msg")
    indent += 1
    val result = thunk
    indent -= 1
    println(s"${"  " * indent}$msg = $result")
    result
  }
}

// The contents of Logger.log will be compiled in-place for every use-site; but `Logger.indent` is private! 
// How do you think the compiler might handle this if an external module calls `log`?


// Ex 6.2 
// Run `clean` and `compile` in sbt. 
// 
// Make a trivial whitespace change to this file, and `compile` again in sbt. It should tell you that only 1 source file was compiled.
//
// Notice that elsewhere in this package, `TotallyDifferentFile.somethingElse` uses the inline `Logger.log` method. 
//
// Now, rename the private `Logger.indent` variable to something else like `indenty`, correcting the references inside the method. 
// Run `compile` again in sbt. How many files were compiled this time?


// Ex 6.3
// If you delete the `inline` modifier of `Logger.log` and repeat the steps in 6.2, 
// how many files get recompiled by renaming the private var this time?
//
// What does this tell you about the modularity of data referred to by inline methods?


// Ex 6.4
inline def power(x: Long, n: Int): Long = {
  if (n == 0) 
    1L
  else if (n % 2 == 1)
    x * power(x, n-1)
  else {
    val y: Long = x * x
    power(y, n / 2)
  }
}

// This inline method calculates the power of two numbers - note that it is recursive.
// What will this do to the generated code at a use-site?

// Ex 6.5
// If you replace 1L with power(x,n) in the inline `power` method, it will never terminate. 
// What do you think will happen if you compile this?

// Ex 6.6 
// What actually happened when you attempted to compile 6.5?

// Ex 6.7 (inline parameter)