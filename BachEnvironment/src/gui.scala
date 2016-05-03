import scala.swing._
import scala.swing.event.ButtonClicked
import swing.Swing

/**
  * Created by Axel on 21-04-16.
  */
object gui extends SimpleSwingApplication {

  var blackBoard = bb
  val simulateur = new BachTSimul(blackBoard)

  // Components definition
  val titleFont = new Font("Verdana", java.awt.Font.BOLD, 14)

  val contentLabel = new Label(){
                      text = "Content of the BlackBoard"
                      font = titleFont
                    }

  val blackBoardContent = new TextArea(){
                            text = "Content of the blackboard"
                            editable = false
                            preferredSize = new Dimension(100,100)
                            maximumSize = new Dimension(800, 400)
                          }

  val clearButton = new Button(){
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
  // End of components definition


  def top = new MainFrame {
                title = "Bach Environment"
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
                            // Clearing the board.
                            blackBoard.clear_store
                            blackBoardContent.text = ""
                          }
                    case ButtonClicked(component) if (component == tellButton)
                      =>  {
                            if (tokenTextField.text != "" && densityTextField.text != ""){
                              var i = 0
                              for (i <- 1 to densityTextField.text.toInt) blackBoard.tell(tokenTextField.text)
                            } else{
                              Dialog.showMessage(tellButton,"Please indicate a token and a density.","An error occured")
                            }

                            blackBoardContent.text = blackBoard.getContent
                          }
                    case ButtonClicked(component) if (component == getButton)
                      =>  {
                            if (tokenTextField.text != "" && densityTextField.text != ""){
                                if (densityTextField.text.toInt <= blackBoard.findDensity(tokenTextField.text)){
                                    var i = 0
                                    for (i <- 1 to densityTextField.text.toInt) {
                                        blackBoard.get(tokenTextField.text)
                                    }
                                } else{
                                  Dialog.showMessage(getButton, "There isn't enough of that token on the blackboard", "The primitive can't be treated")
                                }
                            } else{
                                Dialog.showMessage(getButton, "Please specify a token and a density.","An error occured")
                            }

                            blackBoardContent.text = blackBoard.getContent
                          }
                    case ButtonClicked(component) if (component == askButton)
                      =>  {
                            if(tokenTextField.text != "" && densityTextField.text != ""){
                              if (densityTextField.text.toInt <= blackBoard.findDensity(tokenTextField.text)){
                                  blackBoard.ask(tokenTextField.text)
                                  Dialog.showMessage(askButton,"There is enough tokens on the blackboard !", "Success")
                              } else{
                                  blackBoard.ask(tokenTextField.text)
                                  Dialog.showMessage(askButton,"Unfortunately, there isn't enough tokens on the blackboard...","Failure")
                              }
                            } else{
                              Dialog.showMessage(askButton, "Please specify a token and a density.","An error occured")
                            }

                            blackBoardContent.text = blackBoard.getContent
                          }
                    case ButtonClicked(component) if (component == naskButton)
                      =>  {
                            if(tokenTextField.text != "" && densityTextField.text != ""){
                              if (densityTextField.text.toInt <= blackBoard.findDensity(tokenTextField.text)){
                                blackBoard.ask(tokenTextField.text)
                                Dialog.showMessage(askButton,"Unfortunately, there is enough tokens on the blackboard...","Failure")
                              } else{
                                blackBoard.ask(tokenTextField.text)
                                Dialog.showMessage(askButton,"There isn't enough tokens on the blackboard !", "Success")
                              }
                            } else{
                              Dialog.showMessage(askButton, "Please specify a token and a density.","An error occured")
                            }

                            blackBoardContent.text = blackBoard.getContent
                          }
                    case ButtonClicked(component) if (component == autonomousAgentButton)
                      =>  {
                            new AutonomousAgentGUI().open()
                          }
                    case ButtonClicked(component) if (component == interactiveAgentButton)
                      =>  {
                            new InteractiveAgentGUI().open()
                          }
                }
    }
}
