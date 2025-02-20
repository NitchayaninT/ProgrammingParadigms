//Nitchayanin Thamkunanon 6580081
package Ex6_6580081;
import javax.smartcardio.Card;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

//represents specific card
class OneCard {
    private int score; // 0-51
    private int suit; // clubs, diamonds, hearts, spades
    private int rank; // ace, 2-10, jack, queen, king

    public OneCard(int sc) { score = sc; }
    //setter getter
    public void setSuit(int suit) {this.suit = suit;}
    public void setRank(int rank) {this.rank = rank;}
    public int getSuit(){return this.suit;}
    public int getScore(){return this.score;}
    public int getRank(){return this.rank;}
    public String getSuitStr()
    {
        if(suit == 0){return "Club";}
        else if(suit == 1){return "Diamonds";}
        else if(suit == 2){return "Hearts";}
        else if(suit == 3){return "Spades";}
        else{return "error";}
    }
    public String getRankStr()
    {
        if(rank%13==0){return "A";}

        else{return String.valueOf(rank%13);}
    }
    public String toString()
    {
        return getRankStr()+" of "+getSuitStr();
    }


}
class CardThread extends Thread {
    private PrintWriter out;
    private ArrayList<OneCard> randomCards;
    private ArrayList<OneCard> originalDeck;
    private int rounds;
    //constructor
    CardThread(int threadNo){
        super("T"+String.valueOf(threadNo));
        randomCards = new ArrayList<>();
        originalDeck = new ArrayList<>();
    }
    public void run() {
        String path = "src/main/java/Ex6_6580081/";
        String threadFile = Thread.currentThread().getName()+".txt";
        String outputPath = path + threadFile;
        File outputFile = new File(outputPath);
        rounds = 1; //start at round 1
        try {
            // Create PrintWriter object to write result to a separate file
            //1 deck = 52 cards
            out = new PrintWriter(new FileWriter(outputFile, false));//output file
            addCards(); //original Deck is here
            Random random = new Random();
            while(true) //thread ends = all cards in the same suit or same rank
            {
                for(int i=0;i<4;i++)
                {
                    //get random cards
                    OneCard card = originalDeck.get(random.nextInt(51));
                    if(randomCards.contains(card)){
                        i--;
                        continue;
                    }
                    randomCards.add(card);
                }
                // Execute steps 1-3 in loop:
                // 1. Draw 4 cards from the same deck. The cards must not duplicate.
                // 2. Print round number and these 4 cards to output file.
                out.printf("Round %3d "+randomCards,rounds);
                //System.out.println("Round "+rounds+" "+randomCards);
                // 3. If all cards are from the same suit or have equal rank, stop the loop.
                    int equalRankCount = 0;
                    int equalSuitCount = 0;
                    for(int j=0;j<randomCards.size();j++)
                    {
                        OneCard card = randomCards.get(j);
                        for(int i=j+1;i<randomCards.size();i++)
                        {
                            if(card == randomCards.get(i)) continue;
                            if(card.getSuit()==randomCards.get(i).getSuit())
                            {equalSuitCount++;break;}
                            else if(card.getRank()==randomCards.get(i).getRank())
                            {equalRankCount++;break;}
                        }
                    }
                    randomCards.clear();
                    if(equalRankCount == 3 || equalSuitCount==3)break;
                // Otherwise, clear randomCards & continue to the next round.
                // After the loop, print #rounds to the screen
                rounds++;
            }
            out.close();
            System.out.println("Thread "+Thread.currentThread().getName() + " finishes in "+rounds+" rounds");
        }
        catch (Exception e) {
            System.out.println("Cannot write to output file");
            System.err.println(e);
        }

    }
    private void addCards()
    {
        //Ace,2-10,jack,queen,king
        int round = -1;
        for(int i=0;i<52;i++)
        {
            originalDeck.add(new OneCard(i));
            if(i%13 == 0)
            {
                round++;
                originalDeck.get(i).setRank(0);
                originalDeck.get(i).setSuit(round);
            }
            else if(i%13==10)
            {
                originalDeck.get(i).setRank(10);
                originalDeck.get(i).setSuit(round);
            }
            else if(i%13==11)
            {
                originalDeck.get(i).setRank(11);
                originalDeck.get(i).setSuit(round);
            }
            else if(i%13==12)
            {
                originalDeck.get(i).setRank(12);
                originalDeck.get(i).setSuit(round);
            }
            else{
                originalDeck.get(i).setRank(i%13);
                originalDeck.get(i).setSuit(round);
            }

        }
    }
    public int getRounds(){return rounds;}
}
public class Ex6_6580081 {
    static ArrayList<CardThread> threads = new ArrayList<>();
    public static void main(String []args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Number of threads =");
        int threadNo = sc.nextInt();

        for(int i=0;i<threadNo;i++)
        {
            CardThread T = new CardThread(i);
            threads.add(T);
            T.start();
        }



    }
}
