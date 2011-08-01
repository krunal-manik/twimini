<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <%@include file="/static/includes.txt" %>
        <script type="text/javascript">
            dojo.require("dijit.form.MultiSelect");
            dojo.require("dijit.Dialog");
            dojo.addOnLoad(function() {
                new dijit.form.MultiSelect ({
                        name: 'dynamic'
                    },
                    dojo.byId("tagging_dropdown")
                );
                new dijit.form.MultiSelect ({
                        name: 'dynamic2'
                    },
                    dojo.byId("tagging_dropdown_dialog")
                );
            });
        </script>

    </head>

    <body class=" claro ">
        <div id="replyPopUp" title="Colorful" dojoType="dijit.Dialog">
            My background color is Green
        </div>

        <%@include file="/WEB-INF/jsp/header.jsp" %>

        <div class = "body">
        <div class = "container">
            <div class = "span-24">
                <div class = "span-16">
                        <form name = "tweet_form">
                            <div style = "position:relative">
                                <textarea id = "tweet" onkeypress="return imposeMaxLength(event, this, 140, dojo.byId('tagging_dropdown'));"
                                name = "tweetContent" value = "" class="span-16" placeholder="tweet !!!"
                                style="resize:none; height:60px;" onkeyup = "givesuggestions(event, this, dojo.byId('tagging_dropdown'));"></textarea>
                                <!--div id= "tagging_dropdown" class = "dropdown_box border" style = "display:none"></div-->

                                <select id="tagging_dropdown" style="display:none" class="dropdown_select"> </select>

                            </div>
                            <input type = "button" value = "Tweet" onclick="addTweet()"/>
                        </form>
                    <div id="newsFeedContainer">
                        Newsfeed :
                        <c:forEach var='item' items='${newsFeed}'>
                            <div data-dojo-type="js.tweetContainerWidget" data-dojo-props="tweetId: '${item.tweetId}', tweetedBy: '${item.tweetedBy}', tweet: '${item.tweet}',
                            timestamp: '${item.timestamp}', name: '${item.name}', username: '${item.username}', isFavorite: '${item.isFavorite}'">
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <%@ include file="sidebar.jsp" %>

            </div>
        </div>
        </div>


    </body>
</html>