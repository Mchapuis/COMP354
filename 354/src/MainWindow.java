
import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class MainWindow {
    public static final int BENCH_SIZE = 5;
    public enum Side{
            player,
            ai
    }
    public enum Location{
        hand,
        bench,
        active
    }

    JFrame mainFrame = null;

    LinkedList<GUICard> playerBench = null;
    LinkedList<GUICard> AIBench = null;

    GUICard playerActivePokemonButton = null;
    GUICard AIActivePokemonButton = null;

    LinkedList<GUICard> playerHand = null;
    LinkedList<GUICard> AIHand = null;

    private JPanel pHandContainer = null;
    private JPanel pBenchContainer = null;
    private JPanel AIHandContainer = null;
    private JPanel AIBenchContainer = null;

    MainWindow(){
        //Set window properties
        mainFrame = new JFrame("354 Pokémon Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(1500,800));
        mainFrame.setLayout(new GridLayout(0,1));

        //bench buttons
        playerBench = new LinkedList<>();
        AIBench = new LinkedList<>();
        for(int i = 0; i < BENCH_SIZE; i++){
            playerBench.add(new GUICard(new JButton(String.valueOf(i) + " bench pokémon"), "card description<br>more description<br>even more description<br>moooooore"));
            AIBench.add(new GUICard(new JButton(String.valueOf(i) + " bench pokémon"), "card description<br>more description<br>even more description<br>moooooore"));
        }

        //active pokemon buttons
        playerActivePokemonButton = new GUICard(new JButton("Player Active Pokémon"), "description");
        AIActivePokemonButton = new GUICard(new JButton("AI Active Pokémon"), "description");

        //hand buttons
        playerHand = new LinkedList<>();
        AIHand = new LinkedList<>();
        for(int i = 0; i < 7; i++){
            playerHand.add(new GUICard(new JButton(String.valueOf(i) + " hand card"), "card description<br>more description<br>even more description<br>moooooore"));
            AIHand.add(new GUICard(new JButton(String.valueOf(i) + " hand card"), "card description<br>more description<br>even more description<br>moooooore"));
        }

        //divide window in 2 halves
        JPanel playerSide = new JPanel();
        JPanel AISide = new JPanel();
        mainFrame.add(AISide);
        mainFrame.add(playerSide);


        //add buttons to player side
        {
            playerSide.setLayout(new GridLayout(0,1));

            //active
            playerSide.add(playerActivePokemonButton.card);

            //bench
            pBenchContainer = new JPanel();
            playerSide.add(pBenchContainer);
            for(GUICard c : playerBench){
                pBenchContainer.add(c.card);
            }

            //hand
            pHandContainer = new JPanel();
            playerSide.add(pHandContainer);
            for(GUICard c: playerHand){
                pHandContainer.add(c.card);
            }
        }

        //add buttons to AI side
        {
            AISide.setLayout(new GridLayout(0,1));

            //hand
            AIHandContainer = new JPanel();
            AISide.add(AIHandContainer);
            for(GUICard c: AIHand){
                AIHandContainer.add(c.card);
            }

            //bench
            AIBenchContainer = new JPanel();
            AISide.add(AIBenchContainer);
            for(GUICard c : AIBench){
                AIBenchContainer.add(c.card);
            }

            //active
            AISide.add(AIActivePokemonButton.card);
        }
    }

    public void display(){
        mainFrame.setVisible(true);
    }

    public void addCard(){

    }

    public void removeCard(){

    }


    public static void main(String[] args){

        MainWindow w = new MainWindow();
        w.display();
    }

}
