Project 2, Stage 2
=================

_Sawyer Jager, Rees Klintworth, Derek Nordgren, Brad Steiner_

CSCE 378H: Data Modeling - For Dr. Yu - 5.1.2014

###How to use###

###Data Representation###
######Data Model######

######Strategy######


###Processing Time###
Our input file (generated in C) takes less than one second to generate.

Processing to find triple sets and generate the output file (done in Java) averaged approximately 21.3 seconds. This processing time is of the same order of magnitude as in Stage 1 because our algorithm has the same complexity (as we will discuss below). 

However, several new complexities introduced in Stage 2 have slightly increased the processing time:

* changed C input file format to make use of item sentiments - larger input file to read in (_see Apriori/transactions.txt_)
* in addition to reading in a line, now we must parse out and performs calculations required to total sentiments
* _sentiment.csv_ must be read into memory
* significant hashing had to be done to make use of _sentiment.csv_ in totaling consumer reviews

###Algorithm Complexity###
The algorithm complexity for Stage 2 remains unchanged from Stage 1: 

> The complexity was determined to be O(T*I3), where ‘I’ is the total number of items, and ‘T’ is the number of transactions. In the average case, the complexity will be much lower due to the Apriori algorithm filtering itemsets with less than three occurrences. Additionally, ‘I3’ represents the number of combinations of ‘I’ elements, depending on the iteration of the algorithm. If our algorithm does three passes, to find the minimum support of three, our algorithm will need to make (I choose 3) combinations in the worst case. In our given assignment, the I3 portion was represented by 1000 combinations of 1, 49229 combinations of 2, and 171 combinations of 3.

###Visualizations###

######Interactive Bar Chart######
The Interactive Bar Chart visualization, which can be found in the folder named as such, displays a bar for every itemset purchased on a given day. The y-axis on this visualization is the total sentiment for an itemset. 

By selecting checkboxes, the user can control which days of the week they view total sentiment for. For example, by selecting Monday, Tuesday, and Wednesday, each bar will represent the total sentiment for an itemset purchased on any or all of those three days. By selecting a single day, the user can view the itemsets purchased on that day. By selecting the entire week, the user can view the total sentiment for any given itemset.

Additionally, the user can query to see in which itemsets a specific item appears via the text field at the top of the page. For example, typing '50' will red-fill any itemset bars that contain the item 50. 

The tooltips display on mouseover of the bar displays the item triple that the itemset represents.

######Scatterplot######


###Visualization Observations###
Our Interactive Bar Chart visualization, we have observed that in the beginning of the week, consumers typically leave negative reviews for purchases they make early in the week - specifically, Monday, Tuesday, and Wednesday. The total sentiment for any itemset purchased on one of these days is negative.

However, later in the week, consumers typically leave positive reviews for purchases they make. The total sentiment for any itemset purchased on Thursday, Friday, Saturday, or Sunday is positive.

