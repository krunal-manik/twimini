<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
    <%@include file="/static/includes.html" %>

    <script>
        dojo.require("dijit.layout.TabContainer");
        dojo.require("dijit.layout.ContentPane");

    </script>
    </head>

    <body class=" claro ">
        <% if( session.getAttribute("username") == null ) { %>
            <%@include file="/WEB-INF/jsp/signin_header.jsp" %>
        <% } else { %>
            <%@include file="/WEB-INF/jsp/header.jsp" %>
        <% } %>

        <div class = "body">
            <div class = "container">
                <div class = "span-24">
                    <div class = "span-16">
                        <div id="userListContainer">
                            ${message}
                            <!--
                            <div id="userList">
                                <c:forEach var='item' items='${userList}'>
                                    <div data-dojo-type="js.userContainer"
                                    data-dojo-props="name : '${item.name}' , username : '${item.username}',
                                    followStatus: '${item.followStatus}' ,
                                    userId: '${item.userId}',
                                    loggedInUser : '<%= session.getAttribute("userId") != null ? session.getAttribute("userId").toString() : null %>'">
                                    </div>
                                </c:forEach>
                            </div>
                            -->
                            <div dojoType="dijit.layout.TabContainer" style="width:100%" doLayout="false" widgetsInTemplate="true">
                                <div data-dojo-type="js.userBoxContainer" data-dojo-props="title:'following', user:'<%=session.getAttribute("userId")%>', loggedInUser:'<%=session.getAttribute("userId")%>'">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>