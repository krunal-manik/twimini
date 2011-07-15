<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                <div class = "span-24">
                    <div class = "span-16">
                        <div id="userTweetsContainer">
                            <% if( (session.getAttribute("userId") == null) ) { %>

                            <% } else { %>
                                <input type = "button" id = 'follow${currentUserId}' class = "btn border"
                                value = ${followStatus} onclick='changeFollowStatus(${currentUserId})' />
                                <br>
                            <% } %>

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