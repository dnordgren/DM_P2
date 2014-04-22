import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.*;

/******************************************************************************
* Class Name   : AprioriCalculation
* Purpose      : generate Apriori itemsets
*****************************************************************************/
class AprioriCalculation
{
    //?current basket
    Vector<String> candidates=new Vector<String>(); //the current candidates 
    String configFile="config.txt"; //configuration file
    String transaFile="transa.txt"; //transaction file
    String outputFile="apriori-output.txt";//output file
    //int numItems; //number of items per transaction
    int maxItemID;
    int numTransactions; //number of transactions
    double minSup; //minimum support for a frequent itemset
    //?what is this?
    String oneVal[]; //array of value per column that will be treated as a '1'
    String itemSep = " "; //the separator value for items in the database

    Vector<String> frequentCandidates = new Vector<String>(); //the frequent candidates for the current itemset
    FileInputStream file_in; //file input stream
    BufferedReader data_in; //data input stream
    FileWriter fw;
    BufferedWriter file_out;
    
    /************************************************************************
     * Method Name  : aprioriProcess
     * Purpose      : Generate the apriori itemsets
     * Parameters   : None
     * Return       : None
     *************************************************************************/
    public void aprioriProcess()
    {
        //?need?
        Date d; //date object for timing purposes
        long start, end; //start and end time
        int itemsetNumber=0; //the current itemset being looked at
        
        getConfig();

        System.out.println("Apriori algorithm has started.\n");

        //start timer
        d = new Date();
        start = d.getTime();

        //while not complete
        do
        {
            System.out.println(itemsetNumber);
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

    //? readline method
    /************************************************************************
     * Method Name  : getInput
     * Purpose      : get user input from System.in
     * Parameters   : None
     * Return       : String value of the users input
     *************************************************************************/
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

    /************************************************************************
     * Method Name  : getConfig
     * Purpose      : get the configuration information (config filename, transaction filename)
     *              : configFile and transaFile will be change appropriately
     * Parameters   : None
     * Return       : None
     *************************************************************************/
    private void getConfig()
    {
        FileWriter fw;
        BufferedWriter file_out;
        /*
        String input="";
        //ask if want to change the config
        //? console output for settings -remove-
        System.out.println("Default Configuration: ");
        System.out.println("\tRegular transaction file with '" + itemSep + "' item separator.");
        System.out.println("\tConfig File: " + configFile);
        System.out.println("\tTransa File: " + transaFile);
        System.out.println("\tOutput File: " + outputFile);
        System.out.println("\nPress 'C' to change the item separator, configuration file and transaction files");
        System.out.print("or any other key to continue.  ");
        input=getInput();

        //?change config files -remove-
        if(input.compareToIgnoreCase("c")==0)
        {
            System.out.print("Enter new transaction filename (return for '"+transaFile+"'): ");
            input=getInput();
            if(input.compareToIgnoreCase("")!=0)
                transaFile=input;

            System.out.print("Enter new configuration filename (return for '"+configFile+"'): ");
            input=getInput();
            if(input.compareToIgnoreCase("")!=0)
                configFile=input;

            System.out.print("Enter new output filename (return for '"+outputFile+"'): ");
            input=getInput();
            if(input.compareToIgnoreCase("")!=0)
                outputFile=input;

            System.out.println("Filenames changed");

            System.out.print("Enter the separating character(s) for items (return for '"+itemSep+"'): ");
            input=getInput();
            if(input.compareToIgnoreCase("")!=0)
                itemSep=input;


        }*/

        //? read config files
        try
        {
             FileInputStream file_in = new FileInputStream(configFile);
             BufferedReader data_in = new BufferedReader(new InputStreamReader(file_in));
             //number of items
             maxItemID=Integer.valueOf(data_in.readLine()).intValue();

             //number of transactions
             numTransactions=Integer.valueOf(data_in.readLine()).intValue();

             //minsup
             minSup=(Double.valueOf(data_in.readLine()).doubleValue());

             //output config info to the user
             System.out.print("\nInput configuration: "+maxItemID+" items, "+numTransactions+" transactions, ");
             System.out.println("minsup = "+minSup);
             System.out.println();
             //minSup/=100.0;
            
            /*
            oneVal = new String[maxItemID];
            
            //? change the column labels to indicate what value a one actually represents
            System.out.print("Enter 'y' to change the value each row recognizes as a '1':");
            if(getInput().compareToIgnoreCase("y")==0)
            {
                for(int i=0; i<oneVal.length; i++)
                {
                    System.out.print("Enter value for column #" + (i+1) + ": ");
                    oneVal[i] = getInput();
                }
            }
            
            //?otherwise just let 1 for every column is good enough
            else
                for(int i=0; i<oneVal.length; i++)
                    oneVal[i]="1";
            */
        }
        //if there is an error, print the message
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    /************************************************************************
     * Method Name  : generateCandidates
     * Purpose      : Generate all possible candidates for the n-th itemsets
     *              : these candidates are stored in the candidates class vector
     * Parameters   : n - integer value representing the current itemsets to be created
     * Return       : None
     *************************************************************************/
    private void generateCandidates(int n)
    {
        Vector<String> tempCandidates = new Vector<String>(); //temporary candidate string vector
        String str1, str2; //strings that will be used for comparisons
        StringTokenizer st1, st2; //string tokenizers for the two itemsets being compared

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
		        		List<String> items = Arrays.asList(data_in.readLine().split(itemSep));
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
		        		List<String> items = Arrays.asList(data_in.readLine().split(itemSep));
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

    /************************************************************************
     * Method Name  : calculateFrequentItemsets
     * Purpose      : Determine which candidates are frequent in the n-th itemsets
     *              : from all possible candidates
     * Parameters   : n - iteger representing the current itemsets being evaluated
     * Return       : None
     *************************************************************************/
    private void calculateFrequentItemsets(int n)
    {

        StringTokenizer st, stFile; //tokenizer for candidate and transaction
        boolean match; //whether the transaction has all the items in an itemset
         //array to hold a transaction so that can be checked
        int count[] = new int[candidates.size()]; //the number of successful matches

        try
        {
                //output file
                fw= new FileWriter(outputFile, false);
                file_out = new BufferedWriter(fw);
                //load the transaction file
                file_in = new FileInputStream(transaFile);
                data_in = new BufferedReader(new InputStreamReader(file_in));
                
                //for each transaction
                for(int i=0; i<numTransactions; i++)
                {
                	System.out.println("transaction: " + i + "candidate size: " + candidates.size());
                    //System.out.println("Got here " + i + " times"); //useful to debug files that you are unsure of the number of line
                    stFile = new StringTokenizer(data_in.readLine(), itemSep); //read a line from the file to the tokenizer
                    //put the contents of that line into the transaction array
                    /*for(int j=0; j<numItems; j++)
                    {
                        trans[j]=(stFile.nextToken().compareToIgnoreCase(oneVal[j])==0); //if it is not a 0, assign the value to true
                    }*/
                    boolean trans[] = new boolean[maxItemID+1];
                    
                    while(stFile.hasMoreTokens())
                    {
                        trans[Integer.parseInt(stFile.nextToken())] = true;
                    }
                    
                    //check each candidate
                    for(int c=0; c<candidates.size(); c++)
                    {
                        match = false; //reset match to false
                        //tokenize the candidate so that we know what items need to be present for a match
                        st = new StringTokenizer(candidates.get(c), ", ");
                        //check each item in the itemset to see if it is present in the transaction
                        while(st.hasMoreTokens())
                        {
                            match = (trans[Integer.valueOf(st.nextToken())]);
                            if(!match) //if it is not present in the transaction stop checking
                                break;
                        }
                        if(match) //if at this point it is a match, increase the count
                            count[c]++;
                    }
                    
                }
                for(int i=0; i<candidates.size(); i++)
                {
                    //  System.out.println("Candidate: " + candidates.get(c) + " with count: " + count + " % is: " + (count/(double)numItems));
                    //if the count% is larger than the minSup%, add to the candidate to the frequent candidates
                    if(count[i]>=minSup)
                    {
                        frequentCandidates.add(candidates.get(i));
                        //put the frequent itemset into the output file
                        if(n==minSup)
                        	file_out.write("("+ candidates.get(i) + ") " + count[i] + "\n");
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
    }
}