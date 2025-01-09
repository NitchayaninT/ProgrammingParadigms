//Nitchayanin Thamkunanon 6580081
package Ex1_6580081;
import java.util.*;

public class NewMain {
    public static int readHour()
    {
        Scanner sc = new Scanner(System.in);
        int hr;
        while(true)
        {
            System.out.println("Enter Hour Digit (0-23) = ");
            try{
                hr = sc.nextInt();
                if(hr>23 || hr<0) System.out.println("Invalid Hour");
                else break;
            }
            catch(Exception e)
            {
                System.out.println("Invalid Hour");
                sc.nextLine();
            }
        }
        return hr;
    }
    public static int readMin()
    {
        Scanner sc = new Scanner(System.in);
        int min;
        while(true)
        {
            System.out.println("Enter minute Digit (0-59) = ");
            try{
                min = sc.nextInt();
                if(min>59 || min<0) System.out.println("Invalid Minute");
                else break;
            }
            catch(Exception e)
            {
                System.out.println("Invalid Minute");
                sc.nextLine();
            }
        }
        return min;
    }
    public static void calculateDuration(int startHr,int startMin,int endHr,int endMin)
    {
        int diff = ((endHr*3600)+(endMin*60))-((startHr*3600)+(startMin*60));
        //if diff is negative, end time is less than start time (tomorrow)
        if(diff<0) {
            diff += 3600 * 24; //the next day
            System.out.println("(tomorrow)");
        }
        else System.out.println("(today)");

        System.out.println("Duration = "+diff/3600+" hours, "+(diff-((diff/3600)*3600))/60+" minutes");
    }
    public static void main(String[] args)
    {
        int startHr,startMin,endHr,endMin;
        String equal = "=";
        System.out.println("=== Start Time ===");
        startHr = readHour();
        startMin = readMin();

        System.out.println("\n=== End Time ===");
        endHr = readHour();
        endMin = readMin();

        //display time
        System.out.printf("Start Time = %02d:%02d, ",startHr,startMin);
        System.out.printf("End Time = %02d:%02d ",endHr,endMin);

        //calculate duration between them. start - end (in seconds)
        calculateDuration(startHr,startMin,endHr,endMin);
        System.out.println(equal.repeat(35));
    }
}
