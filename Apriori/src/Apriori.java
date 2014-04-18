
/**
*
* @author Nathan Magnus, under the supervision of Howard Hamilton
* Copyright: University of Regina, Nathan Magnus and Su Yibin, June 2009.
* No reproduction in whole or part without maintaining this copyright notice
* and imposing this condition on any subsequent users.
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
