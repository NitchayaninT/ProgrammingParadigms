package Lab_Ch5;

import java.util.*;
import java.io.*;

/////////////////////////////////////////////////////////////////////////////////////////////
class MyInputReader
{
    private String   path, fileName;
    private Scanner  keyboardScan;
    private int      sum;
    
    public MyInputReader(String p, String fn)                
    {
        path      = p;
        fileName  = fn; 
        keyboardScan = new Scanner(System.in);
    }
    
    public void processLine(String line)
    {
        int numerator, divisor, result = 0;
        try
        {
            String []buf = line.split("\\s+"); // split the line by one or more spaces
            numerator = Integer.parseInt(buf[0]);//get each item without any space
            divisor   = Integer.parseInt(buf[1]);
            result    = numerator / divisor;
            System.out.printf("%2d / %2d = %d \n", numerator, divisor, result);
        }
        catch(RuntimeException e) 
        //catch(ArithmeticException | ArrayIndexOutOfBoundsException e)
        //catch(ArithmeticException | RuntimeException e) //cannot write like this because Artithmetic is child of Runtime exception
        {   
            System.out.println(e + " --> skip");
            result = 0; 
        }
        finally //this block is always excuted even if file ot exist, but if file not exist, we declared result = 0
        {
            sum = sum + result;
        }
    }
    
    public void oldTry_openFileOnce() 
    {
        // ----- (1) declare fileScan outside try-block, so it can be used in finally-block
        Scanner fileScan = null;
        try 
        {
            fileScan = new Scanner(new File(path + fileName));
            while(fileScan.hasNextLine())  
            { 
                processLine(fileScan.nextLine());
            }
        }
        catch (FileNotFoundException e) //if the file missing, just print the msg, not close scanner yet
        {
            System.out.println(e);
        }
        finally //use finally to close the scanner object in old style
        {
            // ----- (1) close the file, either with or without an exception
            if (fileScan != null) fileScan.close();
            System.out.printf("Finally >> Sum = %d \n", sum);
        }
    }
    
    public void newTry_openFileOnce() 
    {
        try (
            // ----- (2) declare fileScan in resource declaration of try-block
            //           it can be used only in try-block & close automatically
            //THIS is global result, no need to close it later, it will be closed automatically
            Scanner fileScan = new Scanner(new File(path + fileName));
        ){
            while(fileScan.hasNextLine())  
            { 
                processLine(fileScan.nextLine());
            }
        }
        catch (FileNotFoundException e) 
        {
            System.out.println(e);
        }
        finally //the program will always excute the code in finaly block, even if the file is not found
        {
            System.out.printf("Finally >> Sum = %d \n", sum);
        }
    }
    
    public void newTry_openFileLoop() 
    {
        boolean opensuccess = false;
        while (!opensuccess)//ask to insert new file name indefinitely until opensuccess = true
        {
            try (
                Scanner fileScan = new Scanner(new File(path + fileName));//global. open success or not
            ){
                opensuccess = true;                
                while(fileScan.hasNextLine())  
                { 
                    processLine(fileScan.nextLine());
                }
            }
            catch (FileNotFoundException e) 
            {
                System.out.print(e + " --> ");
                System.out.println("New file name = ");
                fileName = keyboardScan.next();
            }
            finally 
            {
                System.out.printf("Finally >> Sum = %d \n", sum);
            }
        }
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////
public class w5_6_TryResource 
{
    public static void main(String[] args) 
    {
        String path     = "src/main/java/Lab_Ch5/";
        String [] files = {"correctone.txt", "correctzero.txt", "wrong.txt"};
        
        MyInputReader calc = new MyInputReader( path, files[2] );
        //calc.oldTry_openFileOnce(); //old style
        //calc.newTry_openFileOnce();
        calc.newTry_openFileLoop();
    }    
}
