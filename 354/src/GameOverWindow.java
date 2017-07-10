import javax.swing.*;
import java.awt.*;

public class GameOverWindow {
    private JFrame gameOverFrame;

    GameOverWindow(Ability.Player player){
        gameOverFrame = new JFrame();
        //Set window properties
        gameOverFrame = new JFrame("354 Pokemon Game");
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameOverFrame.setSize(new Dimension(200, 100));
        gameOverFrame.setResizable(false);
        gameOverFrame.setLayout(new BorderLayout());

        String winLose;
        if(player == Ability.Player.PLAYER){
            winLose = "won!";
        }
        else{
            winLose = "lost.";
        }

        JLabel message = new JLabel("<html><center>The game is over.<br/>You have "+ winLose + " </center></html>");
        message.setHorizontalAlignment(SwingConstants.CENTER);

        gameOverFrame.add(message);
    }

    public void display(){
        this.gameOverFrame.setVisible(true);
    }

    public static void main(String [] args){
       GameOverWindow g = new GameOverWindow(Ability.Player.AI);
       g.gameOverFrame.setVisible(true);
    }
}
