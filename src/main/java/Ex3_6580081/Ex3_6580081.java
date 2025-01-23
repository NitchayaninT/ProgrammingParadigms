//Nitchayanin Thamkunanon 6580081
package Ex3_6580081;
import java.util.*;
import java.io.*;

class Player {
    //Constructor
    public Player(){super();};
    public Player(String nm, int by) { name = nm; birthyear = by; }
    //Vars
    public static final int CURRENT_YEAR = 2025;
    private String name;
    protected int birthyear, age;

    //Methods
    public String getName() { return name; }
    public void printPersonalData() { /* override this in child class */ }
    public void printStat() { /* override this in child class */ }
}

class FootballPlayer extends Player {
    //Constructor
    public FootballPlayer(String name,int by){super(name,by);};
    public FootballPlayer(String name, int by, int[] games, int[] goals){
        super(name,by);
        this.games = games;
        this.goals = goals;
        this.GameCount = games.length;
        this.GoalCount = goals.length;
    };
    //Vars
    public static int PlayersCount = 0;
    private int GameCount = 0;
    private int GoalCount = 0;
    private int []games;
    private int []goals;
    private float avgGoals;
    //Methods
    private int calculateAge(){this.age=CURRENT_YEAR-birthyear; return this.age;}
    public int[] getGames(){return this.games;};
    public int[] getGoals(){return this.games;};
    private int getTotalGame(){
        int totalGame = 0;
        for(int i=0;i<this.GameCount;i++)
        {
            totalGame+=this.games[i];
        }
        return totalGame;
    }
    private int getTotalGoals(){
        int totalGoal = 0;
        for(int i=0;i<this.GoalCount;i++)
        {
            totalGoal+=this.goals[i];
        }
        return totalGoal;
    }
    private float calculateAvgGoals(){
        float avg = 0;
        int totalGame = getTotalGame();
        int totalGoals = getTotalGoals();
        avg = (float)totalGoals/totalGame;
        return avg;
    }
    @Override
    public void printPersonalData(){
        String ageStr = "age = "+calculateAge();
        System.out.printf("%-22s %7s %4d %13s",this.getName(),"born",this.birthyear,ageStr);
    };
    public void printStat(){
        int totalGame = getTotalGame();
        int totalGoals = getTotalGoals();
        float avg = calculateAvgGoals();
        int lastSeasonGoal = this.goals[this.GoalCount-1];
        System.out.printf("%-22s %13s = %3d %15s = %3d (%.2f per game) %20s = %3d",this.getName(),"total games",totalGame,"total goals",totalGoals,avg,"last season goals",lastSeasonGoal);
    };
}

class BasketballPlayer extends Player {
    //Constructor
    public BasketballPlayer(String name, int by){super(name,by);};
    public BasketballPlayer(String name, int by, int games, int mins, int pts){
        super(name,by);
        this.totalGames = games;
        this.totalMins = mins;
        this.totalPts = pts;
    }
    //Vars
    public static int PlayersCount = 0;
    private int totalGames, totalMins, totalPts;
    //Methods
    private int calculateAge(){this.age=CURRENT_YEAR-birthyear; return this.age;}
    public int getTotalGames(){return this.totalGames;};
    public int getTotalMins(){return this.totalMins;};
    public int getTotalPts(){return this.totalPts;};
    @Override
    public void printPersonalData(){
        String ageStr = "age = "+calculateAge();
        System.out.printf("%-25s %4s %4d %4s %8s",this.getName(),"born",this.birthyear," ",ageStr);
    };
    public void printStat(){
        System.out.printf("%-22s %13s = %3d %14s = %3d (%.2f per game) %17s = %5d (%.2f per game)",this.getName(),"total games",this.totalGames,"total mins",this.totalMins,(float)this.totalMins/this.totalGames,"total points = ",this.totalPts,(float)this.totalPts/this.totalGames);
    };
}

public class Ex3_6580081 {
    public static int PlayersCount=0;
    /// MAIN METHOD ///
    public static void main(String []args)
    {
        Ex3_6580081 main = new Ex3_6580081();
        Player[] allPlayers;
        allPlayers = new Player[12];  //array of 12 player objects
        //read from file "players.txt"
        main.readFile(allPlayers,"players.txt");
        main.printAllPlayerData(allPlayers);
        System.out.println();
        main.printFootballPlayerStats(allPlayers);
        System.out.println();
        main.printBasketballPlayerStats(allPlayers);
        String line = "-";
        System.out.println(line.repeat(73));
    }
    public void readFile(Player[] allPlayers,String fileName)
    {
        String path = "src/main/java/Ex3_6580081/";
        String inputPath = path + fileName;

        //reading file
        try{
            File inputFile = new File(inputPath);
            Scanner fileScan = new Scanner(inputFile); //for reading input file
            while(fileScan.hasNext())
            {
                String line = fileScan.nextLine();
                String [] cols = line.split(","); //split the cols

                String type = cols[0].trim();

                String name = cols[1].trim();
                int year = Integer.parseInt( cols[2].trim());

                if(type.equals("F"))
                {
                    int []games = new int[3];
                    int []goals = new int[3];
                    for(int i=0;i<3;i++)
                    {
                        String [] s = cols[i+3].split(":");
                        games[i]=Integer.parseInt(s[0].trim());
                        goals[i]=Integer.parseInt(s[1].trim());
                    }

                    allPlayers[PlayersCount] = new FootballPlayer(name,year,games,goals);
                    FootballPlayer.PlayersCount++;
                }
                else if(type.equals("B"))
                {
                    int games = Integer.parseInt(cols[3].trim());
                    int mins = Integer.parseInt(cols[4].trim());
                    int pts = Integer.parseInt(cols[5].trim());
                    allPlayers[PlayersCount] = new BasketballPlayer(name,year,games,mins,pts);
                    BasketballPlayer.PlayersCount++;
                }
                PlayersCount++;
            }
        }
        catch(Exception e)
        {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
    public void printAllPlayerData(Player[]p)
    {
        //Test reading from file
        System.out.println("=== All Player data (by reverse order) ===");
        for(int i=PlayersCount-1;i>=0;i--)
        {
            p[i].printPersonalData();
            System.out.println();
        }
    }
    public void printFootballPlayerStats(Player[]p)
    {
        System.out.println("=== Football player statistics (by input order) ===");
        for(int i=0;i<PlayersCount;i++)
        {
            if(p[i] instanceof FootballPlayer)
            {
                p[i].printStat();
                System.out.println();
            }
        }
    }
    public void printBasketballPlayerStats(Player[]p)
    {
        System.out.println("=== Basketball player statistics (by input order) ===");
        for(int i=0;i<PlayersCount;i++)
        {
            if(p[i] instanceof BasketballPlayer)
            {
                p[i].printStat();
                System.out.println();
            }
        }
    }
}
