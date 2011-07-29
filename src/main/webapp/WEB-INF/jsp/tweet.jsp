<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <%@include file="/static/includes.txt" %>
        <script type="text/javascript">
            dojo.require("dijit.Dialog");
//            dojo.addOnLoad(function() {
//                alert(dijit.byId("replyPopUp").title);
//                var replyPopUp = dijit.byId("replyPopUp");
//                dojo.connect(dojo.byId("reply_52"), "onClick", replyPopUp, "show");
//            });
        </script>

    </head>

    <body class="claro">

        <div id="replyPopUp" title="Colorful" dojoType="dijit.Dialog" style="height:300">
            My background color is Green
        </div>


        <%@include file="/WEB-INF/jsp/header.jsp" %>

        <div class = "body">
        <div class = "container">
            <div class = "span-24">
                <div class = "span-16">
                        <form name = "tweet_form">
                            <div style = "position:relative">
                                <textarea id = "tweet" onkeypress="return imposeMaxLength(event, this, 140);"
                                name = "tweetContent" value = "" class="span-16" placeholder="tweet !!!"
                                style="resize:none; height:60px;" onkeyup = "givesuggestions(event, this);"></textarea>
                                <!--div id= "tagging_dropdown" class = "dropdown_box border" style = "display:none"></div-->

                                <select id="tagging_dropdown1" style="display:none" class="dropdown_select"> </select>

                            </div>
                            <input type = "button" value = "Tweet" onclick="addTweet()"/>
                        </form>
                    <div id="newsFeedContainer">
                        Newsfeed :
                        <div id="tweetsList_o">
                            <c:forEach var='item' items='${newsFeed}'>
                                <jsp:include page="single_tweet.jsp">
                                    <jsp:param name="tweetId" value="${item.tweetId}" />
                                    <jsp:param name="tweetedBy" value="${item.tweetedBy}" />
                                    <jsp:param name="tweet" value="${item.tweet}" />
                                    <jsp:param name="timestamp" value="${item.timestamp}" />
                                    <jsp:param name="name" value="${item.name}" />
                                    <jsp:param name="username" value="${item.username}" />
                                    <jsp:param name="isFavorite" value="${item.isFavorite}" />
                                </jsp:include>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <%@ include file="sidebar.jsp" %>

            </div>
        </div>
        </div>

    </body>
</html>