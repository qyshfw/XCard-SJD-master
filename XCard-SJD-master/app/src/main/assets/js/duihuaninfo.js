
var vm;
var id = "";

requirejs(['main'], function (main) {  
	
		require(['avalon','net'], function() {
	     	
	     	id = $.getUrlParam('id');
	     		 
	 			vm = avalon.define({
	    
		  		$id: "duihuaninfo",
		  		
		  		info:{
			  		
			  		content : ''
		  		},
		  		
		  		doDH: function()
		  		{
			  		$('#alertBG').removeClass("hidden")
		  		},
		  		
		  		alertClick:function(item)
		  		{
			  		if(item == 0)
			  		{
				  		$('#alertBG').addClass("hidden")
			  		}
			  		else
			  		{
				  		var item = vm.info;
				  		item.msg = "兑换商品"
				  		item.type = '2';
				  		sendMsgToAPP(item);
				  		
				  		$('#alertBG').addClass("hidden")
			  		}
			  		
		  		},
		  		
		  		
		  		});
		  		
		  		
		  		
	  		
		  		getinfo();

				
				function getinfo()
		  		 {
			  		 
		  		 	var url = BaseUrl+"jifen.getproduct&id="+id;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var arr = data.data.info;
		  		 		
		  		 		if(arr)
		  		 		{
			  		 		if(arr.length > 0)
			  		 		{
				  		 		var item = arr[0];
				  		 		
				  		 		if(!item.content)
				  		 		{
					  		 		item.content = '';
				  		 		}
				  		
				  		 		vm.info = item;				  		 					  		 		
				  		 		
			  		 		}

						}

					});
				}
	
		
	})
	
	
     
   
});

function doDH()
{
// 	sendMsgToAPP({'msg':'跳转签到规则','type':'0'});
//$('#alertBG').removeClass("hidden")
}

function alertClick(item)
{
/*
	if(item == 0)
	{
		$('#alertBG').addClass("hidden")
	}
	else
	{
				  		
	}
*/
}




