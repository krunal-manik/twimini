<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
        <!-- jQuery and ejs scripts -->
        <script type="text/javascript" src="/static/js/jquery.min.js"></script>
        <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>
        <script type="text/javascript" src="/static/js/functions.js"></script>

        <!-- favicon -->
        <link href='/static/images/twitter_favicon.png' rel='shortcut icon' type='image/x-icon'/>

        <!-- blueprint css -->
        <link rel="stylesheet" href="/static/css/blueprint/screen.css" type="text/css" media="screen, projection">
        <link rel="stylesheet" href="/static/css/blueprint/print.css" type="text/css" media="print">
        <!--[if lt IE 8]><link rel="stylesheet" href="/static/css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->

        <!-- base css -->
        <link rel="stylesheet" type="text/css" href="/static/css/base.css" />
    </head>

    <body>

        <% if( session.getAttribute("username") == null ) { %>
            <%@include file="/WEB-INF/jsp/signin_header.jsp" %>
        <% } else { %>
            <%@include file="/WEB-INF/jsp/header.jsp" %>
        <% } %>

        <div class = "body">
            <div class = "container">
                <div class = "span-24">
                    <div class = "span-16">
                        <div id="userTweetsContainer">
                            ${currentUsername}'s Tweets :
                            <div id="tweetsList">
                                <c:forEach var='item' items='${userTweets}'>
                                    <jsp:include page="single_tweet.jsp">
                                        <jsp:param name="tweetId" value="${item.tweetId}" />
                                        <jsp:param name="tweetedBy" value="${item.tweetedBy}" />
                                        <jsp:param name="tweet" value="${item.tweet}" />
                                        <jsp:param name="timestamp" value="${item.timestamp}" />
                                        <jsp:param name="name" value="${item.name}" />
                                        <jsp:param name="username" value="${item.username}" />
                                    </jsp:include>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <%@include file="sidebar.jsp" %>

                </div>
            </div>
        </div>
    </body>
</html>