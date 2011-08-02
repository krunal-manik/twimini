<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML>
<html>
    <head>
    <%@include file="/static/includes.txt" %>
    <!--
    <script type="text/javascript">

        $(document).ready( function() {
            var queryString = window.location.hash.substr( 1 );
            if( queryString != '' && queryString != null ) {
                $.ajax({
                   type : "GET",
                   url :  "/gmail" ,
                   data : queryString ,
                   success : function() {
                       if (window.location.hash||hash.replace("#","")!=="")
                           window.location.hash = hash;
                   }
                });
            }
            window.location.hash = "";
        });

    </script>
        -->
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
