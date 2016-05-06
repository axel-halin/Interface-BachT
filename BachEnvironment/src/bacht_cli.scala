/* -------------------------------------------------------------------------- 

   Complete code for the command-line interpreter


   AUTHOR : J.-M. Jacquet and D. Darquennes
   DATE   : March 2016

----------------------------------------------------------------------------*/

class Expr {
  def remove(expression:bacht_ast_primitive) = this
}
case class bacht_ast_empty_agent() extends Expr
case class bacht_ast_primitive(primitive: String, token: String) extends Expr {
  /**
    * Returns the primitive in a structured String
    *
    * @return a String object of the token and its density.
    * @author Axel Halin
    */
  override def toString:String = {
    "[ " + primitive + "(" + token + ")" + " ]"
  }

  /**
    * Returns a bacht_ast_empty_agent if expression matches the bacht_ast_primitive.
    * Returns the bacht_ast_primitive otherwise.
    *
    * @param expression Expression to compare to.
    * @return bacht_ast_empty_agent if it matches expression; self otherwise.
    */
  override def remove(expression:bacht_ast_primitive) = if (expression.primitive.equals(primitive) && expression.token.equals(token)) new bacht_ast_empty_agent
                                                        else this
}
case class bacht_ast_agent(op: String, agenti: Expr, agentii: Expr) extends Expr{
  /**
    * Returns the agent in a structured String
    *
    * @return a String object of the operator between the two subagents
    * @author Axel Halin
    */
  override def toString:String = {
    "[ " + agenti.toString + " " + op + " " + agentii.toString + " ]"
  }

  /**
    * Removes the occurence of expression from the bacht_ast_agent
    *
    * @param expression Expression to remove
    * @return The bacht_ast_agent without expression.
    * @author Axel Halin
    */
  override def remove(expression:bacht_ast_primitive) = {
    op match {
      case ";" => if (agenti.remove(expression).isInstanceOf[bacht_ast_empty_agent]) agentii
                  else bacht_ast_agent(op, agenti, agentii.remove(expression))
      case _ =>     bacht_ast_agent(op, agenti.remove(expression), agentii.remove(expression))
    }

  }
}

import scala.util.parsing.combinator._
import scala.util.matching.Regex

class BachTParsers extends RegexParsers {

  def token 	: Parser[String] = ("[a-z][0-9a-zA-Z_]*").r ^^ {_.toString}

  val opChoice  : Parser[String] = "+" 
  val opPara    : Parser[String] = "||"
  val opSeq     : Parser[String] = ";" 
 
  def primitive : Parser[Expr]   = "tell("~token~")" ^^ {
        case _ ~ vtoken ~ _  => bacht_ast_primitive("tell",vtoken) }  | 
                                   "ask("~token~")" ^^ {
        case _ ~ vtoken ~ _  => bacht_ast_primitive("ask",vtoken) }   | 
                                   "get("~token~")" ^^ {
        case _ ~ vtoken ~ _  => bacht_ast_primitive("get",vtoken) }   | 
                                   "nask("~token~")" ^^ {
        case _ ~ vtoken ~ _  => bacht_ast_primitive("nask",vtoken) }

  def agent = compositionChoice

  def compositionChoice : Parser[Expr] = compositionPara~rep(opChoice~compositionChoice) ^^ {
        case ag ~ List() => ag
        case agi ~ List(op~agii)  => bacht_ast_agent(op,agi,agii) }

  def compositionPara : Parser[Expr] = compositionSeq~rep(opPara~compositionPara) ^^ {
        case ag ~ List() => ag
        case agi ~ List(op~agii)  => bacht_ast_agent(op,agi,agii) }

  def compositionSeq : Parser[Expr] = simpleAgent~rep(opSeq~compositionSeq) ^^ {
        case ag ~ List() => ag
        case agi ~ List(op~agii)  => bacht_ast_agent(op,agi,agii) }

  def simpleAgent : Parser[Expr] = primitive | parenthesizedAgent

  def parenthesizedAgent : Parser[Expr] = "("~>agent<~")"

}

object BachTSimulParser extends BachTParsers {

  // return nothing instead of @author Axel Halin

  def parse_primitive(prim: String) = parseAll(primitive,prim) match {
        case Success(result, _) => result
        //case failure : NoSuccess => scala.sys.error(failure.msg)
        case failure : NoSuccess => null
  }

  def parse_agent(ag: String) = parseAll(agent,ag) match {
        case Success(result, _) => result
        //case failure : NoSuccess => scala.sys.error(failure.msg)
        case failure:NoSuccess => null
  }

}
import scala.collection.mutable.Map
import scala.swing._

class BachTStore {

   var theStore = Map[String,Int]()

   def tell(token:String):Boolean = {
      if (theStore.contains(token)) 
        { theStore(token) = theStore(token) + 1 }
      else
        { theStore = theStore ++ Map(token -> 1) }
      true
   }


   def ask(token:String):Boolean = {
      if (theStore.contains(token)) 
             if (theStore(token) >= 1) { true }
             else { false }
      else false
   }


   def get(token:String):Boolean = {
      if (theStore.contains(token)) 
             if (theStore(token) >= 1) 
               { theStore(token) = theStore(token) - 1 
                 true 
               }
             else { false }
      else false
   }


   def nask(token:String):Boolean = {
      if (theStore.contains(token)) 
             if (theStore(token) >= 1) { false }
             else { true }
      else true 
   }

   def print_store {
      print("{ ")
      for ((t,d) <- theStore) 
         print ( t + "(" + theStore(t) + ")" )
      println(" }")
   }

   def clear_store {
      theStore = Map[String,Int]()
   }

  /**
    * Returns the content of the BlackBoard as a String
    *
    * @author Axel Halin
    */
  def getContent:String ={
      var res = ""
      for((t,d) <- theStore) res += t+"("+d.toString+")"
      res = "{" + res + "}"
      res
  }

  /**
    * Returns the density of the specified token.
    * If the token is not in the Store, returns 0
    *
    * @param token Token to search in the store
    * @return The density of the token
    * @author Axel Halin
    */
  def findDensity(token:String):Int = {
      try{
          val density = theStore(token)
          density
      } catch{
        case e:java.util.NoSuchElementException => 0
      }
  }
}

object bb extends BachTStore {

   def reset { clear_store }

}
import scala.util.Random
import language.postfixOps

class BachTSimul(var bb: BachTStore) {

   val bacht_random_choice = new Random()

   def run_one(agent: Expr):(Boolean,Expr) = {

      agent match {
         case bacht_ast_primitive(prim,token) => 
            {  if (exec_primitive(prim,token)) { (true,bacht_ast_empty_agent()) }
               else { (false,agent) }
            }

         case bacht_ast_agent(";",ag_i,ag_ii) =>
            {  run_one(ag_i) match
                  { case (false,_) => (false,agent)
                    case (true,bacht_ast_empty_agent()) => (true,ag_ii)                 
                    case (true,ag_cont) => (true,bacht_ast_agent(";",ag_cont,ag_ii))
                  }
            }

         case bacht_ast_agent("||",ag_i,ag_ii) =>
            {  var branch_choice = bacht_random_choice.nextInt(2) 
               if (branch_choice == 0) 
                 { run_one( ag_i ) match
                      { case (false,_) => 
                          { run_one( ag_ii ) match
                              { case (false,_)
                                  => (false,agent)
                                case (true,bacht_ast_empty_agent())
                                  => (true,ag_i)                 
                                case (true,ag_cont)
                                  => (true,bacht_ast_agent("||",ag_i,ag_cont))
                              }
                          }
                        case (true,bacht_ast_empty_agent())
                                  => (true,ag_ii)                 
                        case (true,ag_cont) 
                                  => (true,bacht_ast_agent("||",ag_cont,ag_ii))
                      }
                  }
               else
                 { run_one( ag_ii ) match
                      { case (false,_) => 
                          { run_one( ag_i ) match
                              { case (false,_) => (false,agent)
                                case (true,bacht_ast_empty_agent()) => (true,ag_ii)                 
                                case (true,ag_cont)
                                      => (true,bacht_ast_agent("||",ag_cont,ag_ii))
                              }
                          }
                        case (true,bacht_ast_empty_agent()) 
                          => (true,ag_i)                 
                        case (true,ag_cont)
                          => (true,bacht_ast_agent("||",ag_i,ag_cont))
                      }
                  }
             
            }


         case bacht_ast_agent("+",ag_i,ag_ii) =>
            {  var branch_choice = bacht_random_choice.nextInt(2) 
               if (branch_choice == 0) 
                 { run_one( ag_i ) match
                      { case (false,_) => 
                          { run_one( ag_ii ) match
                              { case (false,_) => (false,agent)
                                case (true,bacht_ast_empty_agent())
                                  => (true,bacht_ast_empty_agent())                 
                                case (true,ag_cont)
                                  => (true,ag_cont)
                              }
                          }
                        case (true,bacht_ast_empty_agent())
                          => (true,bacht_ast_empty_agent())                 
                        case (true,ag_cont) 
                          => (true,ag_cont)
                      }
                  }
               else
                 { run_one( ag_ii ) match
                      { case (false,_) => 
                          { run_one( ag_i ) match
                              { case (false,_) 
                                  => (false,agent)
                                case (true,bacht_ast_empty_agent())
                                  => (true,bacht_ast_empty_agent())                 
                                case (true,ag_cont)
                                  => (true,ag_cont)
                              }
                          }
                        case (true,bacht_ast_empty_agent())
                          => (true,bacht_ast_empty_agent())                 
                        case (true,ag_cont)
                          => (true,ag_cont)
                      }
                  }
            }
      }
   }

   def bacht_exec_all(agent: Expr):Boolean = {

       var failure = false
       var c_agent = agent
       while ( c_agent != bacht_ast_empty_agent() && !failure ) {
          failure = run_one(c_agent) match 
               { case (false,_)          => true
                 case (true,new_agent)  => 
                    { c_agent = new_agent
                      false
                    }
               }
           bb.print_store
           println("\n")
       }
       
       if (c_agent == bacht_ast_empty_agent()) {
           println("Success\n")
           true
       }
       else {
           println("failure\n")
           false
       }
   }  

   def exec_primitive(prim:String,token:String):Boolean = {
       prim match
         { case "tell" => bb.tell(token)
           case "ask"  => bb.ask(token)
           case "get"  => bb.get(token)
           case "nask" => bb.nask(token)
         }
   }

}

object ag extends BachTSimul(bb) {

  def apply(agent: String) {
     val agent_parsed = BachTSimulParser.parse_agent(agent)
     ag.bacht_exec_all(agent_parsed) 
  }
  def eval(agent:String) { apply(agent) }
  def run(agent:String) { apply(agent) }
}
         
