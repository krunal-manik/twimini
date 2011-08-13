
<head>
    <%@include file="/static/includes.html" %>
</head>
<body>
<input  type="text" name="pattern" id = "pattern" onkeyup="getSearchResults()"/>
<br>
    <form action="/email" method=post>
        <input type="text" name = "email" value = "" />
        <input type="Submit" />
    </form>
    <form action="/testing" enctype="multipart/form-data" method=post>
	    <input type="file" name="file" />
	    <input type="submit" name="submit">
    </form>

    <img src = "${image}" height="150" width="150"/>
    <div id="gmail-contacts-importer">

        <input type="button" onclick="loadContactImporter();" value="Import Contacts"/>
    </div>


</body>