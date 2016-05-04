import java.awt.{Dimension, Font}

import scala.swing._
import scala.swing.event.{ValueChanged, ButtonClicked}

/**
  * Interactive Agent window of the application.
  * This window allows the user to create and run an autonomous agent.
  * The user can choose which primitive to execute (the system only proposes primitives which would lead to a success).
  * In the event of a bad execution (failure returned) a Dialog is displayed.
  * A bad execution would happen only if the blackboard content is altered in another view.
  *
  * Created by Axel on 23-04-16.
  */
class InteractiveAgentGUI(blackboard:BachTStore, blackboardDisplay:TextArea) extends MainFrame {

  var expression = new Expr
  var agent = new BachTSimul(blackboard)

  // Components definition
  val titleFont = new Font("Verdana", java.awt.Font.BOLD, 14)

  val agentTitleLabel = new Label(){
    text = "Agent to be processed"
  }

  val currentAgentLabel = new Label(){
    text = "Current Agent: "
  }

  val currentAgentValue = new Label(){
    tooltip = "Agent to be executed"
    text = ""
  }

  val agentTextArea = new TextArea(){
    editable = true
    preferredSize = new Dimension(100, 100)
    maximumSize = new Dimension(600, 300)
    resizable = false
  }

  val submitButton = new Button(){
    text = "Submit"
    tooltip = "Submit the new Agent"
  }

  val agentLine = new BoxPanel(Orientation.Horizontal)

  title = "Interactive Agent"
  centerOnScreen()
  preferredSize = new Dimension(700, 400)
  resizable = false

  contents = new BoxPanel(Orientation.Vertical){
    contents += new BoxPanel(Orientation.Horizontal){
      contents += agentTitleLabel
    }

    // Vertical Space
    contents += Swing.VStrut(10)

    // Second line: Tokens
    contents += new BoxPanel(Orientation.Horizontal){
      contents += agentTextArea
      contents += Swing.HStrut(15)
      contents += submitButton
    }

    // Vertical Space
    contents += Swing.VStrut(10)

    // Third Line: Buttons
    contents += new BoxPanel(Orientation.Horizontal){
      contents += currentAgentLabel
    }

    contents += Swing.VStrut(3)

    contents += agentLine
  }

  listenTo(submitButton)
  listenTo(blackboardDisplay)

  reactions += {
      case ButtonClicked(component) if component == submitButton
      =>  if (agentTextArea.text == "") Dialog.showMessage(submitButton, "Please enter the value of the agent.", "Error: Agent can't be empty")
          else{
              val agentParsed = BachTSimulParser.parse_agent(agentTextArea.text)
              if (agentParsed == null) Dialog.showMessage(submitButton, "The agent value is incorrect.\nThe agent wasn't recognised","Error: Agent not recognized")
              else {
                  expression = agentParsed
                  agentLine.contents.clear()
                  displayExpression(expression)
                  // Refresh the displaying
                  agentLine.revalidate()
              }
          }
      case ValueChanged(component) if component == blackboardDisplay
      =>  agentLine.contents.clear()
          displayExpression(expression)
          agentLine.revalidate()
  }

  override def closeOperation : scala.Unit = this.close()

  /**
    * Uses pattern matching on case classes to highlight which primitives can be executed.
    */
  private def displayExpression(expression:Expr):Unit = {
      expression match {
          case bacht_ast_empty_agent() => Unit
          case bacht_ast_primitive(primitive: String, token: String) => {
              primitive match{
                case "tell" => agentLine.contents += new Button(primitive + "(" + token +")")
                case "get" =>   val density = blackboard.findDensity(token)
                                if (density > 0) agentLine.contents += new Button(primitive + "(" + token + ")")
                                else agentLine.contents += new Label(primitive + "(" + token + ")")
                case "ask" =>   val density = blackboard.findDensity(token)
                                if (density > 0) agentLine.contents += new Button(primitive + "(" + token + ")")
                                else agentLine.contents += new Label(primitive + "(" + token + ")")
                case "nask" =>  val density = blackboard.findDensity(token)
                                if (density > 0) agentLine.contents += new Label(primitive + "(" + token + ")")
                                else agentLine.contents += new Button(primitive + "(" + token + ")")
              }
          }
          case bacht_ast_agent(op: String, agenti: Expr, agentii: Expr) => ???
          case _ => Dialog.showMessage(submitButton, "An unknown error has occured", "Unknown Error")
      }
  }
}
