<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">
	<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>


<script type="text/javascript">

	function queryActivityAll(pageNo,pageSize){
		//获取参数
		var name = $("#queryName").val()
		var owner = $("#queryOwner").val()
		var startDate= $("#startDate").val()
		var endDate= $("#endDate").val()
		var pageNo = pageNo
		var pageSize = pageSize
		//发起ajax
		$.ajax({
			url:"workbench/activity/queryActivityAll.do",
			data:{
				name:name,
				owner:owner,
				startDate:startDate,
				endDate:endDate,
				pageNo:pageNo,
				pageSize:pageSize
			},
			dataType:"json",
			type:"get",
			success:function(data){
				//总计数
				// $("#totalRowsB").text(data.totalRows)
				//渲染页面
				var htmlStr="";
				$.each(data.activities,function(index,obj){
					htmlStr+="<tr class=\"active\" >"
					htmlStr+="	<td><input type=\"checkbox\" value=\""+obj.id+"\"></td>"
					var ids="workbench/activity/toActivityDetail.do?id="+obj.id
					htmlStr+="	<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='"+ids+"';\">"+obj.name+"</a></td>"
					htmlStr+="	<td>"+obj.owner+"</td>"
					htmlStr+="	<td>"+obj.startDate+"</td>"
					htmlStr+="	<td>"+obj.endDate+"</td>"
					htmlStr+="</tr>"
				})
				$("#TBody").html(htmlStr)

				$("#checkedBtn").prop("checked",false)

				var totalPages1=1;
				if(data.totalRows%pageSize==0){
					totalPages1 = data.totalRows/pageSize
				}else {
					totalPages1 =parseInt(data.totalRows/pageSize)+1;
					// alert(totalPages1)
				}

				$("#demo_pag1").bs_pagination({
					currentPage: pageNo,
					rowsPerPage: pageSize,
					totalPages: totalPages1,
					totalRows: data.totalRows,
					visiblePageLinks: 5,
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					onChangePage:function(event,pageObj){
						// alert("1111")
						queryActivityAll(pageObj.currentPage,pageObj.rowsPerPage)
					}
				})
			}
		})

	}

	$(function(){

		//导入市场活动
		$("#importActivityBtn").click(function(){
			var activityFile= $("#activityFile").val()
			var fileName=activityFile.substr(activityFile.lastIndexOf(".")+1).toLocaleLowerCase()
			if(fileName != "xls"){
				alert("请上传后缀名为xls的文件")
				return
			}
			var activityFile1= $("#activityFile")[0].files[0]
			if(activityFile1.szie > 1024*1024*5){
				alert("文件大小必须小于5MB")
				return
			}
			var formData= new FormData()
			formData.append("file",activityFile1);
			$.ajax({
				url:"workbench/activity/importActivityList.do",
				data:formData,
				processData:false,
				contentType:false,
				dataType:"json",
				type:"post",
				success:function(data){
					if(data.code == "1"){
						$("#importActivityModal").modal("hide")
						alert("成功上传"+data.obj+"记录")
						queryActivityAll(1,$("#demo_pag1").bs_pagination("getOption",'rowsPerPage'));

					}else {
						alert(data.msg)
					}
				}
			})
		})

		//选择下载列表数据
		$("#exportActivityXzBtn").click(function(){
			var select1= $("#TBody input[type=checkbox]:checked")
			if(select1.size() == 0){
				alert("请选择要导出的市场活动信息")
				return;
			}
			var str="";
			$.each(select1,function(){
				str+="id="+this.value+"&"
			})
			var ids= str.substr(0,str.length-1)
			window.location.href="workbench/activity/selectFileDownload.do?"+ids;
		})

		//批量下载列表数据
		$("#exportActivityAllBtn").click(function(){
			window.location.href="workbench/activity/FileDownload.do";
		})

		//保存修改市场活动信息
		$("#editActivityBtn1").click(function(){

			var id=$("#hiddenId").val()
			var owner=$("#edit-marketActivityOwner").val()
			var name=$("#edit-marketActivityName").val()
			var startDate=$("#edit-startDate").val()
			var endDate=$("#edit-endDate").val()
			var cost=$("#edit-cost").val()
			var description=$("#edit-description").val()

			if(owner == ""){
				alert("所有者不能为空")
				return
			}
			if(name == ""){
				alert("名字不能为空")
				return;
			}
			if(startDate != "" && endDate != ""){
				if(startDate > endDate){
					alert("开始时间必须小于结束时间")
					return;
				}
			}
			//正则表达式
			var regEx=/^(([1-9]\d*)|0)/;
			if(!regEx.test(cost)){
				alert("成本必须为正整数")
				return;
			}
			/**
			 * var id=$("#hiddenId").val()
			 var owner=$("#edit-marketActivityOwner").val()
			 var name=$("#edit-marketActivityName").val()
			 var startDate=$("#edit-startDate").val()
			 var endDate=$("#edit-endDate").val()
			 var cost=$("#edit-cost").val()
			 var description=$("#edit-description").val()
			 var createTime=$("#hiddenCreateTime").val()
			 var createBy=$("#hiddenCreateBy").val()
			 */
			$.ajax({
				url:"workbench/activity/updateActicityById.do",
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					description:description,
					cost:cost,
				},
				dataType:"json",
				type:"post",
				success:function(data){
					if(data == '0'){
						alert(data.msg)
						return;
					}else {
						$("#editActivityModal").modal("hide")
						queryActivityAll($("#demo_pag1").bs_pagination("getOption",'currentPage'),$("#demo_pag1").bs_pagination("getOption",'rowsPerPage'));
					}
				}
			})
		})
		//点击修改市场活动按钮弹出模态窗口
		$("#editBtn").click(function(){
			//获取选择的id
			var editActivity= $("#TBody input[type=checkbox]:checked")
			if(editActivity.size() == 0){
				alert("请选择要修改的市场活动");
				return;
			}
			if(editActivity.size() > 1){
				alert("每次修改只能选择一条市场活动");
				return;
			}
			//获取id发起ajax请求
			var id= editActivity[0].value
			$.ajax({
				url:"workbench/activity/queryActivityById.do",
				data:{
					id:id
				},
				dataType:"json",
				type:"get",
				success:function(data){
					//响应数据
					$("#hiddenId").val(data.id)
					$("#edit-marketActivityOwner").val(data.owner)
					$("#edit-marketActivityName").val(data.name)
					$("#edit-startDate").val(data.startDate)
					$("#edit-endDate").val(data.endDate)
					$("#edit-cost").val(data.cost)
					$("#edit-description").val(data.description)
					$("#editActivityModal").modal("show")
				}
			})
		})

		$("#deletBtn").click(function(){
			//获取所以被选中的参数
			var deleteIds= $("#TBody input[type='checkbox']:checked")
			if(deleteIds.size() == 0){
				alert("请选择要删除的市场活动信息")
				return;
			}
			if(window.confirm("确定删除吗")){

				var str = '';
				$.each(deleteIds,function(){
					str += "id="+deleteIds.val()+"&"
				})
				var id= str.substr(0,str.length-1)

				$.ajax({
					url:"workbench/activity/deleteActivityByIds.do",
					data:id,
					dataType:"json",
					type:"post",
					success:function(data){
						if(data.code == '0'){
							alert(date.msg)
							return;
						}else {
							queryActivityAll($("#demo_pag1").bs_pagination("getOption",'currentPage'),$("#demo_pag1").bs_pagination("getOption",'rowsPerPage'));
							// queryActivityAll(1,$("#demo_pag1").bs_pagination("getOption",'rowsPerPage'));
						}
					}
				})
			}
		})

		$("#checkedBtn").click(function(){
			$("#TBody input[type='checkbox']").prop("checked",this.checked)
		})

		$("#TBody").on("click","input[type='checkbox']",function(){
			if($("#TBody input[type='checkbox']").size() == $("#TBody input[type='checkbox']:checked").size()){
				$("#checkedBtn").prop("checked",true)
			}else {
				$("#checkedBtn").prop("checked",false)
			}
		})

		queryActivityAll(1,10);

		$("#queryBtn").click(function(){
			queryActivityAll(1,$("#demo_pag1").bs_pagination("getOption",'rowsPerPage'));
		})

		$(".myDate").datetimepicker({
			language:'zh-CN',
			format:'yyyy-mm-dd',
			minView:'month',
			initialDate:new Date(),
			autoclose:true,
			todayBtn:true,
			clearBtn:true
		})

		$("#createActivityBtn").click(function(){
			$("#creatleAtcivityForm")[0].reset()
			$("#createActivityModal").modal("show")
		})
		$("#saveCreateActivityBtn").click(function(){
			var owner=$("#create-marketActivityOwner").val()
			var name=$("#create-marketActivityName").val()
			var startDate=$("#create-startTime").val()
			var endDate=$("#create-endTime").val()
			var description= $("#create-describe").val()
			var cost= $("#create-cost").val()

			if(owner == ""){
				alert("所有者不能为空")
				return
			}
			if(name == ""){
				alert("名字不能为空")
				return;
			}
			if(startDate != "" && endDate != ""){
				if(startDate > endDate){
					alert("开始时间必须小于结束时间")
					return;
				}
			}
			//正则表达式
			var regEx=/^(([1-9]\d*)|0)/;
			if(!regEx.test(cost)){
				alert("成本必须为正整数")
				return;
			}
			$.ajax({
				url:"workbench/activity/saveActivity.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					description:description,
					cost:cost
				},
				type:"post",
				dataType:"json",
				success:function(data){
					if(data.code == "1"){
						$("#createActivityModal").modal("hide")
						//刷新数据
						queryActivityAll(1,$("#demo_pag1").bs_pagination("getOption",'rowsPerPage'));
						return;
					}else {
						$("#createActivityModal").modal("show")
						return
					}
				}
			})

		})

	});

</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<form id="creatleAtcivityForm" class="form-horizontal" role="form">

						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
<%--								  <option>zhangsan</option>--%>
<%--								  <option>lisi</option>--%>
<%--								  <option>wangwu</option>--%>
									<c:forEach items="${users}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveCreateActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form">
						<input type="hidden" id="hiddenId">
						<input type="hidden" id="hiddenCreateTime">
						<input type="hidden" id="hiddenCreateBy">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${users}" var="u">
										<option value="${u.id}">${u.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-startDate" value="2020-10-10" readonly>
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-endDate" value="2020-10-20" readonly>
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="editActivityBtn1">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>


	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">

			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="queryName">
				    </div>
				  </div>

				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="queryOwner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control myDate" type="text" id="startDate" readonly />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control myDate" type="text" id="endDate" readonly>
				    </div>
				  </div>

				  <button type="button" id="queryBtn" class="btn btn-default">查询</button>

				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deletBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="checkedBtn" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="TBody">
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
					</tbody>
				</table>
				<div id="demo_pag1"></div>
			</div>

<%--			<div style="height: 50px; position: relative;top: 30px;">--%>
<%--				<div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRowsB">50</b>条记录</button>--%>
<%--				</div>--%>
<%--				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>--%>
<%--					<div class="btn-group">--%>
<%--						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">--%>
<%--							10--%>
<%--							<span class="caret"></span>--%>
<%--						</button>--%>
<%--						<ul class="dropdown-menu" role="menu">--%>
<%--							<li><a href="#">20</a></li>--%>
<%--							<li><a href="#">30</a></li>--%>
<%--						</ul>--%>
<%--					</div>--%>
<%--					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>--%>
<%--				</div>--%>
<%--				<div style="position: relative;top: -88px; left: 285px;">--%>
<%--					<nav>--%>
<%--						<ul class="pagination">--%>
<%--							<li class="disabled"><a href="#">首页</a></li>--%>
<%--							<li class="disabled"><a href="#">上一页</a></li>--%>
<%--							<li class="active"><a href="#">1</a></li>--%>
<%--							<li><a href="#">2</a></li>--%>
<%--							<li><a href="#">3</a></li>--%>
<%--							<li><a href="#">4</a></li>--%>
<%--							<li><a href="#">5</a></li>--%>
<%--							<li><a href="#">下一页</a></li>--%>
<%--							<li class="disabled"><a href="#">末页</a></li>--%>
<%--						</ul>--%>
<%--					</nav>--%>
<%--				</div>--%>
<%--			</div>--%>

		</div>

	</div>
</body>
</html>
