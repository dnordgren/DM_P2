
/**
*
*
* File:
* Input files needed:
*      1. config.txt - three lines, each line is an integer
*          line 1 - number of items per transaction
*          line 2 - number of transactions
*          line 3 - minsup
*      2. transa.txt - transaction file, each line is a transaction, items are separated by a space
*/

//package apriori;	
import java.io.*;
import java.util.*;

public class Apriori {

    public static void main(String[] args) {
        AprioriCalculation ap = new AprioriCalculation();

        ap.aprioriProcess();
    }
}
