<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
    <head>
        <%@include file="/static/includes.html" %>
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
                <form action="/signup" method="post" style="padding-top: 10px;">
                    <div class="span-3 append-1">
                        <label for="Full Name">
                            Full Name
                        </label>
                    </div>
                    <div class="span-4 last">
                        <input type="text" name="name" value= "" class="textbox" />
                    </div>
                    <div class="span-3 append-1">
                        <label for="Username">
                            Username
                        </label>
                    </div>
                    <div class="span-4 last">
                        <input type="text" name="username" value="" class="textbox" />
                    </div>
                    <div class="span-3 append-1">
                        <label for="Email address">
                            Email address
                        </label>
                    </div>
                    <div class="span-4 last">
                        <input type="email" name="email" value = "" class="textbox" />
                    </div>
                    <div class="span-3 append-1">
                        <label for="Password">
                            Password
                        </label>
                    </div>
                    <div class="span-4 last">
                        <input type="password" name="password" class="textbox" />
                    </div>
                    <div class="span-3 append-1">
                        <label for="Confirm password">
                            Confirm password
                        </label>
                    </div>
                    <div class="span-4 last">
                        <input type="password" name="confirm_password" class="textbox" />
                    </div>
                    <div class="span-3 prepend-5 last">
                        <input type="Submit" value="Sign up" class="signup_button"/>
                    </div>
                </form>
        </div>
    </div>

    </body>
</html>
