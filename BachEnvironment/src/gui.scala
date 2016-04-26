import java.awt.Color

import scala.swing._
import scala.swing.event.ButtonClicked
import swing.Swing

/**
  * Created by Axel on 21-04-16.
  */
object gui extends SimpleSwingApplication {

  // Components definition
  val titleFont = new Font("Verdana", java.awt.Font.BOLD, 14)

  val contentLabel = new Label(){
                      name = "contentLabel"
                      text = "Content of the BlackBoard"
                      font = titleFont
                    }

  val blackBoardContent = new TextArea(){
                            name = "blackboard"
                            editable = false
                            preferredSize = new Dimension(100,100)
                            maximumSize = new Dimension(800, 400)
                          }

  val clearButton = new Button(){
                      name = "clearButton"
                      text = "Clear"
                      tooltip = "Click to clear the content of the BlackBoard"
                    }
  val tokenLabel = new Label(){
                      text = "Token"
                      font = titleFont
                    }
  val tokenTextField = new TextField(){
                        columns = 1
                        preferredSize = new Dimension(30, 20)
                        maximumSize = new Dimension(100, 20)
                      }
  val densityLabel = new Label(){
                        text = "Density"
                        font = new Font("Verdana", java.awt.Font.BOLD, 15)
                        tooltip = "Number of token"
                      }
  val densityTextField = new TextField(){
                          columns = 1
                          preferredSize = new Dimension(30, 20)
                          maximumSize = new Dimension(100, 20)
                        }
  val tellButton = new Button(){
                    text = "Tell"
                    tooltip = "Write a token on the blackboard."
                  }
  val getButton = new Button(){
                    text = "Get"
                    tooltip = "Remove a token from the blackboard."
                  }
  val askButton = new Button(){
                    text = "Ask"
                    tooltip = "Ask if a token is on the blackboard."
                  }
  val naskButton = new Button(){
                    text = "Nask"
                    tooltip = "Ask if a token is not on the blackboard."
                  }
  val autonomousAgentButton = new Button{
                                text = "New Autonomous Agent"
                                tooltip = "This button lets you create an autonomous agent. You then can choose to let it run and see the result, or run it step by step."
                              }
  val interactiveAgentButton = new Button{
                                text = "New Interactive Agent"
                                tooltip = "This button lets you create an agent and choose which (possible) instruction to execute."
                              }

  def top = new MainFrame {
                title = "Bach Environment"
                centerOnScreen()
                //maximize()
                preferredSize = new Dimension(800, 600)
                resizable = false

                contents = new BoxPanel(Orientation.Vertical){
                    contents += new BoxPanel(Orientation.Horizontal){
                        contents += contentLabel
                        contents += Swing.HStrut(15)
                        contents += blackBoardContent
                        contents += Swing.HStrut(15)
                        contents += clearButton
                    }

                    // Vertical Space
                    contents += Swing.VStrut(15)

                    // Second line: Tokens
                    contents += new BoxPanel(Orientation.Horizontal){
                      contents += tokenLabel
                      contents += Swing.HStrut(10)
                      contents += tokenTextField
                      contents += Swing.HStrut(25)
                      contents += densityLabel
                      contents += Swing.HStrut(10)
                      contents += densityTextField
                    }

                    // Third Line: Buttons
                    contents += new BoxPanel(Orientation.Horizontal){
                      contents += tellButton
                      contents += Swing.HStrut(5)
                      contents += askButton
                      contents += Swing.HStrut(5)
                      contents += getButton
                      contents += Swing.HStrut(5)
                      contents += naskButton
                    }

                    contents += new BoxPanel(Orientation.Horizontal){
                      contents += autonomousAgentButton
                      contents += Swing.HStrut(5)
                      contents += interactiveAgentButton
                    }
                }

                // Listener on buttons
                listenTo(clearButton)
                listenTo(tellButton)
                listenTo(getButton)
                listenTo(askButton)
                listenTo(naskButton)
                listenTo(autonomousAgentButton)
                listenTo(interactiveAgentButton)

                // Defines the reactions of events (e.g buttonclicked)
                reactions += {
                    case ButtonClicked(component) if (component == clearButton)
                      =>  {
                            clearButton.text = "test"
                          }
                    case ButtonClicked(component) if (component == tellButton)
                      =>  {
                            // TODO Handle Tell Request
                          }
                    case ButtonClicked(component) if (component == getButton)
                      =>  {
                            // TODO Handle Get Request
                          }
                    case ButtonClicked(component) if (component == askButton)
                      =>  {

                          }
                    case ButtonClicked(component) if (component == naskButton)
                      =>  {

                          }
                    case ButtonClicked(component) if (component == autonomousAgentButton)
                      =>  {

                          }
                    case ButtonClicked(component) if (component == interactiveAgentButton)
                      =>  {

                          }
                    case _ => ???
                }
    }
}
