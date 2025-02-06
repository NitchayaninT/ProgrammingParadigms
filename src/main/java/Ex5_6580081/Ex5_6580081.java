//Pakin Panawattanakul 6580043
//Nitchayanin Thamkunanon 6580081
package Ex5_6580081;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Company implements Comparable<Company> {
    private String name;
    private int year, marketValue, profit, sales;
    //constructor
    public Company(String name, int year, int market, int profit, int sales)
    {
        this.name = name;
        this.year = year;
        this.marketValue = market;
        this.profit = profit;
        this.sales = sales;
    }
    public int compareTo(Company other) {
        // Sorting depends on value of x only
        int result = Integer.compare(other.marketValue, this.marketValue);
        if (result == 0)
        {
            result = Integer.compare(other.profit, this.profit);
            if(result == 0)
            {
                result = Integer.compare(other.sales, this.sales);
                if(result == 0)
                {
                    result = this.name.compareToIgnoreCase(other.name);
                }
            }
        }
        return result;
    }
    //setter
    public void setName(String name){this.name = name;};
    public void setYear(int year){this.year = year;};
    public void setMarket(int market){this.marketValue = market;};
    public void setProfit(int profit){this.profit = profit;};
    public void setSales(int sales){this.sales = sales;};
    //getter
    public String getName(){return this.name;}
    public int getYear(){return this.year;}
    public int getMarket(){return this.marketValue;}
    public int getProfit(){return this.profit;}
    public int getSales(){return this.sales;}
}
class InvalidYearException extends RuntimeException
{
    public InvalidYearException(String message) {
        super("InvalidYearException: For year : \""+ message+"\"");
    }
}
class InvalidNumberException extends RuntimeException
{
    public InvalidNumberException(String message) {
        super("InvalidNumberException: For input string: \""+ message+"\"");
    }
}
public class Ex5_6580081 {
    ArrayList<Company> Companies = new ArrayList<>();
    public static void main(String []args)
    {
        Ex5_6580081 main = new Ex5_6580081();
        main.readFile("companie.txt");

        Scanner sc = new Scanner(System.in);
        String choice;
        //printing output
        do {
            System.out.println("Enter year threshold = ");
            boolean threshold_correct = false;
            int threshold;
            while(!threshold_correct) {
                try {
                    threshold = sc.nextInt();
                    threshold_correct = true;
                    main.printList(threshold);
                } catch (InputMismatchException e) {
                    System.out.println(e.toString());
                    System.out.println("Enter year threshold = ");
                    sc.nextLine(); //without this it will be infinite loop
                }
            }
            System.out.println();
            System.out.println();
            System.out.println("Enter y or Y to continue, else to quit = ");
            choice = sc.next();
        } while (Objects.equals(choice, "Y") || Objects.equals(choice, "y"));
    }
    public void readFile(String fileName)
    {
        String path = "src/main/java/Ex5_6580081/";
        String inputPath = path + fileName;
        Scanner keyboard = new Scanner(System.in);//for user input new file
        boolean opensuccess = false;

        while(!opensuccess) {
            //reading file
            try (Scanner fileScan = new Scanner(new File(inputPath));//global. open success or not
            ){
                opensuccess = true;
                int count = 0;
                while (fileScan.hasNext()) {
                    String line = fileScan.nextLine(); //LINE str
                    if (count == 0) {
                        count++;
                        continue;
                    }
                    try {
                        String[] cols = line.split(",");
                        String name = cols[0].trim();

                        int year = Integer.parseInt(cols[1].trim());
                        if(year > 2025 || year < 0) throw new InvalidYearException(cols[1].trim());

                        int market = Integer.parseInt(cols[2].trim());
                        if(market < 0) throw new InvalidNumberException(cols[2].trim());
                        //from note 3, some conditions may or may not cause runtime exception
                        //i choose to convert string to int (11.4), so it causes runtime exception

                        int profits = Integer.parseInt(cols[3].trim());
                        if(profits < 0) throw new InvalidNumberException(cols[3].trim());

                        int sales = Integer.parseInt(cols[4].trim());
                        if(sales < 0) throw new InvalidNumberException(cols[4].trim());

                        Companies.add(new Company(name, year, market, profits, sales));
                        count++;

                    }
                    catch(RuntimeException e)//includes all the child classes of RuntimeException
                    {
                        System.out.println(e.toString());
                        System.out.println(line);
                        System.out.println();
                        count++;
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println(e.toString());
                System.out.println("New file name = ");
                fileName = keyboard.next();
                inputPath = path + fileName;
            }
        }
    }
    public void printList(int threshold)
    {
        System.out.printf("Company established since %4d%5sMarket Value($Bn.)%5sProfit($Bn.)%5sSales($Bn.)\n",threshold," "," "," ");
        String equal = "=";
        System.out.println(equal.repeat(90));
        Collections.sort(Companies);
        for(Company C : Companies)
        {
            if(C.getYear()>=threshold) System.out.printf("%-21s(%4d)%20d%17d%16d\n",C.getName(),C.getYear(),C.getMarket(),C.getProfit(),C.getSales());
        }

    }
}
