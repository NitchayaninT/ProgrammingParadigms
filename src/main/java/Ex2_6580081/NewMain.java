//Nitchayanin Thamkunanon 6580081
package Ex2_6580081;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

public class NewMain {
    public static void main(String []args)
    {
        //relative path
        String path = "src/main/java/Ex2_6580081/";
        String inputPath = path + "platforms.txt";
        String outputPath = path + "output.txt";

        try{
            File input_file = new File(inputPath); //created file object to work with a file
            Scanner fileScan = new Scanner(input_file); //to scan file object
            File output_file = new File(outputPath);
            PrintWriter writeFile = new PrintWriter(new FileWriter(output_file, false) );//overwrite
            Scanner keyboardScan = new Scanner(System.in);

            System.out.println("Read platform data from "+inputPath);
            System.out.println("Enter MAU threshold in millions =");
            int threshold = keyboardScan.nextInt();
            String LessthanThreshold = ">"+threshold+" millions";
            System.out.println("Write output "+outputPath);

            //read from platform.txt & writing to output.txt
            int linesCount = 0;
            while(fileScan.hasNext())
            {
                if(linesCount == 0)
                {
                    writeFile.printf("%s%22s%20s%20s\r\n","Platform","MAU(thousands)","MAU(billions)",LessthanThreshold);
                    linesCount++;
                    continue;
                }
                else if(linesCount == 1)
                {
                    String equal = "=";
                    writeFile.println(equal.repeat(70));
                    linesCount++;
                    continue;
                }
                //scan the contents from platform.txt
                String platform = fileScan.next();
                int MAU = fileScan.nextInt();

                //calculate MAU (millions) to thousands and mils
                int thousands = MAU * 1000;
                float billions = (float)MAU / 1000;

                //is the platform more than threshold mil?
                String moreThanThreshold = "no";
                if(billions > (float) threshold /1000) moreThanThreshold = "yes";

                //write to file in : platform, MAU(thousands), MAU(billions), >500 millions (yes/no) format
                writeFile.printf("%-15s%,12d%19.3f%16s%-3s\r\n",platform,thousands,billions," ",moreThanThreshold);
                linesCount++;
            }
            writeFile.close(); // This ensures data is written to the file
            fileScan.close();
        }
        catch(Exception e)
        {
            System.out.println("file doesn't exist");
        }
    }
}
