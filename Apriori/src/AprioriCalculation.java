import java.io.*;
import java.util.*;

class AprioriCalculator
{   
	//current possibleCombos
    Vector<String> possibleCombos = new Vector<String>(); 
    
    int maxItemID;
    int numTransactions;
    //minimum occurrences for a frequent item set
    int frequentNum; 

    //the frequent Combos for the current item set
    Vector<String> frequentCombos = new Vector<String>(); 
    
    String inputFile;
    FileInputStream fileIn;
    BufferedReader bufferedIn;
    
    String outputFile;
    FileWriter fileWriter;
    BufferedWriter bufferedOut;
    
    public AprioriCalculator(int transactions, int maxItemNum, int supportThreshold, String inputPath, String outputPath)
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
        Date date; 
        //start and end time
        double startTime, endTime; 

        //get time right before execution begins
        date = new Date();
        startTime = date.getTime();

        System.out.println("Start");
        
        int currentItemset = 0;
        
        do
        {
            //loop will run once for each itemset, up to frequentNumber
            currentItemset++;
            findCombos(currentItemset);
            findFrequentItemsets(currentItemset);
            
        //stops if there are <= 1 frequent items
        } while(possibleCombos.size() > 1 && currentItemset < frequentNum);
       
        //get time right after execution ends
        date = new Date();
        endTime = date.getTime();

        System.out.println("Execution Complete");
        System.out.println("Program executed in "+((endTime - startTime)/1000) + " seconds.");
        System.out.println("Output for a minimum support threshold of " + frequentNum + " was written to '" + outputFile + "'");
    }

    private void findCombos(int set)
    {
    	//temporary combos string vector
        Vector<String> possibleCombosTemp = new Vector<String>();

        //if its the first set, possibleCombos are all the numbers
        if(set == 1)
        {
            for(int i = 0; i <= maxItemID; i++)
            {
                possibleCombosTemp.add(Integer.toString(i));
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
	        			if(possibleCombos.contains(items.get(j)))
	        			{
	        				for(int k = j+1; k < items.size(); k++)
	        				{
	        					if(possibleCombos.contains(items.get(k)))
	        					{
	        						possibleCombosTemp.add(items.get(j) + ", " + items.get(k));
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
        					if(possibleCombos.contains(items.get(j) + ", " + items.get(k)))
        					{
        						for(int l=k+1; l<items.size(); l++)
		        				{
		        					if(possibleCombos.contains(items.get(j) + ", " + items.get(l)))
		        					{
		        						possibleCombosTemp.add(items.get(j) + ", " + items.get(k) + ", " + items.get(l));
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
        possibleCombos.clear();
        possibleCombos = new Vector<String>(new LinkedHashSet<String>(possibleCombosTemp));
        possibleCombosTemp.clear();
    }

    private void findFrequentItemsets(int set)
    {
    	//whether the transaction has all the items in an item set
        boolean match; 
        //successful matches count
        int count[] = new int[possibleCombos.size()]; 
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
                
                //check all possibleCombos
                for(int c = 0; c < possibleCombos.size(); c++)
                {
                    match = false;
                    candidateTokenizer = new StringTokenizer(possibleCombos.get(c), ", ");
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
            for(int i = 0; i < possibleCombos.size(); i++)
            {
                //if the count is larger than the support threshold, the candidate is frequent
                if(count[i] >= frequentNum)
                {
                    frequentCombos.add(possibleCombos.get(i));
                    //add the frequent candidate to the output file, along with the number of occurrences
                    if(set == frequentNum)
                    {
                    	bufferedOut.write("("+ possibleCombos.get(i) + ") " + count[i] + "\n");
                    }
                }
            }
            bufferedOut.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        possibleCombos.clear();
        possibleCombos = new Vector<String>(frequentCombos);
        frequentCombos.clear();
    }
}
