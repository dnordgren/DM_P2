import java.io.*;
import java.util.*;

class AprioriCalculation
{   
	//current candidates
    Vector<String> candidates = new Vector<String>(); 
    
    int maxItemID;
    int numTransactions;
    //minimum occurrences for a frequent item set
    int frequentNum; 

    //the frequent candidates for the current item set
    Vector<String> frequentCandidates = new Vector<String>(); 
    
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
    	//date object for getting time
        Date d; 
        //start and end time
        double start, end; 

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
            
        //stops if there are <= 1 frequent items
        } while(candidates.size() > 1 && currentItemset < frequentNum);
       
        //get time right after execution ends
        d = new Date();
        end = d.getTime();

        System.out.println("Execution Complete");
        System.out.println("Program executed in "+((end-start)/1000) + " seconds.");
        System.out.println("Output for a minimum support threshold of " + frequentNum + " was written to '" + outputFile + "'");
    }

    private void findCandidates(int set)
    {
    	//temporary candidate string vector
        Vector<String> candidatesTemp = new Vector<String>();

        //if its the first set, candidates are all the numbers
        if(set == 1)
        {
            for(int i = 0; i <= maxItemID; i++)
            {
                candidatesTemp.add(Integer.toString(i));
            }
        }
        //second item set is just all combinations of items in item set 1
        else if(set == 2) 
        {
            //add each item set from the previous frequent item sets together
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
            } 
        	catch (IOException e) 
            {
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
            } 
        	catch (IOException e) 
            {
            	System.out.println(e);
            }
        }
        candidates.clear();
        candidates = new Vector<String>(new LinkedHashSet<String>(candidatesTemp));
        candidatesTemp.clear();
    }

    private void findFrequentItemsets(int set)
    {
    	//whether the transaction has all the items in an item set
        boolean match; 
        //successful matches count
        int count[] = new int[candidates.size()]; 
        StringTokenizer candidateTokenizer, transactionTokenizer;

        try
        {
        	fileIn = new FileInputStream(inputFile);
            bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
            
            fileWriter = new FileWriter(outputFile, false);
            bufferedOut = new BufferedWriter(fileWriter);
            
            for(int i = 0; i < numTransactions; i++)
            {
            	//read a line from the file to the tokenizer
                transactionTokenizer = new StringTokenizer(bufferedIn.readLine(), " "); 
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
                        //if one is not present, can stop checking the line
                        if(!match)
                        {
                            break;
                        }
                    }
                    //if the last item checked was a match, that means all items matched
                    if(match) 
                    {
                        count[c]++;
                    }
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
                    {
                    	bufferedOut.write("("+ candidates.get(i) + ") " + count[i] + "\n");
                    }
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
