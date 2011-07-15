<!DOCTYPE HTML>
<html>
    <head>
    <%@include file="/static/includes.txt" %>
    </head>

    <body>
        <form action="/test_upload_image" method=POST>
            <input type = "file" name = "img"/>
            <input type = "submit" value = "submit"/>
        </form>
    </body>
</html>