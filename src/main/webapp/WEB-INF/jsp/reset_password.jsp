<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>

<html>
    <head>
    <%@include file="/static/includes.txt" %>
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
                    <form action="/email" method=post>
                        <div class="prepend-5 span-9 append-8 last">
                            <div class="span-3 append-1">
                                Email :
                            </div>
                            <div class="span-5 last">
                                <div class="span-5 last">
                                    <input type="text" name = "email" value = "" />
                                </div>
                            </div>
                            <div class="span-8">
                                <%
                                    ReCaptcha c = ReCaptchaFactory.newReCaptcha("6Lfwr8YSAAAAANzx5MJ-x-nXXHxE985XnarE_ged", "6Lfwr8YSAAAAADODckf9OR9xR9CXHD3btgFe7mLN", false);
                                    out.print(c.createRecaptchaHtml(null, null));
                                %>
                            </div>
                            <div class="span-8">
                                <input type="Submit" class="btn" value = "Send reset instructions" style="float:left;margin-top:10px;width:170px;padding-top:2px;"/>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>