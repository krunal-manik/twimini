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
                        <% if( (session.getAttribute("userId") == null) || (session.getAttribute("username").equals(request.getAttribute("currentUsername")))) { %>
                            <div style="height:90px;" class="padding border profile_box">
                                <div class="prepend-3 span-5"> Name </div>
                                <input type="text" value='${currentName}' class="prepend-1 span-5" />
                                <div class="prepend-3 span-5"> E-mail id </div>
                                <input type="text"  value='${currentEmail}' class="prepend-1 span-5"/>
                            </div>
                        <% } else { %>
                            <input type = "button" id = 'follow${currentUserId}' class = "btn border"
                            value = ${followStatus} onclick='changeFollowStatus(${currentUserId})' />

                        <% } %>
                        <div id="userTweetsContainer">
                            ${currentUsername}'s Tweets :
                            <div id="tweetsList">
                                <c:forEach var='item' items='${userTweets}'>
                                 <div data-dojo-type="<%= session.getAttribute("userId") != null ? "js.tweetContainer" : "js.tweetContainerWithoutOptions"%>"
                                      data-dojo-props="tweetId: '${item.tweetId}', tweetedBy: '${item.tweetedBy}', tweet: '${item.tweet}',
                                 timestamp: '${item.timestamp}', name: '${item.name}', username: '${item.username}', isFavorite: '${item.isFavorite}'">
                                 </div>
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