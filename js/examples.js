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
