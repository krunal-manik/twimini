
<head>
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

</body>