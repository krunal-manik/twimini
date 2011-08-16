<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
    <head>
    <%@include file="/static/includes.html" %>
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
                                <br>
                                <a href="/import_contacts" class="as-link">Import Your Gmail Contacts</a>
                            </div>
                        <% } else { %>
                            <input type = "button" id = 'follow${currentUserId}' class = "btn border"
                            value = '${followStatus}' onclick='changeFollowStatus(${currentUserId})' />
                        <% } %>

                        <div id="userTweetsContainer">
                            <div dojoType="dijit.layout.TabContainer" style="width:100%" doLayout="false">

                                <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id:'userfeed', title:'userfeed', user:'${currentUserId}', favoriter:'<%=session.getAttribute("userId")%>'">
                                </div>
                                <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id:'mentions', title:'mentions', user:'${currentUserId}', favoriter:'<%=session.getAttribute("userId")%>'">
                                </div>
                                <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id:'favorites', title:'favorites', user:'${currentUserId}', favoriter:'<%=session.getAttribute("userId")%>'">
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