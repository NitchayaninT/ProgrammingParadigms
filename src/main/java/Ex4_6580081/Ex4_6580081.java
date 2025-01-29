package Ex4_6580081;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

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
public class Ex4_6580081 {
    static ArrayList<Company> Companies = new ArrayList<>();
    public static void main(String []args)
    {
        Ex4_6580081 main = new Ex4_6580081();
        main.readFile("companies.txt");

        Scanner sc = new Scanner(System.in);
        String choice;
        //printing output
        do {
            System.out.println("Enter year threshold = ");
            int threshold = sc.nextInt();
            main.printList(threshold);
            System.out.println();
            System.out.println();
            System.out.println("Enter y or Y to continue, else to quit = ");
            choice = sc.next();
        } while (Objects.equals(choice, "Y") || Objects.equals(choice, "y"));
    }
    public void readFile(String fileName)
    {
        String path = "src/main/java/Ex4_6580081/";
        String inputPath = path + fileName;

        //reading file
        try{
            File inputFile = new File(inputPath);
            Scanner fileScan = new Scanner(inputFile); //for reading input file
            int count = 0;
            while(fileScan.hasNext())
            {
                String line = fileScan.nextLine();
                if(count == 0)
                {
                    count++;
                    continue;
                }
                String [] cols = line.split(",");
                String name= cols[0].trim();
                int year = Integer.parseInt(cols[1].trim());
                int market = Integer.parseInt(cols[2].trim());
                int profits = Integer.parseInt(cols[3].trim());
                int sales = Integer.parseInt(cols[4].trim());
                Companies.add(new Company(name,year,market,profits,sales));
                count++;
            }
        }
        catch(Exception e)
        {
            System.out.println("An error occurred: " + e.getMessage());
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
