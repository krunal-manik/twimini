<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
    <%@include file="/static/includes.html" %>
    </head>

    <body>

        <% if( session.getAttribute("username") == null ) { %>
            <%@include file="/WEB-INF/jsp/signin_header.jsp" %>
        <% } else { %>
            <%@include file="/WEB-INF/jsp/header.jsp" %>
        <% } %>

        <div class = "body">
            <div class = "container">
                <div class="reset-box">
                    <form action="/${username}/updatePassword" method=post onsubmit="return checkPasswords(this);">
                        <div class="prepend-7 span-11 append-8 last">
                            <div class="span-3">
                                Password :
                            </div>
                            <div class="span-4">
                                <input type="password" name = "password" value = "" />
                            </div>
                            <div class="span-4 last error-field" id="passwordError">
                                ${passwordError}
                            </div>
                            <div class="span-3">
                                Confirm Password :
                            </div>
                            <div class="span-4">
                                <input type="password" name = "confirm_password" value = "" />
                            </div>
                            <div class="span-4 last error-field" id="confirmPasswordError">
                                ${confirmPasswordError}
                            </div>
                            <input type="hidden" name = "token" value = "${token}" />
                            <div class="prepend-4 span-5 last">
                                <input type="Submit" value = "Update password" />
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>