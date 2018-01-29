/*
Muhammad Zain UL Islam / Zain-Sra
Advance Programming (Section A)
Assingment

Using Multithreading finding Paladromic Words 

This is PalindromicWriter Thread which writes the result array containing all the palindromic words in dictionary to the File 
It waits till the result array is free from use by Worker Thread , then if it gets notified that result array is free to use , it writes the words from array to File
 */
package palindromefinder;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class PalindromeWriter extends Thread{
    
    private  ArrayList<String> result;
    int totalWorkers; //To Store Total # of Worker Threads
    int jobsDone; //TO keep track of # of Jobs completed by Worker Thread 
    String file; //To store file name
    int indexResult; //Store the current index of result Array, from where it starts reading till the end of the array
    PrintWriter out;
    int totalCountPalindrome=0; //Store Total # of Palindromic Words found 
    ArrayList<String> workers=new ArrayList<String>(); //To store Thread 2 Count_23 , i.e Thread Name alongwith the # of Palindromic Words it found in its job
    
    
    PalindromeWriter(ArrayList<String> r,int t,String f)
    {
        this.result=r;
        this.totalWorkers=t;
        this.jobsDone=0;
        this.file=f;
        this.indexResult=0;
        
    }
    
    public void run()
    {
        
        try {
            
            //Opening File for Writing
     out = new PrintWriter(file);
    
        }
    catch (IOException e)
    {
    System.out.println("Exception ");

        }
        
        //Palindromic Writer keeps on waiting untill there are no more WOrker THread working which means that all of the Palindromic Words have been found
        //During Waiting , if it gets notified that result array is free. THen it writes the array content to File
        while(jobsDone!=totalWorkers)
        {
            
            synchronized(result)
            {
                try{
                System.out.println("Writer is waiting");
                
                result.wait(); //Waiting for result Array to get free of use from other Worker Threads which are filing the array
                
            }catch(InterruptedException e){
                e.printStackTrace();
            }
                
                //Here the Array is free to use , and now This THread is free to use the result array and copy it's content to the File 
                //Keeping the track of index of the array with which it stops last time
                
                jobsDone++; //One of the Worker Thread completed it's job and notify() this thread that the result array is free to use now
                
                for(;indexResult<result.size();indexResult++){
                    
                    if(!result.get(indexResult).startsWith("Worker "))
                        out.println(result.get(indexResult));
                    else {
                    workers.add(result.get(indexResult));
                    String temp;
                    
                    if(result.get(indexResult).charAt(13)=='_')
                    
                    temp=result.get(indexResult).substring(14);
                    else {
                        temp=result.get(indexResult).substring(15);
                    }
                    int count=Integer.parseInt(temp.trim());
                    totalCountPalindrome+=count;

                    }
                }                             

            }
            
        }
        
        System.out.println("All workers have completed their jobs ! ");
        
        out.println("-----End | Zain-Sra's Writer------");
        for(int i=0;i<workers.size();i++)
        {
            out.println(workers.get(i));
        }
        
        out.print("Total Palindromic Words : "+totalCountPalindrome);
        
        //Closing FIle when done with task
        
        out.close();
    }
    
    
    
    
}
