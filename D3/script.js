var margin = {top: 20, right: 20, bottom: 20, left: 20};

var height = 500 - margin.top - margin.bottom;
var width = 960;
//thickness
var barWidth = 20;
			
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
	

highest = (parseInt(document.getElementById('highest').value));


d3.tsv("data.tsv", type, function(error, data) {

    y.domain([d3.min(data, function(d) { return  d.Monday + d.Tuesday + d.Wednesday + d.Thursday + d.Friday + d.Saturday+ d.Sunday;}), 
              d3.max(data, function(d) { return  d.Monday + d.Tuesday + d.Wednesday + d.Thursday + d.Friday + d.Saturday+ d.Sunday; }     )]);

// chart.append("g")
//     .attr("class", "x axis")
//     .attr("transform", "translate(20," + (height + 20) + ")")
//     .call(xAxis)
// .append("text")
//     .attr("x", 850)
//     .attr("y", -10)
//     .attr("dy", ".71em")
//     .style("text-anchor", "middle")
//     .text("Total Sentiment");


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


	chart.attr("width", margin.left + barWidth * data.length);

	var bar = allgroup.selectAll("g")
			.data(data)
		.enter().append("g")
			.attr("transform", function(d, i) { return "translate(" + i * barWidth + ",0)"; });

console.log(function(d) { return (d.Monday+d.Tuesday+ d.Wednesday + d.Thursday + d.Friday + d.Saturday+ d.Sunday)});

if (function(d) { return (d.Monday+d.Tuesday+ d.Wednesday + d.Thursday + d.Friday + d.Saturday+ d.Sunday) > highest }   ){

	bar.append("rect")
         .attr("y", function(d, i) { return (d.Monday+d.Tuesday+ d.Wednesday + d.Thursday + d.Friday + d.Saturday+ d.Sunday) 
                                                    < 0 ? y(0) : y(d.Monday+d.Tuesday+ d.Wednesday + d.Thursday + d.Friday + d.Saturday+ d.Sunday); }   )   
        //.attr("y", function(d) { return y(0); })
        .attr("height", function(d, i) { return Math.abs( y(d.Monday+d.Tuesday+ d.Wednesday + d.Thursday + d.Friday + d.Saturday+ d.Sunday) - y(0) ); })
        //.attr("height", function(d) { return  height - y(d.Monday + d.Tuesday); })
        .attr("width", barWidth - 1)      
        .on("mouseover", function(d, i){
            d3.select(this).style("fill", "orange");
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
            d3.select(this).style("fill", "steelblue");
            tooltip.style("visibility", "hidden");});
    }

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
