import java.awt.{Dimension, Font}

import scala.swing._


/**
  * Created by Axel on 23-04-16.
  */
class AutonomousAgentGUI extends MainFrame {

  // TODO
  // 3 label (1 in scrollable view ??); 1 TextArea; 3 buttons

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
                            text = "(tell(t,3);get(v,2)) || (ask(u,5);tell(w,6))"
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

  override def closeOperation : scala.Unit = this.close()
}
