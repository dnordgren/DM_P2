public class Apriori 
{
    public static void main(String[] args) 
    {
        AprioriCalculator ap = new AprioriCalculator(3000, 999, 3, "transactions.txt", "sentiment.csv", "apriori_output.txt");
    	ap.mainApriori();
    }
}
