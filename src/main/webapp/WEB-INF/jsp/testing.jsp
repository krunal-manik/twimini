
<head>
    <%@include file="/static/includes.txt" %>
</head>
<body>
    <form action="/email" method=post>
        <input type="text" name = "email" value = "" />
        <input type="Submit" />
    </form>
    <form action="/testing" enctype="multipart/form-data" method=post>
	    <input type="file" name="file" />
	    <input type="submit" name="submit">
    </form>

    <img src = "${image}"/>
    <div id="gmail-contacts-importer">

        <input type="button" data-role="button" data-rel="dialog" data-url="https://accounts.google.com/o/oauth2/auth?client_id=205184315336.apps.googleusercontent.com&redirect_uri=http://localhost:8080/gmail&response_type=token&scope=https://www.google.com/m8/feeds/"
                data-popup="true" />
    </div>


</body>