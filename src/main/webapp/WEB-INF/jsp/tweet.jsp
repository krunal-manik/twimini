<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>

    <link rel="stylesheet" href="/static/css/basestyle.css" />

    <script type="text/javascript">
        function return_followers_onpane() {
            document.getElementById("sidepane").innerHTML = '';
            $.ajax({
                   type : "GET",
                   url : "follower_onpane",
                   success : function( data ){
                        for(i=0;i<data.length;i++){
                            arr = JSON.parse( JSON.stringify(data[i]));
                            var html = new EJS( {url:'/static/ejs_templates/follower.ejs'} ).render(arr);
                            var todoItemList = $(html);
                            $("#sidepane").append(todoItemList);
                        }
                   }
            });
        }

        function return_followings_onpane() {
            document.getElementById("sidepane").innerHTML = '';
            $.ajax({
                   type : "GET",
                   url : "following_onpane",
                   success : function( data ){
                        for(i=0;i<data.length;i++){
                            arr = JSON.parse( JSON.stringify(data[i]));
                            var html = new EJS( {url:'/static/ejs_templates/follower.ejs'} ).render(arr);
                            var todoItemList = $(html);
                            $("#sidepane").append(todoItemList);
                        }
                   }
            });
        }

    </script>


    <script type="text/javascript">
        function addTweet(){
            var tweetContent = document.getElementById("tweet").value;
            if( tweetContent == null || tweet == '' )return;
            if( tweetContent.length > 140 )alert( 'You cannot tweet more than 140 characters' );
            $.ajax({
                   type : "POST",
                   url : "tweet/addTweet",
                   data : "tweetContent=" + tweetContent ,
                   success : function( data ){
                        var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
                        var tweetHTML = $(html);
                        $("#sidepane").prepend(tweetHTML);
                   }

            });
        }
        function prependTweet(data){
            var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
            var tweetHTML = $(html);
            $("#tweetsList").prepend(tweetHTML);
        }
        function toggle_show() {
            $("#userTweetsContainer").toggle();
            $("#newsFeedContainer").toggle();
        }
        function prependTweet_o(data){
            var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
            var tweetHTML = $(html);
            $("#tweetsList_o").prepend(tweetHTML);
        }
    </script>
    <body>

    <div class="header">
            <div class="banner">
                <img src="/static/images/tweet_banner.jpg" style="width:130; height:45;"/>
            </div>
            <div class="taskbar">
                <div class="taskbar_item"> Hello ${sessionScope.username} </div>
                <div class="taskbar_item"> <a href="#" onclick="return_followings_onpane()"> Following (${followedLength})</a> </div>
                <div class="taskbar_item"> <a href="#" onclick="return_followers_onpane()"> Followers (${followerLength})</a> </div>
                <div class="taskbar_item"> <a href="/all_users">All Users !!! </a> </div>
                <div class="taskbar_item"><a href="/logout">Log out</a> </div>
            </div>
    </div>

    <div id="sidepane">
    </div>

    <h4>Tweet</h4><br>


        <input type = "text" id = "tweet" name = "tweetContent" value = "" length = "150"/>
        <input type = "button" value = "Tweet" onclick="addTweet()" />
        <br>
        <input type = "button" value = "Toggle" onclick = "toggle_show();">
        <br>
        <div id="userTweetsContainer">
            Your Tweets :
            <div id="tweetsList">
                <c:forEach var='item' items='${tweetsList}'>
                    <script type="text/javascript">
                        prependTweet({tweetId:${item.tweetId}, tweetedBy:${item.tweetedBy} , tweet:'${item.tweet}' , timestamp:'${item.timestamp}'})
                    </script>
                </c:forEach>
            </div>
        </div>
        <br>
        <div id="newsFeedContainer">
            Newsfeed :
            <div id="tweetsList_o">
                <c:forEach var='item' items='${tweetsList_o}'>
                    <script type="text/javascript">
                        prependTweet_o({tweetId:${item.tweetId}, tweetedBy:${item.tweetedBy} , tweet:'${item.tweet}' , timestamp:'${item.timestamp}'})
                    </script>
                </c:forEach>
            </div>
            <script type="text/javascript">
                $("#newsFeedContainer").hide();
            </script>
        </div>

    </body>
</html>