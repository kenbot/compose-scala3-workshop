package scala3workshop.ex6_inline


// There is a new `inline` keyword, which forces (not recommends - forces!) the compiler to recompile 
// the code inline at every use-site.
//
// This exists for 2 reasons:
// 1. Advanced users can, with care, optimise code to remove indirection costs.
// 2. The inline functionality enables Scala 3's macro system.

// To experiment with the code in this chapter, either run `console` in sbt and 
// scala> import scala3workshop.ex6_inline._
// or run your arbitrary code here with `run`, selecting `Ex6`.
object Ex6 {
  def main(args: Array[String]): Unit = {
    // Put whatever code you want here to play around with the stuff in this chapter
  }
}


// Ex 6.1 - Inline methods
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

// The contents of Logger.log will be compiled in-place for every use-site; 
// but `Logger.indent` is private! 
// How do you think the compiler might handle this if an external module calls `log`?


// Ex 6.2 - Modularity with inline 
//
// Run `clean` and `compile` in sbt. 
// 
// Make a trivial whitespace change to this file, and `compile` again in sbt. 
// It should tell you that only 1 source file was compiled.
//
// Notice that elsewhere in this package, `TotallyDifferentFile.somethingElse` 
// uses the inline `Logger.log` method. 
//
// Now, rename the private `Logger.indent` variable to something else like `indenty`, 
// correcting the references inside the method. 
// Run `compile` again in sbt. How many files were compiled this time?


// Ex 6.3 - Modularity without inline
// 
// If you delete the `inline` modifier of `Logger.log` and repeat the steps in 6.2, 
// how many files get recompiled by renaming the private var this time?
//
// What does this tell you about the modularity of data referred to by inline methods?


// Ex 6.4 - Recursion
inline def power(x: Long, n: Int): Long = {
 inline if (n == 0) 
    1L
  else inline if (n % 2 == 1)
    x * power(x, n-1)
  else {
    val y: Long = x * x
    power(y, n / 2)
  }
}

// This inline method calculates the power of two numbers - note that it is recursive.
// What will this do to the generated code at a use-site?


// Ex 6.5 - Infinite recursion
// 
// If you replace 1L with power(x,n) in the inline `power` method, it will never terminate. 
// What do you think will happen if you compile this? What actually happens?


// Ex 6.6 - Inlining statically-known args
//
// (Undo the changes in 6.5)
// Try running, say, `power(5,2)`; as you might expect, 25 will be the answer. 
// Now try running `val n = 2; power(5,n)`. What happens?


// Ex 6.7 - Inline parameters
//
// Parameters can be marked as `inline`, which means that the compiler has to be able to statically calculate the arguments at the callsite.
// 
// Now put another `inline` in, for the parameter `n` in `power`:
//
// inline def power(x: Long, inline n: Int): Long 
//
// How does this change the error message?


// Ex 6.8 - Inline if
//
// Declaring `if` statements as `inline` means that the compiler has to be able to statically calculate the condition as true or false.
//
// Take away the `inline` before n; insert `inlines` before if:
// inline if (n == 0) 
// ...
// else inline if (n % 2 == 1)
//
// How does this change the error message, running with power(5,2) and power(5,n)?


// Ex 6.9 - Inline match
// 
// Declaring `match` statements as `inline` means that the compiler has to be able to statically calculate the branch dispatches.

/*
sealed trait Nat

case object Zero extends Nat
case class Succ[N <: Nat](n: Nat) extends Nat

enum Natx { // Peano numbers
  case Zero
  case Succ[N <: Natx](n: N)
}

inline def natToInt(n: Nat): Int = inline n match {
  case Zero => 0
  case Succ(n1) => natToInt(n1) + 1
}

val natTwo: Int = natToInt(Succ(Succ(Zero))) */

// Define the natToInt function above using pattern matching, injecting Nat into Int.
