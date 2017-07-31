import javax.swing.*;
import java.awt.*;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
        //I've been using this as a quick way to run short snippets of arbitrary code

        //print description of all parsed abilities
        try{
            FileReader fileReader = new FileReader("abilities.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;
            int i = 1;
            while ((line = bufferedReader.readLine()) != null){
                Ability a = Ability.parseAbilitiesLine(line);
                System.out.println(i++ + ": " + a.getDescription());
            }

            bufferedReader.close();
        } catch (FileNotFoundException e){
           e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
