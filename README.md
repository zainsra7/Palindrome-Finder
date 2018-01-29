# Palindrome-Finder
Finding all possible palindromic words from Dictionary using Multi-threading

## Source Code
  * Fully Documented
  * Console based application written in Java language

### This was an assingment for my undergrad course of Advance Programming (Fall 2017, Fast-Nu Lahore).

## My Algotrithm

1. Reads Words from Dictionary File and Create Bags where each bag has a specific length and contains a list of words of that length.

     * File (words.txt) => Bags (Map <length,ArrayList<String>>) 

2. Get number of Worker Threads from user 

3. Distribute and Assing Bags to Worker Threads and start the threads.

4. Each Worker Thread finds Palindrome words in its assingned bag and reports the total number of Palindrome words found at the end of its job through call to notifyall().

5. Palindrome Writer , writes the results (shared resource) onto a file and it waits using wait() command and when a worker thread completes its job it calls notifyall() to tell the writer that it has completed its job so that the writer can write the report in file as in "Thread_ID number_of_palindromes_found"

      * results array => result.txt

5. Threads works around the critical section using synchronized (results){} block, where results is the shared resource (arrayList)


