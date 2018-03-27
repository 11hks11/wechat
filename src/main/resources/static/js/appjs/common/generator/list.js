var prefix = "/common/generator"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
						showRefresh : true,
						showToggle : true,
						showColumns : true,
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : true, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						search : true, // 是否显示搜索框
						showColumns : true, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "client", // 设置在哪里进行分页，可选值为"client" 或者
						// "server"
						queryParams : function (params) {
							return {
					              typeId: document.getElementById("status")[document.getElementById("status").selectedIndex].value // 额外添加的参数
					          }
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : [
//								{
//									radio : true
//								},
								{
									field:  'id',
									title : '问卷id',
									visible : false,
									switchable:false
								},
								{
									field : 'name', // 列字段名
									title : '问卷名称' // 列标题
								},
								{
									field : 'type',
									title : '问卷类型'
								},
								{
									field : 'quantity',
									align : 'center',
									title : '回收问卷数'
								},
								{
									field : 'begindate',
									align : 'center',
									title : '起始调研时间'
								},
								{
									field : 'enddate',
									align : 'center',
									title : '结束调研时间'
								},
								{
									field : 'flag',
									title : '状态',
									align : 'center',
									formatter : function(value, row, index) {
										if (row.flag == 1) {
											return '<span class="label label-default">未开始</span>';
										} else if (row.flag == 2) {
											return '<span class="label label-primary">进行中</span>';
										}else{
											return '<span class="label label-danger">已结束</span>';
										}
									}
								},
								{
									title : '操作',
									field : 'ids',
									align : 'center',
									formatter : function(value, row, index) {
										var a = '<a class="btn btn-primary btn-sm " onclick="dataOut(\''
										    + row.id+"," + row.quantity
											+ '\')"><i class=""></i>导出数据源</a> ';
										var d = '<a class="btn  btn-info btn-sm disabled" role="button" style="visibility: hidden" ><i class=""></i>开始调研</a> ';
										var e = '<a class="btn  btn-info btn-sm disabled" role="button" style="visibility: hidden"><i class=""></i>导出数据源</a> ';
										if(row.flag == 1){
//											var a = '<a class="btn btn-default btn-sm disabled" role="button" onclick="dataOut(\''
//											    + row.id
//												+ '\')"><i class=""></i>生成报告</a> ';
											var b = '<a class="btn btn-info btn-sm" onclick="begin(\''
												+ row.id
												+ '\')"><i class=""></i>开始调研</a> ';
											return b+d+e;
										}else if(row.flag == 2){
											var c = '<a class="btn btn-success btn-sm " onclick="end(\''
												+ row.id
												+ '\')"><i class=""></i>结束调研</a> ';
											return d+c+a;
										}else{
//											var a = '<a class="btn btn-primary btn-sm " onclick="dataOut(\''
//											    + row.id
//												+ '\')"><i class=""></i>生成报告</a> ';
											return d+d+a;
										}
										
									}
								} ]
					});
}

function reLoad() {
	load();
}

function change() {
	var  myselect=document.getElementById("status")
	var index=myselect.selectedIndex ;
    var grade =myselect.options[index].value;
	$('#exampleTable').bootstrapTable('refresh');
}
function begin(id) {
	
	
	layer.confirm('确定开始调研？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/begin",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					$('#exampleTable').bootstrapTable('refresh');
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})
	
	
}

function end(id) {
	
	layer.confirm('确定结束调研？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix + "/end",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					$('#exampleTable').bootstrapTable('refresh');
				} else {
					layer.msg(r.msg);
				}
			}
		});
	})

//	$('#exampleTable').bootstrapTable('refresh');
}


function dataOut(id) {
	
	var quantity=id.split(",")[1];
	var id=id.split(",")[0];
	
//	alert(id);
//	alert(quantity);
	
	if(quantity == undefined ||quantity=="undefined"){
		layer.msg("暂无回收问卷，无法导出报告");
	}else{
		layer.confirm('确定导出数据源？', {
			btn : [ '确定', '取消' ]
		}, function() {
			
			location.href = prefix + "/dataOut?id="+id ;
			layer.msg('稍等，正在导出，请勿重复点击', {
				time: 15000, //15s后自动关闭
				btn: ['明白了']
			});
		})
		
		
//		layer.msg('导出中', {
//			  icon: 16,
//			  shade: 0.1
//			});
//		layer.load(2,{offset: ['40%','50%'],shade: [0.1,'#3595CC'],area: ['10%','10%']});
	}



}

