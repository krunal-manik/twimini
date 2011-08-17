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
        <div class="prepend-8 span-14 append-2 last">
                <div class="empty_box" style="height:100px; width=100%"></div>
                <div>
                    Login to twitter<br>
                    <div class="error-field">
                        ${loginError}
                    </div>
                    <form action="/login" method="post" style="padding-top: 10px">
                        <div class="span-3">
                            <label for="Username">
                                Username
                            </label>
                        </div>
                        <div class="span-5">
                            <input type="text" name="username" value="${username}" class="textbox" />
                        </div>
                        <div class="error-field span-6 last">
                            ${usernameError}
                        </div>
                        <div class="span-3">
                            <label for="Password">
                                Password
                            </label>
                        </div>
                        <div class="span-5 append-6 last">
                            <input type="password" name="password" class="textbox" />
                        </div>
                        <div class="span-5 prepend-3 append-6 last" style="padding-top:10px">
                            <a href="/forgot_password" class="as-link">Forgot Password ?</a>
                        </div>
                        <div class="span-5 prepend-3 append-6 last" style="padding-top:10px">
                            <input type="Submit" value="Login" class="signup_button"/>
                        </div>
                    </form>
                </div>
        </div>
    </div>

    </body>
</html>