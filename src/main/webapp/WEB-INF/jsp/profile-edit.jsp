<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <%@include file="/static/includes.html" %>
        <script>
        dojo.addOnLoad(sendform);
        function sendform() {
//            dojo.connect(dojo.byId("image_form"), "onsubmit", function(event) {
//                dojo.stopEvent(event);
//                dojo.xhrPost({
//                    form : dojo.byId("image_form"),
//                    handleAs : "text",
//                    load : function(data) {
//                       alert('hi');
//                    },
//                    error: function(data) {
//                        alert('dsadsa');
//                    }
//                });
//            });
        }
        </script>
    </head>

    <body class=" claro ">
        <%@include file="/WEB-INF/jsp/header.jsp" %>

        <div class = "container body">
            <div class="prepend-2 span-8 append-4">
                <div class="empty_box" style="height:20px; width:100%"></div>

                Update Your Profile <br><br>

                <img src="${image}" height="150" width="150"/>

                <form action="/edit_profile" method="post" enctype="multipart/form-data" style="padding-top: 10px;">
                    <input type="file" name="file" />
                    <br>
                    <div class="span-3 append-1"> Full Name </div>
                    <div class="span-4 last"> <input type="text" name="name" value= "" class="textbox" /> </div>
                    <div class="span-3 append-1"> Username </div>
                    <div class="span-4 last"> <input type="text" name="username" value="" class="textbox" /> </div>
                    <div class="span-3 append-1"> Email address </div>
                    <div class="span-4 last"> <input type="email" name="email" value = "" class="textbox" /> </div>
                    <div class="span-3 prepend-5 last"> <input type="Submit" value="SAVE" class="signup_button"/> </div>
                </form>
            </div>
            <div class="span-6 append-2 last">
                 <div class="empty_box" style="height:150px; width:100%"></div>
                 <img src="/static/images/twitter_bird.gif" />
            </div>
        </div>


    </body>
</html>