<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>
    <script type="text/javascript">
        function prependTweet(data){
            var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
            var tweetHTML = $(html);
            $("#tweetsList").prepend(tweetHTML);
        }
    </script>
    <body>

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

        <a href="/profile/followed?userId=${currentUserId}">
            Following ${fn:length(followedList)} users
        </a>
        <br>
        <a href="/profile/follower?userId=${currentUserId}">
            Followed by ${fn:length(followerList)} users
        </a>
        <br>


    </body>
</html>