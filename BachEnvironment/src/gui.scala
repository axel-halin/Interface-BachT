import java.awt.Color

import scala.swing._
import scala.swing.event.ButtonClicked
import swing.Swing

/**
  * Created by Axel on 21-04-16.
  */
object gui extends SimpleSwingApplication {
    def top = new MainFrame {
                title = "Bach Environment"
                centerOnScreen()
                //maximize()

                preferredSize = new Dimension(800, 600)
                maximumSize = new Dimension(1200, 600)
                minimumSize = new Dimension(800, 400)
                resizable = false

                contents = new BoxPanel(Orientation.Vertical){
                    contents += new BoxPanel(Orientation.Horizontal){

                      contents += new Label(){
                                    text = "Content of the BlackBoard"
                                    font = new Font("Verdana", java.awt.Font.BOLD, 15)
                                  }
                      contents += Swing.HStrut(15)
                      contents += new TextArea(){
                                    columns = 20
                                    rows = 10
                                    editable = false
                                    preferredSize = new Dimension(100,100)
                                    maximumSize = new Dimension(800, 400)
                                  }

                      contents += Swing.HStrut(15)
                      contents += new Button(){
                                    text = "Clear"
                                    tooltip = "Click to clear the content of the BlackBoard"
                                  }
                    }

                    // Vertical Space
                    contents += Swing.VStrut(15)

                    // Second line: Tokens
                    contents += new BoxPanel(Orientation.Horizontal){
                      contents += new Label(){
                                    text = "Token"
                                    font = new Font("Verdana", java.awt.Font.BOLD, 15)
                                  }
                      contents += Swing.HStrut(10)
                      contents += new TextField(){
                                    columns = 1
                                    preferredSize = new Dimension(30, 20)
                                    maximumSize = new Dimension(100, 20)
                                  }
                      contents += Swing.HStrut(25)
                      contents += new Label(){
                                    text = "Density"
                                    font = new Font("Verdana", java.awt.Font.BOLD, 15)
                                    tooltip = "Number of token"
                                  }
                      contents += Swing.HStrut(10)
                      contents += new TextField(){
                                    columns = 1
                                    preferredSize = new Dimension(30, 20)
                                    maximumSize = new Dimension(100, 20)
                                  }
                    }

                    // Vertical Space
                    //contents += Swing.VStrut(15)

                    // Third Line: Buttons
                    contents += new BoxPanel(Orientation.Horizontal){
                      contents += new Button(){
                        text = "Tell"
                        tooltip = "Write a token on the blackboard."
                      }
                      contents += Swing.HStrut(5)
                      contents += new Button(){
                        text = "Ask"
                        tooltip = "Ask if a token is on the blackboard."
                      }
                      contents += Swing.HStrut(5)
                      contents += new Button(){
                        text = "Get"
                        tooltip = "Remove a token from the blackboard."
                      }
                      contents += Swing.HStrut(5)
                      contents += new Button(){
                        text = "Nask"
                        tooltip = "Ask if a token is not on the blackboard."
                      }
                    }


                    contents += new BoxPanel(Orientation.Horizontal){
                      contents += new Button{
                        text = "New Autonomous Agent"
                        tooltip = "This button lets you create an autonomous agent. You then can choose to let it run and see the result, or run it step by step."
                      }
                      contents += Swing.HStrut(5)
                      contents += new Button{
                        text = "New Interactive Agent"
                        tooltip = "This button lets you create an agent and choose which (possible) instruction to execute."
                      }
                    }
                }

                reactions += {
                  case ButtonClicked(component) => 


                }
    }
}
