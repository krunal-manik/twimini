<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
        <%@include file="/WEB-INF/jsp/header.jsp" %>

        <div class = "body">
        <div class = "container">
            <div class = "span-24">
                <div class = "span-16" style="">
                <h4>TWEET</h4>
                <form name = "tweet_form">
                    <textarea id = "tweet" onkeypress="return imposeMaxLength(event, this, 140);"
                    name = "tweetContent" value = "" class="span-16" placeholder="tweet !!!"
                    style="resize:none; height:60px;"></textarea>
                    <input type = "button" value = "Tweet" onclick="addTweet()" />
                </form>
                    <div id="userTweetsContainer">
                        Your Tweets :
                        <div id="tweetsList">
                            <c:forEach var='item' items='${tweetsList}'>
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
                <div class = "span-8 last" style="background-color:#FFFFFF; height:200px">
                </div>
            </div>
        </div>
        </div>

    </body>
</html>