<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
    <%@include file="/static/includes.html" %>
    <script type="text/javascript">
        if (location.hash) {
            var selected = location.hash.split("/")[1];
        }
        dojo.require("dijit.form.MultiSelect");
        dojo.require("dijit.Dialog");
        dojo.require("dijit.layout.TabContainer");
        dojo.require("dijit.layout.ContentPane");
        dojo.addOnLoad( function() {
            if (selected) {
                dijit.byId("tabContainer").selectChild(dijit.byId(selected));
            }
        });
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
                        <% } else { %>
                            <input type = "button" id = 'follow${currentUserId}' class = "btn border"
                            value = '${followStatus}' onclick='changeFollowStatus(${currentUserId})' />
                        <% } %>

                        <div class="topbox prepend-1 span-15" style="padding:17px; border:1px #AAAAAA solid; margin-bottom: 5px; diplay:block">
                            <img src="/photos/${currentUserId}.jpg" height="100" width="100" class="span-3" style="border-color:#000000; border-width:2px; border-style:solid; margin:2px"/>
                            <div class="span-10 last" style="margin-left:20px; padding:10px">
                                <div class="span-5">
                                    NAME
                                </div>
                                <div class="span-5 last">
                                    ${name}
                                </div>

                                <div class="span-5" style="padding-top: 10px;">
                                    About <a href="/${currentUsername}" class="as-link">@${currentUsername}</a>
                                </div>
                                <div class="span-5 last" style="overflow:hidden;padding-top: 10px;">
                                    ${aboutCurrentUser}
                                </div>

                                <% if( session.getAttribute("username") != null && (session.getAttribute("username").toString()).equals(request.getAttribute("currentUsername"))) { %>
                                    <a href="/edit_profile" class="as-link">Edit your profile</a>
                                <% } %>
                            </div>
                        </div>


                        <div id="userTweetsContainer">
                            <div dojoType="dijit.layout.TabContainer" id="tabContainer" data-dojo-props="id:'tabContainer'" style="width:100%" doLayout="false">

                                <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id:'userfeed', href:'#!/userfeed', title:'userfeed', user:'${currentUserId}', favoriter:'<%=session.getAttribute("userId")%>'">
                                </div>
                                <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id:'favorites', href:'#!/favorites', title:'favorites', user:'${currentUserId}', favoriter:'<%=session.getAttribute("userId")%>'">
                                </div>
                                <div data-dojo-type="js.userBoxContainer" data-dojo-props="id: 'follower', href:'#!/follower', title:'follower', user:'${currentUserId}', loggedInUser:'<%=session.getAttribute("userId")%>'">
                                </div>
                                <div data-dojo-type="js.userBoxContainer" data-dojo-props="id: 'following', href:'#!/following', title:'following', user:'${currentUserId}', loggedInUser:'<%=session.getAttribute("userId")%>'">
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