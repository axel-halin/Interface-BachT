import java.awt.image.BufferedImage
import java.awt.{Graphics, Component}
import javax.swing.{ImageIcon, Icon}

import scala.swing._
import scala.swing.event.ValueChanged

/**
  * MainFrame to display the visual blackboard.
  * This version of the blackboard displays images depending on the content of the blackboard.
  *
  * Created by Axel on 07-05-16.
  */
class VisualBlackboard(blackboard:BachTStore, blackboardDisplay:TextArea) extends MainFrame{

  val flowPanel = new FlowPanel()

  contents = new ScrollPane(){
    contents = flowPanel
    
  }

    //flowPanel
  title = "Visual Blackboard"
  preferredSize = new Dimension(800, 600)
  minimumSize = new Dimension(800,600)
  maximumSize = new Dimension(800,600)

  listenTo(blackboardDisplay)

  reactions +={
      case ValueChanged(component) if component == blackboardDisplay
      =>  {
              flowPanel.contents.clear()
              val exprList = blackboard.theStore
              if (exprList.isEmpty){}
              else {
                for (i <- exprList) {
                  i._2 match {
                      case 0 => flowPanel.contents += new Label("Token " + i._1, new ImageIcon("./resources/jar.jpg"), Alignment.Center) {
                        verticalTextPosition = Alignment.Bottom
                        horizontalTextPosition = Alignment.Center
                      }
                      case 1 => flowPanel.contents += new Label("Token " + i._1, new ImageIcon("./resources/jar1.png"), Alignment.Center){
                          verticalTextPosition = Alignment.Bottom
                          horizontalTextPosition = Alignment.Center
                      }
                      case 2 => flowPanel.contents += new Label("Token " + i._1, new ImageIcon("./resources/jar2.png"), Alignment.Center){
                          verticalTextPosition = Alignment.Bottom
                          horizontalTextPosition = Alignment.Center
                      }
                      case 3 => flowPanel.contents += new Label("Token " + i._1, new ImageIcon("./resources/jar3.png"), Alignment.Center){
                          verticalTextPosition = Alignment.Bottom
                          horizontalTextPosition = Alignment.Center
                      }
                      case 4 => flowPanel.contents += new Label("Token " + i._1, new ImageIcon("./resources/jar4.png"), Alignment.Center){
                          verticalTextPosition = Alignment.Bottom
                          horizontalTextPosition = Alignment.Center
                      }
                      case _ => flowPanel.contents += new Label("Token "+i._1, new ImageIcon("./resources/jar5.png"), Alignment.Center){
                          verticalTextPosition = Alignment.Bottom
                          horizontalTextPosition = Alignment.Center
                      }
                  }
                }
                blackboardDisplay.revalidate()
              }
          }
  }

  override def closeOperation : scala.Unit = this.close()
}
