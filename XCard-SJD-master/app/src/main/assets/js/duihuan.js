
var vm;
var uid = "";
var uname="";
var cid = "";
var sname = "";
requirejs(['main'], function (main) {  
	
		require(['avalon','net'], function() {
	     		 
	     		
	     		 uid = $.getUrlParam('uid');
		 		 uname = $.getUrlParam('uname'); 
		 		 cid = $.getUrlParam('cid');
	     		 sname = $.getUrlParam('sname');

	 			vm = avalon.define({
	    
		  		$id: "duihuan",
		  		
		  		input: "",
		  		
		  		info:{
			  		
			  		content : ''
		  		},
		  		
		  		doJifenDuihuan: function()
		  		{
			  		doDH();
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
				  		item.type = '1';
				  		sendMsgToAPP(item);
				  		
				  		$('#alertBG').addClass("hidden")
			  		}
			  		
		  		},
		  		
		  		
		  		});
		  		
		  		
		  		
	  		
		  		getinfo();

				
				function getinfo()
		  		 {
			  		 
		  		 	var url = BaseUrl+"News.getArticle&id=6889";

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var arr = data.data.info;
		  		 		
		  		 		if(arr)
		  		 		{
			  		 		if(arr.length > 0)
			  		 		{
				  		 		var item = arr[0];
				  		 		item.create_time = $.myTime.UnixToDate(item.create_time);
				  		 		
				  		 		if(!item.content)
				  		 		{
					  		 		item.content = '';
				  		 		}
				  		
				  		 		vm.info = item;				  		 					  		 		
				  		 		
			  		 		}

						}

					});
				}
	
	
				function doDH()
		  		 {
			  		 if(vm.input.trim().length == 0)
			  		 {
				  		 return;
			  		 }
			  		 
			  		 sendMsgToAPP({'msg':'积分兑换','type':'4'});
			  		 
		  		 	var url = BaseUrl+"Jifen.addDHhfb&uid="+uid+"&username="+uname+"&mcardid="+cid+"&jifen="+vm.input;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  			var info = data.data.info;
		  		 		var code = data.data.code;
		  		 		if(code != null)
		  		 		{
			  		 		if(code == 0)
			  		 		{
				  		 		info.create_time = $.myTime.UnixToDateFormat(info.create_time,"yyyy-MM-dd HH:mm:ss");
				  		 		info.sname = sname;
			  		 			sendMsgToAPP({'msg':'积分兑换成功','type':'5','info':info});
			  		 			return;
							}	
		  		 		}
		  		 		
		  		 		var msg = data.data.msg;
		  		 		
		  		 		if(!msg)
		  		 		{
			  		 		msg = "积分兑换失败";
		  		 		}
		  		 		
		  		 		sendMsgToAPP({'msg':'积分兑换失败','type':'6','info':msg});
		  		 		
					});
				}

		
			})
	
	
     
   
});

function doJifenDuihuan()
{
// 	sendMsgToAPP({'msg':'跳转签到规则','type':'0'});
//$('#alertBG').removeClass("hidden")
}





