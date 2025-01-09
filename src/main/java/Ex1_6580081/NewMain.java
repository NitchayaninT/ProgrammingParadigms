package Ex1_6580081;
import java.util.*;

public class NewMain {
    public static int readHour()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Hour Digit (0-23) ");
        int hr = sc.nextInt();
        return hr;
    }
    public static int readMin()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter minute Digit (0-59) ");
        int min = sc.nextInt();
        return min;
    }
    public static void main(String[] args)
    {
        System.out.println("=== Start Time ===");
        int hr,min;
        while(true)
        {
            hr = readHour();
            if(hr>23 || hr<0)
            {
                System.out.println("Invalid Hour");
                continue;
            }
            else break;
        }
        while(true)
        {
            min = readMin();
            if(min>59 || min<0)
            {
                System.out.println("Invalid Min");
                continue;
            }
            else break;
        }
        //int min = readMin();

    }
}
