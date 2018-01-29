/*
Muhammad Zain UL Islam / Zain-Sra
Advance Programming (Section A) 
Assingment

Using Multithreading finding Paladromic Words from Dictionary

My Algorithm: 

1.Reads Words from Dictionary File and Create Bags where each bag has a specific length and contains a list of words of that length.

File => Bags (Map <length,ArrayList<String>>) 

2. Get # of Worker Thread from user 

3. Assing Bags to Worker Thread and start the threads

4. Palindrome Writer , writes the result (shared resource) onto a file and it waits using wait() command and when a worker thread completes its job it calls notifyall()

5. Threads works around the critical section using synchronized (results){} block, where results is the shared resource

 */
package palindromefinder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class PalindromeFinder {

   private void File2Dictionary(){
        
       //Input from File and Storing in Dictionary ArrayList
        
       dictionary=new ArrayList<String>();
       
        FileInputStream file=null;
        try {
            file = new FileInputStream("words.txt");
        } catch (FileNotFoundException ex) {
            
        }
        
        BufferedReader buffer=new BufferedReader(new InputStreamReader(file));
        
        String strLine;

        try {
            //Read File Line By Line
            while ((strLine = buffer.readLine()) != null)   {
               
                dictionary.add(strLine);
            }
            
              //Close the input stream
                buffer.close();
        } catch (IOException ex) {
            
        }
    
    }
    
   
    private void findLargestWordLength(){
        
         //Calculate Largest String Length in the Dictionary
        
        
        
        for(String word : dictionary)
        {
            if(word.length()>largestWordLength)
            {
                largestWordLength=word.length();
            }
        }
    
    }
    
    private void creatingBags(){
    
        boolean check=false;
        int count=1; //Used to find Array of Words whose length == count and then we increment count
        
       bagOfTasks = new HashMap<String, ArrayList<String>>(); //To Store <String Length, ArrayList of all words equal to Length>
        lengths=new ArrayList<String>(); //To store lengths of bags

          //Creating Bags , Filling the HashMap bagOfTasks
         //Find all the words in dictionary with the length=count and add them in a bag/list arr and add that bag in hashmap
        //First find all the words in dictionary with length 1 and then 2 and then 3 and so on until the Largest String Size
        
        while(count<=largestWordLength){
        
        
        ArrayList<String> arr=new ArrayList<>();
        
     
        for(int l=0;l<dictionary.size();l++)
        {
            if(dictionary.get(l).length()== count)
            {
                arr.add(dictionary.get(l));
            }
        }
        
        bagOfTasks.put(Integer.toString(count), arr);
        lengths.add(Integer.toString(count));
       
        
        count++; //For next iteration
        
        if(count>largestWordLength)
        {
            break;
        }
                
        //Loop to find next count or next word whose length is greater than the last bag (or last count)
        //e.g First count was 1 and we find all the words in the dictinary with length 1 and then we need to find the next count which is available in dictionary
        // so that we don't end up with an empty bag , i.e what if we make a bag of length 2 but there are no words in the dictionary with length =2 so we need to find the next
        //valid count for which we need to fill the next bag
           
                check=false;
        while(!check){
            
            for(int x=0;x<dictionary.size();x++)
            {
                
                if(dictionary.get(x).length() == count)
                {
                   
                    check=true;
                   
                    break;
                }
            }
            if(!check)
            count++;
        }
       
        } //end of while
    
    }
    
    
    private void printBags(){
    
        System.out.println("Total # Bags : "+lengths.size()+ " , Dictionary Size : "+dictionary.size());
        
        for(String length:lengths)
        {
            ArrayList<String> temp=bagOfTasks.get(length);
            System.out.print("Bag Length : "+length+" Word Count :" +temp.size()+" [");
            for(String word : temp)
            {
                System.out.print(word+", ");
            }
            System.out.println("]");
        }
        
    
    }
    
    private void getNumOfWorkers(){
    
         System.out.print("Enter # of Worker Threads : ");
        Scanner sc = new Scanner(System.in);
       numOfWorkers = sc.nextInt();
  
        if(numOfWorkers >lengths.size() || numOfWorkers  <1)
        {
        do{
        
   //No use of having worker threads more than the bags count or threads less than 1
            System.out.println();
            System.out.print("Enter # of Worker Threads less than or equal to # of Bags ( "+lengths.size()+")  or >= 1: ");
            numOfWorkers =sc.nextInt();
            
        }while(numOfWorkers >lengths.size() || numOfWorkers  <1);
            
        }
    }
    
    private void distributeBags(){
    
        getNumOfWorkers();
        int bagPerThread=lengths.size()/numOfWorkers; //Amount of bags we need to give to each worker thread
        int maxBag=bagPerThread;
        int countBagsAssingned=0; 
        
        divideThread=new HashMap<>();
        
        int indexBag=0;
        
        for(int i=1;i<=numOfWorkers;i++)
        {
            ArrayList<String> temp=new ArrayList<>();
            
            for(;indexBag<maxBag;indexBag++)
            {
            temp.add(lengths.get(indexBag));
            countBagsAssingned++;
            }
            
           maxBag+=bagPerThread;
            
                divideThread.put(Integer.toString(i),temp);
        }

        //Remaining Bags Left , which are yet to be assignmend to Workers
        
        int remainingBags=lengths.size()-countBagsAssingned;
        
        //We are assinging all the remaining bags to the first worker thread (we can also do a random and give the remaining bags to random worker threads)
        
       ArrayList<String> firstWorkerBags=divideThread.get("1");
       
       for(int i=countBagsAssingned+1;i<=lengths.size();i++)
       {
           firstWorkerBags.add(lengths.get(indexBag));
           indexBag++;
       }
       
       divideThread.replace("1", firstWorkerBags);
       
       //Printing # Thread alongwith the Bag# which will be assingned to them
        
        for(int x=0;x<divideThread.size();x++)
        {
            
            ArrayList<String> temp=divideThread.get(Integer.toString(x+1));
            
            System.out.print("Thread # "+Integer.toString(x+1) + " Bags # :"+temp);
            System.out.println();
        }
       
    }
    
    
    public static void main(String[] args) {
        
        
        PalindromeFinder pF=new PalindromeFinder();
        
        pF.File2Dictionary(); //Copying Words from File to Dictionary
        pF.findLargestWordLength(); //Finding Largest Word Length
        pF.creatingBags(); //Creating Map of Bags
        pF.printBags(); //Printing the Map
        pF.distributeBags(); //Distributing bags among workers (In form of a map)
        
         ArrayList<String> result=new ArrayList<>(); //Shared Array Resource among Threads , will contain final result and also to be written onto a result file
       
       //PalindromicWriter thread which will write result onto File 
       palindromeWriter=new PalindromeWriter(result,divideThread.size(),"result.txt");
       palindromeWriter.start();
       
       //Creating Worker Threads and Assigning them their respective Bags
       
       for(int x=0;x<divideThread.size();x++)
       {
           ArrayList<String> bagList=divideThread.get(Integer.toString(x+1));
           
           ArrayList<ArrayList<String>> bag=new ArrayList<ArrayList<String>>();
           
          for(String b:bagList)
          {
              bag.add(bagOfTasks.get(b));
          }
            WorkerThread worker=new WorkerThread("Worker "+Integer.toString(x+1),result,dictionary,bag);
            worker.start();
       }

    }

    //Variables
    private static ArrayList<String> dictionary; //To Store all words from file
    //Calculate Largest String Length in the Dictionary

    private int largestWordLength = 0;
    private static HashMap<String, ArrayList<String>> bagOfTasks; //To Store <String Length, ArrayList of all words equal to Length>
    private static HashMap<String,ArrayList<String>> divideThread;  //Map to store <Thread #, # of Bags(BagID)>
    private static ArrayList<String> lengths; //To store lengths of bags
    private int numOfWorkers;

    private static PalindromeWriter palindromeWriter; //PalindromicWriter thread which will write result onto File 

}
