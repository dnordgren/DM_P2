import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.*;

class AprioriCalculation
{   
    Vector<String> candidates=new Vector<String>(); //current candidates
    String inputFile="transactions.txt";
    String outputFile="project_2_output.txt";
    
    int maxItemID;
    int numTransactions; //number of transactions
    int frequentNum; //minimum occurrences for a frequent itemset
    String itemSep = " "; //the separator value for items in the database

    Vector<String> frequentCandidates = new Vector<String>(); //the frequent candidates for the current itemset
    FileInputStream fileIn; //file input stream
    BufferedReader bufferedIn; //data input stream
    FileWriter fileWriter;
    BufferedWriter fileOut;
    
    public void aprioriProcess()
    {
        Date d; //date object for timing purposes
        long start, end; //start and end time
        
        int itemsetNumber=0;
        maxItemID=999;
        numTransactions=3000;

        //frequentNum
        frequentNum=3;

        //start timer
        d = new Date();
        start = d.getTime();

        System.out.println("Begin Execution");
        
        do
        {
        	//loop will run once for each itemset, up to frequentNumber
            itemsetNumber++;

            //generate the candidates
            findCandidates(itemsetNumber);

            //determine and display frequent itemsets
            findFrequentItemsets(itemsetNumber);
            
        //if there are <=1 frequent items, then its the end. This prevents reading through the database again. When there is only one frequent itemset.
        }while(candidates.size()>1 && itemsetNumber < frequentNum);

        System.out.println("Frequent " + itemsetNumber + "-itemsets");
        System.out.println(candidates);
       
        //end timer
        d = new Date();
        end = d.getTime();

        //display the execution time
        System.out.println("Execution time is: "+((double)(end-start)/1000) + " seconds.");
    }

    private void findCandidates(int n)
    {
        Vector<String> tempCandidates = new Vector<String>(); //temporary candidate string vector

        //if its the first set, candidates are all the numbers
        if(n==1)
        {
            for(int i=0; i<=maxItemID; i++)
            {
                tempCandidates.add(Integer.toString(i));
            }
        }
        else if(n==2) //second itemset is just all combinations of items in itemset 1
        {
            //add each itemset from the previous frequent itemsets together
        	try
            {
                    fileWriter = new FileWriter(outputFile, false);
                    fileOut = new BufferedWriter(fileWriter);
                    fileIn = new FileInputStream(inputFile);
                    bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
                    
		        	for(int i=0; i<numTransactions; i++)
		        	{
		        		List<String> items = Arrays.asList(bufferedIn.readLine().split(itemSep));
		        		for(int j=0; j<items.size(); j++)
		        		{
		        			if(candidates.contains(items.get(j)))
		        			{
		        				for(int k=j+1; k<items.size(); k++)
		        				{
		        					if(candidates.contains(items.get(k)))
		        					{
		        						tempCandidates.add(items.get(j) + ", " + items.get(k));
		        					}
		        				}
		        			}
		        		}
		        	}
            } catch (IOException e) {
            	System.out.println(e);
            }
        }
        else
        {
        	try
            {
                    fileWriter = new FileWriter(outputFile, false);
                    fileOut = new BufferedWriter(fileWriter);
                    fileIn = new FileInputStream(inputFile);
                    bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
                    
		        	for(int i=0; i<numTransactions; i++)
		        	{
		        		List<String> items = Arrays.asList(bufferedIn.readLine().split(itemSep));
		        		for(int j=0; j<items.size(); j++)
		        		{
	        				for(int k=j+1; k<items.size(); k++)
	        				{
	        					if(candidates.contains(items.get(j) + ", " + items.get(k)))
	        					{
	        						for(int l=k+1; l<items.size(); l++)
			        				{
			        					if(candidates.contains(items.get(j) + ", " + items.get(l)))
			        					{
			        						tempCandidates.add(items.get(j) + ", " + items.get(k) + ", " + items.get(l));
			        					}
			        			
			        				}
	        					}
	        			
	        				}
		        			
		        		}
		        	}
            } catch (IOException e) {
            	System.out.println(e);
            }
        }
        candidates.clear();
        candidates = new Vector<String>(new LinkedHashSet<String>(tempCandidates));
        tempCandidates.clear();
    }

    private void findFrequentItemsets(int n)
    {
    	StringTokenizer st, stFile;
        boolean match; //whether the transaction has all the items in an itemset
        int count[] = new int[candidates.size()]; //successful matches count

        try
        {
                fileWriter = new FileWriter(outputFile, false);
                fileOut = new BufferedWriter(fileWriter);
                fileIn = new FileInputStream(inputFile);
                bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
                
                //loop through all transactions
                for(int i=0; i<numTransactions; i++)
                {
                    stFile = new StringTokenizer(bufferedIn.readLine(), itemSep); //read a line from the file to the tokenizer
                    boolean trans[] = new boolean[maxItemID+1];
                    
                    while(stFile.hasMoreTokens())
                    {
                        trans[Integer.parseInt(stFile.nextToken())] = true;
                    }
                    
                    //check all candidates
                    for(int c=0; c<candidates.size(); c++)
                    {
                        match = false;
                        st = new StringTokenizer(candidates.get(c), ", ");
                        //check each item to see if it is in the current transaction line
                        while(st.hasMoreTokens())
                        {
                            match = (trans[Integer.valueOf(st.nextToken())]);
                            if(!match) //if one is not present, can stop checking the line
                                break;
                        }
                        if(match) //if at this point it is a match, increase the count
                            count[c]++;
                    }
                    
                }
                for(int i=0; i<candidates.size(); i++)
                {
                    //if the count% is larger than the frequentNum%, add to the candidate to the frequent candidates
                    if(count[i]>=frequentNum)
                    {
                        frequentCandidates.add(candidates.get(i));
                        //put the frequent itemset into the output file
                        if(n==frequentNum)
                        	fileOut.write("("+ candidates.get(i) + ") " + count[i] + "\n");
                    }
                }
                fileOut.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        candidates.clear();
        candidates = new Vector<String>(frequentCandidates);
        frequentCandidates.clear();
    }
}