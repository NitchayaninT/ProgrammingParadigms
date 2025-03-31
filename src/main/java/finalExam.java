import java.util.*;
import java.util.concurrent.Exchanger;

public class finalExam {
    public static void main(String[] args) {
        Exchanger<Integer> ex = new Exchanger<>();
        myThread T1 = new myThread("Pakin",1,ex);
        myThread T2 = new myThread("Ninny",10,ex);
    }
}


class myThread extends Thread{
    int value;
    ArrayList<Integer> myList;
    Exchanger<Integer> myExchanger;

    public myThread(String na, int v, Exchanger<Integer> ex){
        super(na);
        this.value = v;
        myExchanger = ex;
        myList = new ArrayList<>();
        this.start();
    }

    @Override
    public void run(){
        for ( int i = 0; i<5;i++){
            try{
                myList.add(myExchanger.exchange(value));
                // I am teach you !! --> it is the job of the master to help his/her disciple
                value = myExchanger.exchange(value);
            } catch(Exception e){
                System.err.println("Exchange fail!");
            }
            this.value += 1;
        }
        synchronized(System.out){
            System.out.print(Thread.currentThread().getName()+" ");
            System.out.println(myList);
        }
    }
}