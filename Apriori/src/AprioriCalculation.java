import java.io.*;
import java.util.*;

class AprioriCalculation
{   
    Vector<String> candidates=new Vector<String>(); //current candidates
    
    int maxItemID;
    int numTransactions;
    int frequentNum; //minimum occurrences for a frequent itemset...support threshold

    Vector<String> frequentCandidates = new Vector<String>(); //the frequent candidates for the current itemset
    
    String inputFile;
    FileInputStream fileIn;
    BufferedReader bufferedIn;
    
    String outputFile;
    FileWriter fileWriter;
    BufferedWriter bufferedOut;
    
    public AprioriCalculation(int transactions, int maxItemNum, int supportThreshold, String inputPath, String outputPath)
    {
    	numTransactions = transactions;
    	maxItemID = maxItemNum;
    	frequentNum = supportThreshold;
    	inputFile = inputPath;
    	outputFile = outputPath;
    }
    
    public void mainApriori()
    {
        Date d; //date object for getting time
        double start, end; //start and end time

        //get time right before execution begins
        d = new Date();
        start = d.getTime();

        System.out.println("Begin Program Execution");
        
        int currentItemset = 0;
        
        do
        {
        	//loop will run once for each itemset, up to frequentNumber
            currentItemset++;

            findCandidates(currentItemset);

            findFrequentItemsets(currentItemset);
            
        //Stops if there are <= 1 frequent items
        }while(candidates.size()>1 && currentItemset < frequentNum);
       
        //get time right after execution endss
        d = new Date();
        end = d.getTime();

        System.out.println("Execution Complete");
        System.out.println("Program executed in "+((end-start)/1000) + " seconds.");
        System.out.println("Output for a minimum support threshold of " + frequentNum + " was written to '" + outputFile + "'");
    }

    private void findCandidates(int set)
    {
        Vector<String> candidatesTemp = new Vector<String>(); //temporary candidate string vector

        //if its the first set, candidates are all the numbers
        if(set == 1)
        {
            for(int i = 0; i <= maxItemID; i++)
            {
                candidatesTemp.add(Integer.toString(i));
            }
        }
        else if(set == 2) //second itemset is just all combinations of items in itemset 1
        {
            //add each itemset from the previous frequent itemsets together
        	try
            {
        		fileIn = new FileInputStream(inputFile);
                bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
                fileWriter = new FileWriter(outputFile, false);
                bufferedOut = new BufferedWriter(fileWriter);
                
	        	for(int i = 0; i < numTransactions; i++)
	        	{
	        		List<String> items = Arrays.asList(bufferedIn.readLine().split(" "));
	        		for(int j = 0; j < items.size(); j++)
	        		{
	        			if(candidates.contains(items.get(j)))
	        			{
	        				for(int k = j+1; k < items.size(); k++)
	        				{
	        					if(candidates.contains(items.get(k)))
	        					{
	        						candidatesTemp.add(items.get(j) + ", " + items.get(k));
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
        		fileIn = new FileInputStream(inputFile);
                bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
                fileWriter = new FileWriter(outputFile, false);
                bufferedOut = new BufferedWriter(fileWriter);
                
	        	for(int i = 0; i < numTransactions; i++)
	        	{
	        		List<String> items = Arrays.asList(bufferedIn.readLine().split(" "));
	        		for(int j = 0; j < items.size(); j++)
	        		{
        				for(int k = j+1; k < items.size(); k++)
        				{
        					if(candidates.contains(items.get(j) + ", " + items.get(k)))
        					{
        						for(int l=k+1; l<items.size(); l++)
		        				{
		        					if(candidates.contains(items.get(j) + ", " + items.get(l)))
		        					{
		        						candidatesTemp.add(items.get(j) + ", " + items.get(k) + ", " + items.get(l));
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
        candidates = new Vector<String>(new LinkedHashSet<String>(candidatesTemp));
        candidatesTemp.clear();
    }

    private void findFrequentItemsets(int set)
    {
        boolean match; //whether the transaction has all the items in an itemset
        int count[] = new int[candidates.size()]; //successful matches count
        StringTokenizer candidateTokenizer, transactionTokenizer;

        try
        {
        	fileIn = new FileInputStream(inputFile);
            bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
            fileWriter = new FileWriter(outputFile, false);
            bufferedOut = new BufferedWriter(fileWriter);
            
            for(int i = 0; i < numTransactions; i++)
            {
                transactionTokenizer = new StringTokenizer(bufferedIn.readLine(), " "); //read a line from the file to the tokenizer
                boolean trans[] = new boolean[maxItemID+1];
                
                while(transactionTokenizer.hasMoreTokens())
                {
                    trans[Integer.parseInt(transactionTokenizer.nextToken())] = true;
                }
                
                //check all candidates
                for(int c = 0; c < candidates.size(); c++)
                {
                    match = false;
                    candidateTokenizer = new StringTokenizer(candidates.get(c), ", ");
                    //check each item to see if it is in the current transaction line
                    while(candidateTokenizer.hasMoreTokens())
                    {
                        match = (trans[Integer.valueOf(candidateTokenizer.nextToken())]);
                        if(!match) //if one is not present, can stop checking the line
                            break;
                    }
                    if(match) //if the last item checked was a match, that means all items matched
                        count[c]++;
                }
                
            }
            for(int i = 0; i < candidates.size(); i++)
            {
                //if the count is larger than the support threshold, the candidate is frequent
                if(count[i] >= frequentNum)
                {
                    frequentCandidates.add(candidates.get(i));
                    //add the frequent candidate to the output file, along with the number of occurrences
                    if(set == frequentNum)
                    	bufferedOut.write("("+ candidates.get(i) + ") " + count[i] + "\n");
                }
            }
            bufferedOut.close();
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