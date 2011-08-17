<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <%@include file="/static/includes.html" %>
        <script type="text/javascript">
            if (location.hash) {
                var selected = location.hash.split("/")[1];
            }
            dojo.require("dijit.form.ComboBox");
            dojo.require("dojo.data.ItemFileReadStore");
            dojo.require("dijit.form.MultiSelect");
            dojo.require("dijit.Dialog");
            dojo.require("dijit.layout.TabContainer");
            dojo.require("dijit.layout.ContentPane");
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
                if (selected) {
                    dijit.byId("tabContainer").selectChild(dijit.byId(selected));
                }

                var peopleStore = new dojo.data.ItemFileReadStore({
                    url: "/first_search?pattern=a&follower=2"
                });
                var filteringSelect = new dijit.form.ComboBox({
                    id: "peopleSelect",
                    name: "people",
                    value: "",
                    store: peopleStore,
                    searchAttr: "username"
                },
                "peopleSelect");
            });
        </script>

    </head>

    <body class=" claro ">
        <div id="replyPopUp" dojoType="dijit.Dialog">
        </div>

        <%@include file="/WEB-INF/jsp/header.jsp" %>

        <div class = "body">
        <div class = "container">
            <div class = "span-24">
                <div class = "span-16">
                        <form name = "tweet_form">
                            <div style = "position:relative">
                                <textarea id = "tweet" onkeypress ="return imposeMaxLength(event, this, 140, dojo.byId('tagging_dropdown'));"
                                name = "tweetContent" value = "" class="prepend-1 span-15" placeholder="tweet !!!"
                                style="resize:none; height:60px;"
                                onkeydown="suggestionDivChange(event, this, dojo.byId('tagging_dropdown'))"
                                onkeyup = "givesuggestions(event, this, dojo.byId('tagging_dropdown'));"></textarea>
                                <!--div id= "tagging_dropdown" class = "dropdown_box border" style = "display:none"></div-->

                                <select id="tagging_dropdown" style="display:none" class="dropdown_select"> </select>

                            </div>
                            <input type = "button" value = "Tweet" onclick="addTweet()"/>
                        </form>
                    <div id="tabContainer" dojoType="dijit.layout.TabContainer" data-dojo-props="id:'tabContainer'" style="width:100%" doLayout="false" widgetsInTemplate="true">

                        <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id:'newsfeed', href:'#!/newsfeed', title:'newsfeed', user:'<%=session.getAttribute("userId")%>', favoriter:'<%=session.getAttribute("userId")%>'">
                        </div>
                        <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id: 'mentions', href:'#!/mentions', title:'mentions', user:'<%=session.getAttribute("userId")%>', favoriter:'<%=session.getAttribute("userId")%>'">
                        </div>
                        <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id: 'favorites', href:'#!/favorites', title:'favorites', user:'<%=session.getAttribute("userId")%>', favoriter:'<%=session.getAttribute("userId")%>'">
                        </div>
                        <div data-dojo-type="js.tweetBoxContainer" data-dojo-props="id: 'retweets', href:'#!/retweets', title:'retweets', user:'<%=session.getAttribute("userId")%>', favoriter:'<%=session.getAttribute("userId")%>'">
                        </div>
                        <div data-dojo-type="js.userBoxContainer"  data-dojo-props="id: 'follower', href:'#!/follower', title:'follower', user:'<%=session.getAttribute("userId")%>', loggedInUser:'<%=session.getAttribute("userId")%>'">
                        </div>
                        <div data-dojo-type="js.userBoxContainer" data-dojo-props="id: 'following', href:'#!/following', title:'following', user:'<%=session.getAttribute("userId")%>', loggedInUser:'<%=session.getAttribute("userId")%>'">
                        </div>

                    </div>
                </div>

                <%@ include file="sidebar.jsp" %>

            </div>
        </div>
        </div>


    </body>
</html>