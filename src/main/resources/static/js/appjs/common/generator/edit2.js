// 以下为官方示例
$().ready(function() {
	update();
});

function update() {
	$.ajax({
		cache : true,
//		type : "POST",
		url : "/common/generator/begin",
//		data : $('#signupForm').serialize(),// 你的formid
//		async : false,
//		error : function(request) {
//			parent.layer.alert("网络连接超时");
//		},
		parent.layer.msg("操作成功");
//		success : function(data) {
//			if (data.code == 0) {
//				parent.layer.msg(data.msg);
//
//			} else {
//				parent.layer.msg(data.msg);
//			}
//
//		}
	});

}

