<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>

    <link href='/static/images/twitter_favicon.png' rel='shortcut icon' type='image/x-icon'/>
    <link rel="stylesheet" href="/static/css/blueprint/screen.css" type="text/css" media="screen, projection">
    <link rel="stylesheet" href="/static/css/blueprint/print.css" type="text/css" media="print">
    <!--[if lt IE 8]><link rel="stylesheet" href="/static/css/blueprint/ie.css" type="text/css" media="screen, projection"><![endif]-->
        <link rel="stylesheet" type="text/css" href="/static/css/basestyle.css" />

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

        function all_users_onpane() {
            document.getElementById("sidepane").innerHTML = '';
            $.ajax({
                   type : "GET",
                   url : "all_users_onpane",
                   success : function( data ){
                        for(i=0;i<data.length;i++){
                            arr = JSON.parse( JSON.stringify(data[i]));
                            var html = new EJS( {url:'/static/ejs_templates/add_new_followers.ejs'} ).render(arr);
                            var todoItemList = $(html);
                            $("#sidepane").append(todoItemList);
                        }
                   }
            });
        }

        function changeFollowStatus(userId) {
                var buttonName = "follow_" + userId;
                if( document.getElementById(buttonName).value == "Follow" ){
                    $.ajax({
                       type : "POST",
                       url : "/all_users/addFollowing",
                       data : "userId=" + userId ,
                       success : function() {
                            document.getElementById(buttonName).value = "Unfollow";
                       }
                    });
                }
                else {
                    $.ajax({
                       type : "POST",
                       url : "/all_users/removeFollowing",
                       data : "userId=" + userId ,
                       success : function() {
                           alert( 'success in remove' );
                            document.getElementById(buttonName).value = "Follow";
                           alert( 'final success in remove' );
                       }
                    });
                }
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
                        $("#tweetsList").prepend(tweetHTML);
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
        <%@include file="/WEB-INF/jsp/header.jsp" %>
    <!--
    <div class="header">
            <div class="banner">
                <img src="/static/images/tweet_banner.jpg" style="width:130; height:45;"/>
            </div>
            <div class="taskbar">
                <div class="taskbar_item"> Hello ${sessionScope.username} </div>
                <div class="taskbar_item"> <a href="#" onclick="return_followings_onpane()"> Following (${followedLength})</a> </div>
                <div class="taskbar_item"> <a href="#" onclick="return_followers_onpane()"> Followers (${followerLength})</a> </div>
                <div class="taskbar_item"> <a href="#" onclick="all_users_onpane()">All Users !!! </a> </div>
                <div class="taskbar_item"><a href="/logout">Log out</a> </div>
            </div>
    </div>
    <div id="sidepane">
    </div>
     -->


    <div class = "body">
    <div class = "container">
        <div class = "span-24">
            <div class = "span-16">
                <h4>Tweet</h4><br>


            <input type = "text" id = "tweet" name = "tweetContent" value = "" length = "150"/>
            <input type = "button" value = "Tweet" onclick="addTweet()" />
                <input type = "button" value = "Toggle" onclick = "toggle_show();">
                <br>
                <div id="userTweetsContainer">
                    Your Tweets :
                    <div id="tweetsList">
                        <c:forEach var='item' items='${tweetsList}'>
                            <!--
                            <script type="text/javascript">
                                prependTweet({tweetId:${item.tweetId}, tweetedBy:${item.tweetedBy} , tweet:'${item.tweet}' , timestamp:'${item.timestamp}'})
                            </script>
                            -->
                            <jsp:include page="single_tweet.jsp">
                                <jsp:param name="tweetId" value="${item.tweetId}" />
                                <jsp:param name="tweetedBy" value="${item.tweetedBy}" />
                                <jsp:param name="tweet" value="${item.tweet}" />
                                <jsp:param name="timestamp" value="${item.timestamp}" />
                                <jsp:param name="timestamp" value="${item.timestamp}" />
                            </jsp:include>
                        </c:forEach>
                    </div>
                </div>
                <br>
                <div id="newsFeedContainer">
                    Newsfeed :
                    <div id="tweetsList_o">
                        <c:forEach var='item' items='${tweetsList_o}'>
                            <div class = "prepend-1 span-14 append-1">
                                <!--
                                <script type="text/javascript">
                                    prependTweet_o({tweetId:${item.tweetId}, tweetedBy:${item.tweetedBy} , tweet:'${item.tweet}' , timestamp:'${item.timestamp}'})
                                </script>
                                -->
                            <jsp:include page="single_tweet.jsp">
                                <jsp:param name="tweetId" value="${item.tweetId}" />
                                <jsp:param name="tweetedBy" value="${item.tweetedBy}" />
                                <jsp:param name="tweet" value="${item.tweet}" />
                                <jsp:param name="timestamp" value="${item.timestamp}" />
                            </jsp:include>
                            </div>
                        </c:forEach>
                    </div>
                    <script type="text/javascript">
                        $("#newsFeedContainer").hide();
                    </script>
                </div>
            </div>
            <div class = "span-8 last" style="background-color:#FFFFFF; height:200px">
            </div>
        </div>
    </div>
    </div>

    </body>
</html>