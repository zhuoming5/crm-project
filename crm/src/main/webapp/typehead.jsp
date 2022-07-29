<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bs_typeahead/bootstrap3-typeahead.min.js"></script>
<html>
<script type="text/javascript">
    $(function(){
        $("#div").typeahead({
            source:["动1","动2","动3"]
        })
    })
</script>
<head>
    <title>Title</title>
</head>
<body>
<input type="text" id="div">
</body>
</html>
