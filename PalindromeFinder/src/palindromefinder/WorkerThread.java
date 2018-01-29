/*
Muhammad Zain UL Islam / Zain-Sra
Advance Programming (Section A) 
Assingment

Using Multithreading finding Paladromic Words 

This is WorkerThread class which extends Thread and is used to create Threads.
Each thread has a set of bags which contains words.
Worker Thread picks words from its bags and check if it's palindromic (reverse exists in dictionary) or not ,if it exists it writes the word onto Result Array which is shared
among all the threads. 
Worker THread complete it's task when it has found all the palindromic words in all of the bags which were assinged to it.
 */
package palindromefinder;

import java.util.ArrayList;

public class WorkerThread extends Thread {

    private String id;
    private ArrayList<String> result;
    private ArrayList<String> dictionary;
    private ArrayList<ArrayList<String>> bags;

    WorkerThread(String i, ArrayList<String> r, ArrayList<String> d, ArrayList<ArrayList<String>> b) {

        this.id = i;
        this.result = r;
        this.dictionary = d;
        this.bags = b;

        System.out.println("I am " + id);
        System.out.println("My Array : " + bags.toString());

    }

    public void run() {

        int count = 0; //To store the # of Palindromic Words which the Thread will find

        for (int i = 0; i < bags.size(); i++) {

            ArrayList<String> temp = bags.get(i);

            boolean check = false;

            for (String aword : temp) {

                check = isPalindromic(aword);

                if (check == true) {

                    count++;
                    synchronized (result) {
                        result.add(aword);
                    }

                }

            }

        }

        //Critical section , as result array is shared by all the threads so i put synchronized there so that only one thread could access it at one time
        synchronized (result) {
            result.add(id + "Count_" + count);

            result.notifyAll(); //After Worker thread completes it's job , it notifies all the waiting threads to use the result array (in our case Palindromic Writer is the thread which is waiting)
        }
        System.out.println(id + " Done !");

    }

    //To find if the word is Palindromic or not 
    //Reverse the word and find it in dictionary , if it exists then it's palindromic otherwise not
    private boolean isPalindromic(String word) {

        StringBuilder string = new StringBuilder(word);

        // reverse StringBuilder input1
        word = string.reverse().toString();

        for (String dword : dictionary) {
            if (word.equals(dword)) {
                return true;
            }
        }

        return false;
    }

}
