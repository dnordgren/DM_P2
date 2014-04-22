public class Apriori 
{
    public static void main(String[] args) 
    {
        AprioriCalculation ap = new AprioriCalculation(3000, 999, 3, "transactions.txt", "project_2_output.txt");
        ap.mainApriori();
    }
}
