<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
    <%@include file="/static/includes.txt" %>
    <script type="text/javascript">
        dojo.require("dijit.form.MultiSelect");
        dojo.require("dijit.Dialog");
        dojo.require("dijit.layout.TabContainer");
        dojo.require("dijit.layout.ContentPane");
    </script>
    </head>

    <body class="claro">
        <div id="replyPopUp" dojoType="dijit.Dialog">
        </div>
        <% if( session.getAttribute("username") == null ) { %>
            <%@include file="/WEB-INF/jsp/signin_header.jsp" %>
        <% } else { %>
            <%@include file="/WEB-INF/jsp/header.jsp" %>
        <% } %>

        <div class = "body">
            <div class = "container">
                <div class = "span-24">
                    <div class = "span-16">
                        <% if (session.getAttribute("username") == null) {%>
                        <% } else if((session.getAttribute("username").equals(request.getAttribute("currentUsername")))) { %>
                            <div class="topbox" style="padding:17px; border:1px #AAAAAA solid">
                                <a href="/edit_profile" class="as-link">Edit your profile</a>
                                <div class="as-link" onclick="loadContactImporter();">Import Contacts</div>
                            </div>
                        <% } else { %>
                            <input type = "button" id = 'follow${currentUserId}' class = "btn border"
                            value = '${followStatus}' onclick='changeFollowStatus(${currentUserId})' />
                        <% } %>

                        <div id="userTweetsContainer">
                            <div dojoType="dijit.layout.TabContainer" style="width:100%" doLayout="false">
                                <div dojoType="dijit.layout.ContentPane" style="padding:17px; background-color:#eaeaea;" title="${currentUsername}'s Tweets" selected="true">
                                    <div id="tweetsList">
                                        <c:forEach var='item' items='${userTweets}'>
                                            <div data-dojo-type="js.tweetContainer"
                                              data-dojo-props="tweetId: '${item.tweetId}', tweetedBy: '${item.tweetedBy}', tweet: '${item.tweet}',
                                            timestamp: '${item.timestamp}', name: '${item.name}', username: '${item.username}', isFavorite: '${item.isFavorite}',
                                            tweetOptions: '<%= session.getAttribute("userId") != null %>', retweetedBy: '0'">
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div dojoType="dijit.layout.ContentPane" style="padding:17px; background-color:#eaeaea;" title="${currentUsername}'s Mentions" selected="true">
                                    <div id="userMentionList">
                                        <c:forEach var='item' items='${userMentionList}'>
                                            <div data-dojo-type="js.tweetContainer"
                                              data-dojo-props="tweetId: '${item.tweetId}', tweetedBy: '${item.tweetedBy}', tweet: '${item.tweet}',
                                            timestamp: '${item.timestamp}', name: '${item.name}', username: '${item.username}', isFavorite: '${item.isFavorite}',
                                            tweetOptions: '<%= session.getAttribute("userId") != null %>', retweetedBy: '0'">
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                                <div dojoType="dijit.layout.ContentPane" style="padding:17px; background-color:#eaeaea;" title="${currentUsername}'s Favorites" selected="true">
                                    <div id="userFavoriteList">
                                        <c:forEach var='item' items='${userFavoriteList}'>
                                            <div data-dojo-type="js.tweetContainer"
                                              data-dojo-props="tweetId: '${item.tweetId}', tweetedBy: '${item.tweetedBy}', tweet: '${item.tweet}',
                                            timestamp: '${item.timestamp}', name: '${item.name}', username: '${item.username}', isFavorite: '${item.isFavorite}',
                                            tweetOptions: '<%= session.getAttribute("userId") != null %>', retweetedBy: '0'">
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <%@include file="sidebar.jsp" %>

                </div>
            </div>
        </div>

    </body>
</html>