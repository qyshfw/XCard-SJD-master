
var vm;

var hfb = "";
var time = "";
var jifen = "";
var sname = "";

requirejs(['main'], function (main) {  
	
		require(['avalon','net'], function() {
	     		
	     		hfb = $.getUrlParam('hfb');
	     		time = $.getUrlParam('time');
	     		jifen = $.getUrlParam('jifen');
	     		sname = $.getUrlParam('sname');
	     		 
	 			vm = avalon.define({
	    
		  		$id: "success",
		  		
		  		hfb : "",
		  		time : "",
		  		jifen : "",
		  		sname : "",
		  		
		  		});
		  		
		  		vm.hfb = hfb;
		  		vm.time = time;
		  		vm.jifen = jifen;
		  		vm.sname = sname;
		  		
		  		
  				
	});
	
   
});





