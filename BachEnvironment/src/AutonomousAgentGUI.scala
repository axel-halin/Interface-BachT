import java.awt.{Dimension, Font}

import scala.swing._
import scala.swing.event.ButtonClicked


/**
  * Created by Axel on 23-04-16.
  */
class AutonomousAgentGUI(blackboard:BachTStore, blackboardDisplay:TextArea) extends MainFrame {

  var agent = new BachTSimul(blackboard)
  var expression = new Expr

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
                        maximumSize = new Dimension(500, 300)
                        resizable = false
                      }

  val submitButton = new Button(){
                        text = "Submit"
                        tooltip = "Submit the new Agent"
                    }

  val runButton = new Button(){
                    text = "Run"
                    tooltip = "Run all primitives of the agent"
                  }

  val nextButton = new Button(){
                    text = "Next"
                    tooltip = "Run next primitive"
                  }



  title = "Autonomous Agent"
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
        contents += Swing.HStrut(10)
        contents += currentAgentValue
    }

    // Vertical Space
    contents += Swing.VStrut(5)

    contents += new BoxPanel(Orientation.Horizontal){
      contents += runButton
        contents += Swing.HStrut(5)
        contents += nextButton
    }
  }

  listenTo(submitButton)
  listenTo(nextButton)
  listenTo(runButton)

  reactions += {
    case ButtonClicked(component) if component == submitButton
    =>  // Check the input content
        if(agentTextArea.text == ""){
            Dialog.showMessage(submitButton,"Please enter the value of the agent","Error: Agent can't be empty")
        } else{
            val agentParsed = BachTSimulParser.parse_agent(agentTextArea.text)
            if (agentParsed == null) Dialog.showMessage(submitButton,"The agent value is incorrect.\nThe agent wasn't recognised.", "Error: Agent not recognized")
            else {currentAgentValue.text = agentParsed.toString; expression = agentParsed}
        }
    case ButtonClicked(component) if component == nextButton
    =>  if (currentAgentValue.text == "") Dialog.showMessage(nextButton, "The agent is empty. It cannot be run", "Error: Agent is empty")
        else{
            val res = agent.run_one(expression)
            if (res._1) {
                expression = res._2
                if (!expression.isInstanceOf[bacht_ast_empty_agent]){
                    currentAgentValue.text = expression.toString
                }
                else currentAgentValue.text = ""
                blackboardDisplay.text = blackboard.getContent
            }
            else Dialog.showMessage(nextButton, "The execution of the next primitive returns a failure.","Error: next primitive failure")
        }
    case ButtonClicked(component) if component == runButton
    => {}
  }

  override def closeOperation : scala.Unit = this.close()
}
