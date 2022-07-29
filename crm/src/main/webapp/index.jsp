<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" language="java" %>
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
        $(function () {
            $("#FileDownload").click(function(){
                window.location.href = "workbench/activity/FileDownload.do"
            })
        })
    </script>

</head>
    <script type="text/javascript">
        $(function(){
            $("#myTest").datetimepicker({
                language:'zh-CN',
                format:'yyyy-mm-dd',
                minView:'month',
                initialDate:new Date(),
                autoclose:true,
                todayBtn:true,
                clearBtn:true
            })

            $("#div").bs_pagination({
                currentPage: 1,
                rowsPerPage: 50,
                // maxRowsPerPage: 100,
                totalPages: 100,
                totalRows: 0,

                visiblePageLinks: 5,

                showGoToPage: true,
                showRowsPerPage: true,
                showRowsInfo: true,
                showRowsDefaultInfo: true,
                onchangePage:function(event,pageObj){
                    queryActivityAll(pageObj.currentPage,pageObj.rowsPerPage)
                }
            })
        })
    </script>
<body>
<h1>字符串</h1>
<input type="text" id="myTest" readonly>

<div id="div"></div>

<input type="button" id="FileDownload" value="下载">
</body>
</html>
