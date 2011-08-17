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
                <div class="empty_box" style="height:100px; width:100%"></div>
                <div>
                    Want to Tweet? Join us.<br>
                    <form action="/signup" method="post" style="padding-top: 10px">
                        <div class="span-3">
                            <label for="Full Name">
                                Full Name
                            </label>
                        </div>
                        <div class="span-5">
                            <input type="text" name="name" value="${name}" class="textbox" />
                        </div>
                        <div class="error-field span-6 last">
                            ${nameError}
                        </div>
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
                            <label for="Email address">
                                Email address
                            </label>
                        </div>
                        <div class="span-5">
                            <input type="email" name="email" value="${email}" class="textbox" />
                        </div>
                        <div class="error-field span-6 last">
                            ${emailError}
                        </div>
                        <div class="span-3">
                            <label for="Password">
                                Password
                            </label>
                        </div>
                        <div class="span-5">
                            <input type="password" name="password" class="textbox" />
                        </div>
                        <div class="error-field span-6 last">
                            ${passwordError}
                        </div>
                        <div class="span-3">
                            <label for="Confirm password">
                                Confirm password
                            </label>
                        </div>
                        <div class="span-5">
                            <input type="password" name="confirm_password" class="textbox" />
                        </div>
                        <div class="error-field span-6 last">
                            ${passwordsError}
                        </div>
                        <div class="span-5 prepend-3 append-6 last">
                            <input type="Submit" value="Sign up" class="signup_button"/>
                        </div>
                    </form>
                </div>
        </div>
    </div>

    <%@include file="/WEB-INF/jsp/footer.jsp" %>
    </body>
</html>
