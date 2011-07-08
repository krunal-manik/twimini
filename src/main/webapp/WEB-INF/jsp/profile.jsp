<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>

    <link rel="stylesheet" href="/static/css/basestyle.css" />
    <script type="text/javascript">
        function prependTweet(data){
            var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
            var tweetHTML = $(html);
            $("#tweetsList").prepend(tweetHTML);
        }
    </script>
    <body>

        <div class="header">
            <div class="banner">
                <img src="/static/images/tweet_banner.jpg" style="width:130; height:45;"/>
            </div>
            <div class="taskbar">
                <div class="taskbar_item"> NAME </div>
                <div class="taskbar_item"> <a href="/profile/followed?userId=${currentUserId}"> Following ${fn:length(followedList)} users </a> </div>
                <div class="taskbar_item"> <a href="/profile/follower?userId=${currentUserId}"> Followed by ${fn:length(followerList)} users </a> </div>
                <div class="taskbar_item"><a href="/logout">Log out</a> </div>
            </div>
        </div>

        <div id="sidepane">
        </div>


        <div class="main_body">
            Hello ${sessionScope.username}  <a href="/logout">Log out</a>
            <br>
            <h4>Profile</h4><br>
            <br>
            Tweets
                <div id="tweetsList">
                    <c:forEach var='item' items='${userTweets}'>
                        <script type="text/javascript">
                            prependTweet({tweetId:${item.tweetId}, tweetedBy:${item.tweetedBy} , tweet:'${item.tweet}' , timestamp:'${item.timestamp}'})
                        </script>
                    </c:forEach>
                </div>
        </div>


    </body>
</html>