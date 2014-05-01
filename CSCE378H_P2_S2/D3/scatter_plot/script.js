var margin = {top: 20, right: 20, bottom: 40, left: 50};

var height = 500 - margin.top - margin.bottom;
var width = 960;
//thickness
var barWidth = 10;
			
var y = d3.scale.linear().range([height, 0]);
var x = d3.scale.linear().range([0, width]);

x.domain([0,6]);

var chart = d3.select(".chart")
        .attr("height", height + margin.top + margin.bottom);
//change? 
var allgroup = chart.append("g")   
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");
    
var tooltip = chart.append("text")
        .style("visibility", "hidden");   


var yAxis = d3.svg.axis()
    .scale(y)
    .orient("left");

var xAxis = d3.svg.axis()
    .orient("bottom")
    .scale(x)
    .ticks(7)
    .tickFormat(function (d, i) {
      return ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"][d];
    });

chart.append("g")
      .attr("class", "x axis")
      .attr("transform", "translate(50," + (460) + ")")
      .call(xAxis); 
d3.tsv("project_2_output.tsv", type, function(error, data) {

  // y.domain([d3.min(data, function(d) { return  Math.min(d.Monday, d.Tuesday, d.Wednesday, d.Thursday, d.Friday, d.Saturday, d.Sunday);}), 
  //           d3.max(data, function(d) { return  Math.max(d.Monday, d.Tuesday, d.Wednesday, d.Thursday, d.Friday, d.Saturday, d.Sunday);})]);

  y.domain([d3.min(data, function(d) { return  Math.min(d.sentiment);}), 
            d3.max(data, function(d) { return  Math.max(d.sentiment);})]);

  // console.log("Minimum" + d3.min(data, function(d) { return  Math.min(d.day);}));
  // console.log("MAximum" + d3.max(data, function(d) { return  Math.max(d.day);}));


// .append("text")
//     .attr("transform", "rotate(0)")
//     .attr("y", 6)
//     .attr("x", 10)
//     .attr("dy", ".71em")
//     .style("text-anchor", "end");



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

  
    // console.log(j);
    // for(var j=0; j < 7; j++)
    // {

      var point = allgroup.selectAll("g")
      .data(data)
      .enter().append("g");

      point.append("circle")
       .attr("cx", function(d, i){  return x(d.day); }  )
       //.attr("cy", function(d, i, j){ console.log(getValue(d, j) +" "+ j); return y(getValue(d, j)); }  )
       .attr("cy", function(d, i){  return y(d.sentiment); }  )
       .attr("visibility", function(d, i){  
        return (d.sentiment == 0) ? "hidden" : "" })

       .attr("r", 5)
       //.attr("class", function(d){ return (    (d.Itemset).indexOf(" " +item +",") > -1 || (d.Itemset).indexOf("(" + item + ",") > -1 || (d.Itemset).indexOf(" " + item + ")")) > -1 ? "queried" : "" })
       //.style("fill", function(d){ return (    (d.Itemset).indexOf(" " +item +",") > -1 || (d.Itemset).indexOf("(" + item + ",") > -1 || (d.Itemset).indexOf(" " + item + ")")) > -1 ? "red" : "steelblue" })       

       .on("mouseover", function(d, i){
        d3.select(this).style("fill", "orange");
        var tipx =  x(d.day); 
        var tipy =  y(d.sentiment); 
        tooltip.attr("y", tipy);      
        tooltip.attr("x", tipx);
        tooltip.attr("dx", 60);
        tooltip.attr("dy", 15);
        tooltip.style("visibility", "visible");
        tooltip.style("fill", "black");
        tooltip.text(d.Itemset);
        })

        .on("mouseout", function(){
            if(d3.select(this).attr("class") == "queried"){
                d3.select(this).style("fill", "red");
            } else {
                //console.log(d3.select(this).attr("class"));
                d3.select(this).style("fill", "steelblue");

            }
            tooltip.style("visibility", "hidden");});
   // }
});






// 	bar.append("rect")
//         .attr("y", function(d, i) { return (days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday) 
//                                                 < 0 ? y(0) : y( days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday); }   )   
//         //.attr("y", function(d) { return y(0); })

         
//         .attr("height", function(d, i) { return Math.abs( y(days[0]*d.Monday + days[1]*d.Tuesday + days[2]*d.Wednesday + days[3]*d.Thursday + days[4]*d.Friday + days[5]*d.Saturday+ days[6]*d.Sunday) - y(0) ); })
//         //.attr("height", function(d) { return  height - y(d.Monday + d.Tuesday); })
//         .attr("width", barWidth - 1)  
//         .attr("class", function(d){ return (    (d.Itemset).indexOf(" " +item +",") > -1 || (d.Itemset).indexOf("(" + item + ",") > -1 || (d.Itemset).indexOf(" " + item + ")")) > -1 ? "queried" : "" })     
//         .style("fill", function(d){ return (    (d.Itemset).indexOf(" " +item +",") > -1 || (d.Itemset).indexOf("(" + item + ",") > -1 || (d.Itemset).indexOf(" " + item + ")")) > -1 ? "red" : "steelblue" }) 


//         .on("mouseover", function(d, i){
//             d3.select(this).style("fill", "yellow");
//             var tipy = d3.select(this).attr("y");
//             var tipx = barWidth* i;
//             tooltip.attr("y", tipy); 		
//             tooltip.attr("x", tipx);
//             tooltip.attr("dx", 35);
//             tooltip.attr("dy", 35);
//             tooltip.style("visibility", "visible");
//             tooltip.style("fill", "black");
//             tooltip.text(d.Itemset);})
          
            
//         .on("mouseout", function(){
//             if(d3.select(this).attr("class") == "queried"){
//                 d3.select(this).style("fill", "red");
//             } else {
//                 //console.log(d3.select(this).attr("class"));
//                 d3.select(this).style("fill", "steelblue");

//             }
//             tooltip.style("visibility", "hidden");});

// });

function type(d) {
    d.day = +d.day;
    d.sentiment = +d.sentiment;
	return d;
}

  function Y0() {
    return yScale(0);
  }

// function getValue(d, i) {
//   switch(i) {
//     case(0) : return d.Sunday;
//     case(1) : return d.Monday;
//     case(2) : return d.Tuesday;
//     case(3) : return d.Wednesday;
//     case(4) : return d.Thursday;
//     case(5) : return d.Friday;
//     case(6) : return d.Saturday;
//   }

// }
