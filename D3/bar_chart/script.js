var margin = {top: 20, right: 20, bottom: 40, left: 20};

var height = 500 - margin.top - margin.bottom;
var width = 1500;
//thickness
var barWidth = 10;
			
var y = d3.scale.linear().range([height, 0]);
var x = d3.scale.linear().range([0, width]);

var chart = d3.select(".chart")
        .attr("height", height + margin.top + margin.bottom);
//change? 
var allgroup = chart.append("g")   
        .attr("transform", "translate(" + margin.bottom + "," + margin.left + ")");
    
var tooltip = chart.append("text")
        .style("visibility", "hidden");   

var xAxis = d3.svg.axis()
    .orient("bottom")
    .scale(x);

var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");     
	
var days = [0,0,0,0,0,0,0];

var item = ((document.getElementById('highest').value));


 if (document.getElementById('monday').checked){
    days[0] = 1;
  }
   if (document.getElementById('tuesday').checked){
    days[1] = 1;
  }
   if (document.getElementById('wednesday').checked){
    days[2] = 1;
  }
   if (document.getElementById('thursday').checked){
    days[3] = 1;
  }
   if (document.getElementById('friday').checked){
    days[4] = 1;
  }
   if (document.getElementById('saturday').checked){
    days[5] = 1;
  }
   if (document.getElementById('sunday').checked){
    days[6] = 1;
  }



console.log(days[0]);

d3.tsv("project_2_output.tsv", type, function(error, data) {

    y.domain([d3.min(data, function(d) { return  days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday;}), 
              d3.max(data, function(d) { return  days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday; }     )]);

chart.append("g")
      .attr("class", "y axis")
      .attr("transform", "translate(20," + 20 + ")")
      .call(yAxis)    
.append("text")
    .attr("transform", "rotate(-90)")
    .attr("y", 6)
    .attr("dy", ".71em")
    .style("text-anchor", "end")
    .text("Total Sentiment");

chart.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(20," + (y(0) + 20) + ")")
      .call(xAxis)    
.append("text")
    .attr("transform", "rotate(0)")
    .attr("y", 6)
    .attr("x",barWidth * data.length)
    .attr("dy", ".71em")
    .style("text-anchor", "end")
    .text("Itemset");

	chart.attr("width", margin.left + barWidth * data.length);

	var bar = allgroup.selectAll("g")
			.data(data)
		.enter().append("g")
                  
			.attr("transform", function(d, i) { return "translate(" + i * barWidth + ",0)"; });

	bar.append("rect")
        .attr("y", function(d, i) { return (days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday) 
                                                < 0 ? y(0) : y( days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday); }   )   
        //.attr("y", function(d) { return y(0); })

         
        .attr("height", function(d, i) { return Math.abs( y(days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday) - y(0) ); })
        //.attr("height", function(d) { return  height - y(d.Monday + d.Tuesday); })
        .attr("width", barWidth - 1)  
        .attr("class", function(d){ return (    (d.Itemset).indexOf(" " +item +",") > -1 || (d.Itemset).indexOf("(" + item + ",") > -1 || (d.Itemset).indexOf(" " + item + ")")) > -1 ? "queried" : "" })     
        .style("fill", function(d){ return (    (d.Itemset).indexOf(" " +item +",") > -1 || (d.Itemset).indexOf("(" + item + ",") > -1 || (d.Itemset).indexOf(" " + item + ")")) > -1 ? "red" : "steelblue" }) 


        .on("mouseover", function(d, i){
            d3.select(this).style("fill", "yellow");
            var tipy = d3.select(this).attr("y");
            var tipx = barWidth* i;
            tooltip.attr("y", tipy); 		
            tooltip.attr("x", tipx);
            tooltip.attr("dx", 35);
            tooltip.attr("dy", 35);
            tooltip.style("visibility", "visible");
            tooltip.style("fill", "black");
            tooltip.text(d.Itemset);})
          
            
        .on("mouseout", function(){
            if(d3.select(this).attr("class") == "queried"){
                d3.select(this).style("fill", "red");
            } else {
                //console.log(d3.select(this).attr("class"));
                d3.select(this).style("fill", "steelblue");

            }
            tooltip.style("visibility", "hidden");});

});

function type(d) {
	d.Monday = +d.Monday;
    d.Tuesday = +d.Tuesday;
    d.Wednesday = +d.Wednesday;
    d.Thursday = +d.Thursday;
    d.Friday = +d.Friday;
    d.Saturday = +d.Saturday;
    d.Sunday = +d.Sunday;
	return d;
}

  function Y0() {
    return yScale(0);
  }
