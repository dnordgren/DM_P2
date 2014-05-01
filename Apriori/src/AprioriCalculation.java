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
    Vector<String> candidates=new Vector<String>(); //the current candidates 
    String configFile="config.txt"; //configuration file
    String transaFile="transa (1).txt"; //transaction file
    String sentimentFile="sentiment.csv";
    String outputFile="apriori-output.txt";//output file
    
    int maxItemID;
    int numTransactions; //number of transactions
    double minSup; //minimum support for a frequent itemset
    HashMap<String, Integer> sentiments;
    String itemSep = " "; //the separator value for items in the database

    Vector<String> frequentCandidates = new Vector<String>(); //the frequent candidates for the current itemset
    Vector<int[]> frequentReviews = new Vector<int[]>(); 
    
    FileInputStream file_in; //file input stream
    BufferedReader data_in; //data input stream
    FileWriter fw;
    BufferedWriter file_out;
    
    public void aprioriProcess()
    {
        Date d; //date object for timing purposes
        long start, end; //start and end time
        int itemsetNumber=0; //the current itemset being looked at
        
        sentiments = new HashMap<String, Integer>();
        try{
        	file_in = new FileInputStream(sentimentFile);
        	data_in = new BufferedReader(new InputStreamReader(file_in));
        	
        	String current;
            while((current = data_in.readLine()) != null)
            {
            	sentiments.put(current.split("\t")[0], Integer.parseInt(current.split("\t")[1]));
            }
        } catch (Exception e)
        {}
        
        
        
        
        maxItemID=999;

       //number of transactions
       numTransactions=3000;

       //minsup
       minSup=3;

        //start timer
        d = new Date();
        start = d.getTime();

        System.out.println("Begin Execution");
        
        //while not complete
        do
        {
        	//increase the itemset that is being looked at
            itemsetNumber++;

            //generate the candidates
            generateCandidates(itemsetNumber);

            //determine and display frequent itemsets
            calculateFrequentItemsets(itemsetNumber);
            
        //if there are <=1 frequent items, then its the end. This prevents reading through the database again. When there is only one frequent itemset.
        }while(candidates.size()>1 && itemsetNumber < minSup);

        System.out.println("Frequent " + itemsetNumber + "-itemsets");
        System.out.println(candidates);
       
        //end timer
        d = new Date();
        end = d.getTime();

        //display the execution time
        System.out.println("Execution time is: "+((double)(end-start)/1000) + " seconds.");
    }

    public static String getInput()
    {
        String input="";
        //read from System.in
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //try to get users input, if there is an error print the message
        try
        {
            input = reader.readLine();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return input;
    }

    private void generateCandidates(int n)
    {
        Vector<String> tempCandidates = new Vector<String>(); //temporary candidate string vector

        //if its the first set, candidates are just the numbers
        if(n==1)
        {
            for(int i=0; i<=maxItemID; i++)
            {
                tempCandidates.add(Integer.toString(i));
            }
        }
        else if(n==2) //second itemset is just all combinations of itemset 1
        {
            //add each itemset from the previous frequent itemsets together
            
        	try
            {
                    //output file
                    fw= new FileWriter(outputFile, false);
                    file_out = new BufferedWriter(fw);
                    //load the transaction file
                    file_in = new FileInputStream(transaFile);
                    data_in = new BufferedReader(new InputStreamReader(file_in));
                    
		        	for(int i=0; i<numTransactions; i++)
		        	{
		        		String pattern = "(\\}?\\{)|\\}";
	                	String[] objectList = data_in.readLine().split(pattern);
	                	String itemList = "";
	                	String reviewList = "";
	                	for(int j=1; j<objectList.length-1; j++) {
	                		itemList += objectList[j].split(",")[0];
	                		itemList += " ";
	                		reviewList += objectList[j].split(",")[1];
	                		reviewList += ",";
	                	}
	                	String weekday = objectList[objectList.length-1];
		        		List<String> items = Arrays.asList(itemList.split(itemSep));
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
                    //output file
                    fw= new FileWriter(outputFile, false);
                    file_out = new BufferedWriter(fw);
                    //load the transaction file
                    file_in = new FileInputStream(transaFile);
                    data_in = new BufferedReader(new InputStreamReader(file_in));
                    
		        	for(int i=0; i<numTransactions; i++)
		        	{
		        		String pattern = "(\\}?\\{)|\\}";
	                	String[] objectList = data_in.readLine().split(pattern);
	                	String itemList = "";
	                	String reviewList = "";
	                	for(int j=1; j<objectList.length-1; j++) {
	                		itemList += objectList[j].split(",")[0];
	                		itemList += " ";
	                		reviewList += objectList[j].split(",")[1];
	                		reviewList += ",";
	                	}
	                	String weekday = objectList[objectList.length-1];
		        		List<String> items = Arrays.asList(itemList.split(itemSep));
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
        //clear the old candidates
        candidates.clear();
        //set the new ones
        candidates = new Vector<String>(new LinkedHashSet<String>(tempCandidates));
        tempCandidates.clear();
    }

    private void calculateFrequentItemsets(int n)
    {
    	StringTokenizer st, stFile, stReview; //tokenizer for candidate and transaction
        boolean match; //whether the transaction has all the items in an itemset
        int count[] = new int[candidates.size()]; //the number of successful matches
        int review[][] = new int[candidates.size()][7];

        try
        {
                //output file
                fw= new FileWriter(outputFile, false);
                file_out = new BufferedWriter(fw);
                //load the transaction file
                file_in = new FileInputStream(transaFile);
                data_in = new BufferedReader(new InputStreamReader(file_in));
                
                //for each transaction
                System.out.println(candidates.size());
                for(int i=0; i<numTransactions; i++)
                {
                	String pattern = "(\\}?\\{)|\\}";
                	String[] objectList = data_in.readLine().split(pattern);
                	String itemList = "";
                	String reviewList = "";
                	for(int j=1; j<objectList.length-1; j++) {
                		itemList += objectList[j].split(",")[0];
                		itemList += " ";
                		reviewList += objectList[j].split(",")[1];
                		reviewList += ",";
                	}
                	String weekday = objectList[objectList.length-1];
                	
                	System.out.println("Transaction " + i + ": " + itemList + weekday);
                	
                	stFile = new StringTokenizer(itemList, itemSep); //read a line from the file to the tokenizer
                    stReview = new StringTokenizer(reviewList, ",");
                	boolean trans[] = new boolean[maxItemID+1];
                    int reviewNumber[][] = new int[maxItemID+1][7];
                    
                    while(stFile.hasMoreTokens())
                    {
                        int itemID = Integer.parseInt(stFile.nextToken());
                    	trans[itemID] = true;
                        reviewNumber[itemID][GetWeekdayIndex(weekday)] = calculateSentiment(stReview.nextToken());
                        System.out.println("\tItem:" + itemID + " " + reviewNumber[itemID]);
                    }
                    
                    //check each candidate
                    for(int c=0; c<candidates.size(); c++)
                    {
                        match = false; //reset match to false
                        int[] totalSentiment = new int[7];
                        //tokenize the candidate so that we know what items need to be present for a match
                        st = new StringTokenizer(candidates.get(c), ", ");
                        //check each item in the itemset to see if it is present in the transaction
                        while(st.hasMoreTokens())
                        {
                        	int itemID = Integer.valueOf(st.nextToken());
                            match = trans[itemID];
                            for(int k=0; k<7; k++){
	                            totalSentiment[k] += reviewNumber[itemID][k];
	                        }
                            if(!match) //if it is not present in the transaction stop checking
                                break;
                        }
                        
                        if(match) {//if at this point it is a match, increase the count
                            count[c]++;
                        	for(int m=0; m<7; m++){
                        		review[c][m] += totalSentiment[m];
                        	}
                        }
                    }
                    
                }
                for(int i=0; i<candidates.size(); i++)
                {
                    //if the count% is larger than the minSup%, add to the candidate to the frequent candidates
                    if(count[i]>=minSup)
                    {
                        frequentCandidates.add(candidates.get(i));
                        frequentReviews.add(review[i]);
                        //put the frequent itemset into the output file
                        System.out.println("( "+ candidates.get(i) + ") " + review[i][0] + " " + review[i][1] + " " + review[i][2] + " " + review[i][3] + " " + review[i][4] + " " + review[i][5] + " " + review[i][6]);
                        if(n==minSup)
                        	file_out.write("( "+ candidates.get(i) + ") " + review[i][0] + " " + review[i][1] + " " + review[i][2] + " " + review[i][3] + " " + review[i][4] + " " + review[i][5] + " " + review[i][6] + "\n");
                    }
                }
                file_out.close();
        }
        //if error at all in this process, catch it and print the error messate
        catch(IOException e)
        {
            System.out.println(e);
        }
        //clear old candidates
        candidates.clear();
        //new candidates are the old frequent candidates
        candidates = new Vector<String>(frequentCandidates);
        frequentCandidates.clear();
        frequentReviews.clear();
    }
    
    public int GetWeekdayIndex(String input) {
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
    
    public int calculateSentiment(String input) {
    	int sum = 0;
    	StringTokenizer tok = new StringTokenizer(input, " ");
    	while(tok.hasMoreTokens()) {
    		sum += sentiments.get(tok.nextToken());
    	}
    	return sum;
    }
}