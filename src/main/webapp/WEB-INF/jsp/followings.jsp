<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
                <div class = "span-16" style="">
                    <div id="newsFeedContainer">
                        PEOPLE FOLLOWING ${username}
                        <div id="followedList">
                            <c:forEach var='item' items='${followedList}'>
                                <div class = "span-16 tweet_container">
                                    <div class = "span-2 usr_image_box">
                                        <img src="/static/images/def_user.jpg" style="border-color:#000000; border-width:2px; border-style:solid"/>
                                    </div>
                                    <div class= "span-14 last tweet_box">
                                        <img src="/static/images/comment_arrow.png" class="comment_arrow" />
                                        <div class="padding">
                                            <div class = "tweeter_name"> <a href = "/${item.username}">${item.username} </a></div>
                                            <div style="width:100%; height:25px">
                                                <% if (session.getAttribute("userId") != null) {%>
                                                    <c:if test="${item.followStatus == 'Unfollow'}">
                                                        <div id = 'follow_${item.userId}' class = "i fav span-3" onclick = 'changeFollowStatusForDivs(${item.userId})'>${item.followStatus}</div>
                                                    </c:if>
                                                    <c:if test="${item.followStatus == 'Follow'}">
                                                        <div id = 'follow_${item.userId}' class = "i unfav span-3" onclick = 'changeFollowStatusForDivs(${item.userId})'>${item.followStatus}</div>
                                                    </c:if>
                                                    <div class = "span-3"><a href="#"><img src = "/static/images/empty_star.png" />Reply</a> </div>
                                                    <div class = "span-3"><a href="#"><img src = "/static/images/empty_star.png" />Retweet</a> </div>
                                                <% } %>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        </div>

    </body>
</html>