<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
    <link href='/static/images/twitter_favicon.png' rel='shortcut icon' type='image/x-icon'/>
    <link rel="stylesheet" href="/static/css/blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="/static/css/blueprint/print.css" type="text/css" media="print">
    <!--[if lt IE 8]><link rel="stylesheet" href="/static/css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
        <link rel="stylesheet" type="text/css" href="/static/css/basestyle.css" />
    </head>

    <body>
    <%@include file="/WEB-INF/jsp/signin_header.jsp" %>
    <div class="container body">
        <div class="span-6 append-2">
             <div class="empty_box" style="height:150px; width=100%"></div>
             <img src="/static/images/twitter_bird.gif" />
        </div>
        <div class="prepend-8 span-8 last">
                <div class="empty_box" style="height:100px; width=100%"></div>
                Want to Tweet? Join us.<br>
                <form action="/register" method="post" style="padding-top: 10px;">
                    <div class="span-3 append-1"> Full Name </div>
                    <div class="span-4 last"> <input type="text" name="name" value= "" class="textbox" /> </div>
                    <div class="span-3 append-1"> Username </div>
                    <div class="span-4 last"> <input type="text" name="username" value="" class="textbox" /> </div>
                    <div class="span-3 append-1"> Email address </div>
                    <div class="span-4 last"> <input type="email" name="email" value = "" class="textbox" /> </div>
                    <div class="span-3 append-1"> Password </div>
                    <div class="span-4 last"> <input type="password" name="password" class="textbox" /> </div>
                    <div class="span-3 append-1"> Confirm password </div>
                    <div class="span-4 last"> <input type="password" name="password2" class="textbox" /> </div>
                    <div class="span-3 prepend-5 last"> <input type="Submit" value="Sign up" class="signup_button"/> </div>
                </form>
        </div>
    </div>

    <%@include file="/WEB-INF/jsp/footer.jsp" %>
    </body>
</html>

<!--
<html>
    <body>
    <h4>Login</h4><br>
    ${message}
    <form action = "/login" method=post>
        <table>
            <tr>
                <td> Username : </td>
                <td> <input type = "text" name = "username" value = "" /> </td>
            </tr>
            <tr>
                <td> Password : </td>
                <td> <input type = "password" name = "password" value = "" /> </td>
            </tr>
            <tr><input type = "submit" /></tr>
        </table>
    </form>
    Not a user ? <a href="/register">Sign up here</a>

    </body>
</html>
-->