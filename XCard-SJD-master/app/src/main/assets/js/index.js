var page = 1;
var pagesize = 20;
var end = false;

var uid = "";
var uname="";

requirejs(['main'], function (main) {  

		require(['avalon','net'], function() {
	     
	     console.log("index js loaded!!!!!!");
	     
	     uid = $.getUrlParam('uid');
	     uname = $.getUrlParam('uname');
	     
	 			var qdarr = [];
	 				  		 
	 			var rili = avalon.define({
	    
		  		$id: "rili",
		  		
		  		today : '',
		  		
		  		riliArr: [
			  		
		  		],
		  		
		  		});
		  		
		  		var goods = avalon.define({
	    
		  		$id: "goods",
		  		
		  		goodsArr: [
			  		
		  		],
		  		
		  		doDH: function(item)
		  		{
			  		item.msg = "兑换商品"
			  		item.type = '1';
			  		sendMsgToAPP(item);
		  		},
		  		
		  		});
		  		
		  		var banner = avalon.define({
	    
		  		$id: "banner",
		  		
		  		lxqdday : "0"
		  		
		  		});
		  		
		  		
		  		getQDDay();
		  		getTj();
		  		
		  		var date = new Date();
		  		rili.today = date.toLocaleDateString().replace(/\//gm, "-");
		  		
		  		date.setDate(1);
		  		var weekday=new Array(7);
		  		weekday[0]="星期日" ;
		  		weekday[1]="星期一";
		  		weekday[2]="星期二";
		  		weekday[3]="星期三";
		  		weekday[4]="星期四";
		  		weekday[5]="星期五";
		  		weekday[6]="星期六";
		  		
		  		var b = date.getDay();
		  		
		  		
		  		date.setMonth(date.getMonth());
		  		var lastDate = new Date(date - 3600000*24);

		  		
		  		var last = lastDate.getDate();
		  		

		  		date.setMonth(date.getMonth() + 1);
		  		var lastDate = new Date(date - 3600000*24);

		  		
		  		var now = lastDate.getDate();
		  		
		  		var nowb = lastDate.getDay();
		  		
		  		
		  		
		  		for(i=b-1;i>=0;i--)
		  		{
			  		var item = {};
			  		item.month = 0;
			  		item.day = last-i;
			  		item.enable = false;
			  		item.css = 'noenable';
			  		rili.riliArr.push(item);
		  		}
		  		
		  		for(i=1;i<=now;i++)
		  		{
			  		var item = {};
			  		item.month = 1;
			  		item.day = i;
			  		item.enable = true;
			  		item.css = 'normal';
			  		rili.riliArr.push(item);
		  		}
		  		
		  		var j=1;
		  		for(i=nowb+1;i<7;i++)
		  		{
			  		var item = {};
			  		item.month = 0;
			  		item.day = j;
			  		item.enable = false;
			  		item.css = 'noenable';
			  		rili.riliArr.push(item);
			  		
			  		j++;
		  		}
		  		
		  		
		  		
		  		
		  		function getTj()
		  		 {
		  		 	var url = BaseUrl+"jifen.getproductList&page="+page+"&perNumber=20";

		  		 	XHttpGet( url, function(data) 
		  		 	{
		  		 		var info = data.data.info;
		
		  		 		if(info)
		  		 		{
		  		 			$(info).each(function(index,item)
		  		 			{
		  		 				item.css = index % 2 == 0 ? 'item_left' : 'item_right';
							});
			
			
							goods.goodsArr = goods.goodsArr.concat(data.data.info);

							if(!end)
							{
								page = page + 1;
							}
							

							if(info.length<page.pagesize)
							{
								end = true;
							}
							else
							{
								end = false;
							}
						}

					});
				}
				
				
				function getQDDay()
		  		 {
			  		 qdarr = [];
			  		 var year = (new Date()).getFullYear();
			  		 var month = (new Date()).getMonth()+1;
			  		 
		  		 	var url = BaseUrl+"Jifen.getQiandao&uid="+uid+"&username="+uname+"&year="+year+"&month="+month;

		  		 	XHttpGet( url, function(data) 
		  		 	{
			  		 	banner.lxqdday = data.data.lxqdday;
			  		 	
		  		 		var info = data.data.info;
		  		 		
		  		 		if(info)
		  		 		{
		  		 			$(info).each(function(index,item)
		  		 			{
			  		 			var time = new Date(item.create_time * 1000);
			  		 			item.day = time.getDate();
			  		 			
			  		 			qdarr.push(item);
			  		 			
			  		 			
			  		 			$(rili.riliArr).each(function(index,item1)
		  		 			{
			  		 			if(item1.month == 1 && item.day == item1.day)
			  		 			{
				  		 			item1.css = "selected";
			  		 			}

							});

							});
							
			
						}

					});
				}

		
	})
	
	
     
   
});

function toQDGZ()
{
	sendMsgToAPP({'msg':'跳转签到规则','type':'0'});
}

function toGoodsCenter()
{
	sendMsgToAPP({'msg':'跳转怀府币商城','type':'3'});
}



