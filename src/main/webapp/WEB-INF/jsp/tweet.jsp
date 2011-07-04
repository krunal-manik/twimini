<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>

    <script type="text/javascript">
        function addTweet(){
            var tweetContent = document.getElementById("tweet").value;
            if( tweetContent == null || tweet == '' )return;
            if( tweetContent.length > 140 )alert( 'You cannot tweet more than 140 characters' );
            $.ajax({
                   type : "POST",
                   url : "tweet/addTweet",
                   data : "tweetContent=" + tweetContent,
                   success : function( data ){
                        var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
                        var tweetHTML = $(html);
                        $("#tweetsList").prepend(tweetHTML);
                   }

             });
        }
        function prependTweet(data){
            var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
            var tweetHTML = $(html);
            $("#tweetsList").prepend(tweetHTML);
        }
    </script>
    <body>
    <h4>Tweet</h4><br>
        Hello ${sessionScope.username}  <a href="/logout">Log out</a>

        <br>
        <a href="/followed">People u r following !!! </a>
        <br>
        <a href="/follower">People who r following u!!! </a>
        <br>
        <a href="/all_users">All Users !!! </a>

        <input type = "text" id = "tweet" name = "tweetContent" value = "" length = "150"/>
        <input type = "button" value = "Tweet" onclick="addTweet()" />
        <div id="tweetsList">
            <c:forEach var='item' items='${tweetsList}'>
                <script type="text/javascript">
                    prependTweet({tweetId:${item.tweetId}, tweetedBy:${item.tweetedBy} , tweet:'${item.tweet}' , timestamp:'${item.timestamp}'})
                </script>
            </c:forEach>
        </div>

    </body>
</html>