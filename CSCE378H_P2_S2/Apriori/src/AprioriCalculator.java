import java.io.*;
import java.util.*;


class AprioriCalculator
{   
	//current possible item combinations
    Vector<String> possibleCombos=new Vector<String>(); 
    
    int maxItemID;
    int numTransactions;
    //minimum occurrences for a frequent itemset
    int frequentNum;
    HashMap<String, Integer> sentiments;
    //the separator value for items in the database
    String itemSep = " ";

    Vector<String> frequentCombos = new Vector<String>(); //the frequent candidates for the current itemset
    Vector<int[]> frequentReviews = new Vector<int[]>(); 
    
    String inputFile;
    String sentimentFile;
    FileInputStream fileIn;
    BufferedReader bufferedIn;
    
    String outputFile;
    FileWriter fileWriter;
    BufferedWriter bufferedOut;
    
    public AprioriCalculator(int transactions, int maxItemNum, int supportThreshold, String inputPath, String sentimentPath, String outputPath)
    {
    	numTransactions = transactions;
    	maxItemID = maxItemNum;
    	frequentNum = supportThreshold;
    	inputFile = inputPath;
    	sentimentFile = sentimentPath;
    	outputFile = outputPath;
    }
    
    public void mainApriori()
    {
    	//date object for getting time
        Date d;
        //start and end time
        double startTime, endTime;
        
        sentiments = new HashMap<String, Integer>();
        try{
        	fileIn = new FileInputStream(sentimentFile);
        	bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
        	
        	String current;
            while((current = bufferedIn.readLine()) != null)
            {
            	sentiments.put(current.split("\t")[0], Integer.parseInt(current.split("\t")[1]));
            }
        } catch (Exception e)
        {}

        //get time right before execution begins
        d = new Date();
        startTime = d.getTime();

        System.out.println("Begin Execution");
        
        int currentItemset = 0;
        
        do
        {
        	//loop will run once for each itemset, up to frequentNumber
            currentItemset++;
            findCombos(currentItemset);
            findFrequentItemsets(currentItemset);
            
        //stops if there are <= 1 frequent Items
        }while(possibleCombos.size()>1 && currentItemset < frequentNum);
       
        //get time right after execution ends
        d = new Date();
        endTime = d.getTime();

        System.out.println("Execution Complete");
        System.out.println("Program executed in: "+ ((endTime - startTime)/1000) + " seconds.");
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
        		
        			fileWriter= new FileWriter(outputFile, false);
        			bufferedOut = new BufferedWriter(fileWriter);
                    
		        	for(int i = 0; i < numTransactions; i++)
		        	{
		        		String pattern = "(\\}?\\{)|\\}";
	                	String[] objectList = bufferedIn.readLine().split(pattern);
	                	String itemList = "";
	                	
	                	for(int j=1; j<objectList.length-1; j++) {
	                		itemList += objectList[j].split(",")[0];
	                		itemList += " ";
	                	}
		        		List<String> items = Arrays.asList(itemList.split(itemSep));
		        		
		        		for(int j=0; j<items.size(); j++)
		        		{
		        			if(possibleCombos.contains(items.get(j)))
		        			{
		        				for(int k=j+1; k<items.size(); k++)
		        				{
		        					if(possibleCombos.contains(items.get(k)))
		        					{
		        						possibleCombosTemp.add(items.get(j) + ", " + items.get(k));
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
        		
                fileWriter= new FileWriter(outputFile, false);
                bufferedOut = new BufferedWriter(fileWriter);
                    
		        	for(int i = 0; i < numTransactions; i++)
		        	{
		        		String pattern = "(\\}?\\{)|\\}";
	                	String[] objectList = bufferedIn.readLine().split(pattern);
	                	String itemList = "";
	                	
	                	for(int j=1; j<objectList.length-1; j++) {
	                		itemList += objectList[j].split(",")[0];
	                		itemList += " ";
	                	}
		        		List<String> items = Arrays.asList(itemList.split(itemSep));
		        		
		        		for(int j = 0; j < items.size(); j++)
		        		{
	        				for(int k = j+1; k < items.size(); k++)
	        				{
	        					if(possibleCombos.contains(items.get(j) + ", " + items.get(k)))
	        					{
	        						for(int l = k+1; l < items.size(); l++)
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
            } catch (IOException e) {
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
        int count[] = new int[possibleCombos.size()]; //the number of successful matches
        int review[][] = new int[possibleCombos.size()][7];
        StringTokenizer comboTokenizer, transactionTokenizer, reviewTokenizer;

        try
        {
        		fileIn = new FileInputStream(inputFile);
        		bufferedIn = new BufferedReader(new InputStreamReader(fileIn));
        		
                fileWriter= new FileWriter(outputFile, false);
                bufferedOut = new BufferedWriter(fileWriter);
                
                for(int i = 0; i < numTransactions; i++)
                {
                	String pattern = "(\\}?\\{)|\\}";
                	String[] objectList = bufferedIn.readLine().split(pattern);
                	String itemList = "";
                	String reviewList = "";
                	for(int j = 1; j < objectList.length-1; j++) {
                		itemList += objectList[j].split(",")[0];
                		itemList += " ";
                		reviewList += objectList[j].split(",")[1];
                		reviewList += ",";
                	}
                	String weekday = objectList[objectList.length-1];
                	
                	//read a line from the file to the tokenizer
                	transactionTokenizer = new StringTokenizer(itemList, itemSep);
                    reviewTokenizer = new StringTokenizer(reviewList, ",");
                	boolean trans[] = new boolean[maxItemID+1];
                    int reviewNumber[][] = new int[maxItemID+1][7];
                    
                    while(transactionTokenizer.hasMoreTokens())
                    {
                        int itemID = Integer.parseInt(transactionTokenizer.nextToken());
                    	trans[itemID] = true;
                        reviewNumber[itemID][GetWeekdayIndex(weekday)] = calculateSentiment(reviewTokenizer.nextToken());
                    }
                    
                    //check all possibleCombos
                    for(int c = 0; c < possibleCombos.size(); c++)
                    {
                        match = false;
                        int[] totalSentiment = new int[7];
                        comboTokenizer = new StringTokenizer(possibleCombos.get(c), ", ");
                        //check each item to see if it is in the current transaction line
                        while(comboTokenizer.hasMoreTokens())
                        {
                        	int itemID = Integer.valueOf(comboTokenizer.nextToken());
                            match = trans[itemID];
                            
                            for(int k = 0; k < 7; k++){
	                            totalSentiment[k] += reviewNumber[itemID][k];
	                        }
                            //if one is not present, can stop checking the line
                            if(!match)
                                break;
                        }
                        //if the last item checked was a match, that means all items matched
                        if(match) {
                            count[c]++;
                        	for(int m = 0; m < 7; m++){
                        		review[c][m] += totalSentiment[m];
                        	}
                        }
                    }
                    
                }
                for(int i = 0; i < possibleCombos.size(); i++)
                {
                	//if the count is larger than the support threshold, the candidate is frequent
                    if(count[i] >= frequentNum)
                    {
                        frequentCombos.add(possibleCombos.get(i));
                        frequentReviews.add(review[i]);
                        //add the frequent candidate to the output file, along with the number of occurrences
                        if(set == frequentNum)
                        	bufferedOut.write("( "+ possibleCombos.get(i) + ") " + review[i][0] + " " + review[i][1] + " " + review[i][2] + " " + review[i][3] + " " + review[i][4] + " " + review[i][5] + " " + review[i][6] + "\n");
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
        frequentReviews.clear();
    }
    
    private int GetWeekdayIndex(String input) {
    	int result = 0;
    	if(input.equals("Sunday"))
    		result = 0;
    	else if (input.equals("Monday"))
    		result = 1;
    	else if (input.equals("Tuesday"))
    		result = 2;
    	else if (input.equals("Wednesday"))
    		result = 3;
    	else if (input.equals("Thursday"))
    		result = 4;
    	else if (input.equals("Friday"))
    		result = 5;
    	else if (input.equals("Saturday"))
    		result = 6;
    	return result;
    }
    
    private int calculateSentiment(String input) {
    	int sum = 0;
    	StringTokenizer tok = new StringTokenizer(input, " ");
    	
    	while(tok.hasMoreTokens()) {
    		sum += sentiments.get(tok.nextToken());
    	}
    	return sum;
    }
}