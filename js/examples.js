$(function() {
	var svg = d3.select('#a-simple-visualization').append('svg').attr({width:"400",height:"400"});
	$('#a-simple-visualization-go').bind('click',function() {
		var points     = [];
		for(var i=0;i<10;i++)
			points.push({x:50+300*Math.random(),y:50+300*Math.random(),r:50*Math.random()});
		var x = svg.selectAll("circle").data(points)
			.attr({cx:function(d) { return d.x;},cy:function(d) { return d.y},r:function(d) { return d.r; }});		
		x.enter().append("circle").attr({cx:function(d) { return d.x;},cy:function(d) { return d.y},r:function(d) { return d.r; }});
	});
});


$(function() {
	var words = [
		{word:"the",count:240},
		{word:"but",count:120},
		{word:"together",count:50},
		{word:"observer",count:72},
		{word:"word",count:86},
		{word:"group",count:97},
		{word:"gaggle",count:138}
	];
	var svg    = d3.select('#example-2').append('svg').attr({width:"400",height:"400"})
		.append("g").attr({transform:"translate(200,200)"});
	var color = d3.scale.ordinal()
    	.range(["#98abc5", "#8a89a6", "#7b6888", "#6b486b", "#a05d56", "#d0743c", "#ff8c00"]);
	var pie   = d3.layout.pie()
		.sort(null).value(function(d) { return d.count; });
	var arc = d3.svg.arc()
		.outerRadius(190)
		.innerRadius(130);
			      		
	function draw() {
		console.log(JSON.stringify(pie(words)));
		var arcs = svg.selectAll("g.arc").data(pie(words),function(d) { return d.data.word; });
		arcs.exit().remove();
		
		//Update existing elements
		arcs.select("path").attr("d",arc);
		arcs.select("text").attr("transform",function(d) { return "translate("+arc.centroid(d)+")"; });
		
		//Create new elements
		var newArcs = arcs.enter().append("g").attr("class","arc");
		
		newArcs.append("path")
			.style("fill",function(d) { return color(d.data.word); })
			.attr("d",arc);
		newArcs.append("text")
			.attr("transform",function(d) { return "translate("+arc.centroid(d)+")"; })
			.attr({dy:".35em"}).style({"text-anchor":"middle","font-size":"40%","fill":"#fff"})
			.text(function(d) { return d.data.word; });
		
	}
	$('#example-2-go').bind('click',function() {
		for(var i in words)
			words[i].count = Math.floor(1000*Math.random());
		draw();		
	});
	
	draw();
	
})
