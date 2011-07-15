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
                        <div id="followerList">
                            <c:forEach var='item' items='${followerList}'>
                                <div class = "span-16 tweet_container">
                                    <div class = "span-2 usr_image_box">
                                        <img src="/static/images/def_user.jpg" style="border-color:#000000; border-width:2px; border-style:solid"/>
                                    </div>
                                    <div class= "span-14 last tweet_box">
                                        <img src="/static/images/comment_arrow.png" class="comment_arrow" />
                                        <div class="padding">
                                            <div class = "tweeter_name"> <a href = "/${item.username}">${item.username} </a> </div>
                                            <div style="width:100%; height:25px">
                                                <div class = "span-3"><a href="#"> <img src = "/static/images/empty_star.png" />Follow</a> </div>
                                                <div class = "span-3"><a href="#"><img src = "/static/images/empty_star.png" />Mention</a> </div>
                                                <div class = "span-3"><a href="#"><img src = "/static/images/empty_star.png" />OPTIONS</a> </div>
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