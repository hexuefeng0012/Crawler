var show={};

show.query=function(){
	show.queryFile();
}

$(function() {
	$(".module-list-view .item").hover(function() {
		$(this).addClass("item-hover");
	}, function() {
		$(this).removeClass("item-hover");
	})
});

show.queryFile=function()
{
	var data={};
	data.pageSize=10;
	var rand = Math.random();
	data.pageNo=1;
	data.rand = rand;	
	var myurl = basePath+"getResult";
	/**
	 * 前端分页
	 */
	$("#jg_ajax_page").whpage({
		url:myurl,
		data: data,
		showNum:5,
		type: 'java',
		pagesize: 10,
		cur: 'cur',
		action:'post',
		format: function(data,page,pagesize){
			var page = page || 1;			
			var data = data.jsonData;
			console.log(data);
			var t = {};
			var records = parseInt(data.length);
			pageCount = Math.ceil(data.length / pagesize);
			t['cur'] = page;
			t['pageCount'] = pageCount;
			t['records'] = records;
			if(data.length>0){
				$("#jg_ajax_page").removeClass("hide");
			}else{
				$("#jg_ajax_page").removeClass("hide").addClass("hide");
			}
			return t;	
		},
		insert: function(result,start,end){
			if((null==result)||(""==result)){
				return;
			}
			else{
				var data = result.jsonData;
				if(data!=[] && data!="" && data.length!=0){
					var FileHtml = "";
					var filenum="";
					for(var i=start;i<end;i++){
						var obj = data[i];
						FileHtml +='  <div class="item global-clearfix"><div style="width: 10%" class="col c1">'+obj.userId+'</div><div style="width: 20%;" class="col">'+obj.oriCmtUrl+'</div><div style="width: 10%;" class="col">'+obj.pubTime+'</div><div style="width: 40%;" class="col">'+obj.content+'</div></div>';
					}
				}
				$("#FileHtml").html(FileHtml);
			}
		}
	});
}

show.getcomment=function()
{
	var data={};
	data.pageSize=10;
	var rand = Math.random();
	data.pageNo=1;
	data.rand = rand;	
	var myurl = basePath+"getComment";
	/**
	 * 前端分页
	 */
	$("#jg_ajax_page").whpage({
		url:myurl,
		data: data,
		showNum:5,
		type: 'java',
		pagesize: 10,
		cur: 'cur',
		action:'post',
		format: function(data,page,pagesize){
			var page = page || 1;			
			var data = data.jsonData;
			console.log(data);
			var t = {};
			var records = parseInt(data.length);
			pageCount = Math.ceil(data.length / pagesize);
			t['cur'] = page;
			t['pageCount'] = pageCount;
			t['records'] = records;
			if(data.length>0){
				$("#jg_ajax_page").removeClass("hide");
			}else{
				$("#jg_ajax_page").removeClass("hide").addClass("hide");
			}
			return t;	
		},
		insert: function(result,start,end){
			if((null==result)||(""==result)){
				return;
			}
			else{
				var data = result.jsonData;
				if(data!=[] && data!="" && data.length!=0){
					var FileHtml = "";
					var filenum="";
					for(var i=start;i<end;i++){
						var obj = data[i];
						FileHtml +='  <div class="item global-clearfix"><div style="width: 10%" class="col c1">'+obj.uid+'</div><div style="width: 20%;" class="col">'+obj.id+'</div><div style="width: 10%;" class="col">'+obj.weiboId+'</div><div style="width: 40%;" class="col">'+obj.content+'</div></div>';
					}
				}
				$("#FileHtml").html(FileHtml);
			}
		}
	});
}

$('#CrawlerWeibo').on('click',function(){
	Prompt.add();
	Prompt.init({
		title : "请设置微博抓取的主题词",
		shade : true,
		opacity : 20,
		width : 550,
		height : 320,
		html : $('.choose_class').html(),
		ConfirmFun : a1,
        CancelFun : a2
		});
		function a1(){
			alert("0000000");
			show.doCrawle();
		}
		function a2(){
	}
});

show.doCrawler=function(){
	alert("111111");
	var myurl=basePath+"getCrawler";
	var indexurl=basePath+"show/show.jsp";
	var data={};
	var keyword=$("#prompt_body").contents().find("#word").val();
	var startP=$("#prompt_body").contents().find("#startP").val();
	var endP=$("#prompt_body").contents().find("#endP").val();
	var time=$("#prompt_body").contents().find("#time").val();
	alert("2222222");
	$.ajax({
	   type: "POST",
	   url:  myurl,	
	   async: true, 
	   dataType:"json",
	   data:{keyWord:keyword,start:startP,end:endP,time:time},
	}).done(function(result){
		MU.msgTips('error','请检查网络后刷新页面重试！');
	}).fail(function(){
		MU.msgTips('error','请检查网络后刷新页面重试！');
	});
} 