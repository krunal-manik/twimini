<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <%@include file="/static/includes.html" %>
    </head>

    <body class=" claro ">
        <% if( session.getAttribute("username") == null ) { %>
            <%@include file="/WEB-INF/jsp/signin_header.jsp" %>
        <% } else { %>
            <%@include file="/WEB-INF/jsp/header.jsp" %>
        <% } %>

        <div class="container">
            <div class="body">
                <div class="prepend-9 span-6 append-9 last">

                        <div class="span-2 append-1">
                            Gmail emailid
                        </div>
                        <div class="span-2 append-1 last">
                            <input type="email" value="" name="email" />
                        </div>

                        <div class="prepend-3 span-3 last">
                            <a href="https://accounts.google.com/o/oauth2/auth?client_id=205184315336.apps.googleusercontent.com&redirect_uri=http://localhost:8080/import_contacts&response_type=token&scope=https://www.google.com/m8/feeds/">
                                <input type="Submit" value="Import Contacts" />
                            </a>
                        </div>
                </div>
            </div>
            <br>
            <ul id="userList">

            </ul>
        </div>
    </body>
</html>