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
            <div class="prepend-2 span-13 append-9 last">
                <div class="empty_box" style="height:20px; width:100%"></div>

                Update Your Profile <br><br>

                <img src="${image}" height="150" width="150"/>

                <form action="/edit_profile" method="post" enctype="multipart/form-data" style="padding-top: 10px;">
                    <input type="file" name="file" />
                    <br>
                    <div class="span-3 append-1">
                        <label for="Full Name">
                            Full Name
                        </label>
                    </div>
                    <div class="span-5">
                        <input type="text" name="name" value="${name}" class="textbox" />
                    </div>
                    <div class="span-4 last error-field">
                        ${nameError}
                    </div>
                   <div class="span-3 append-1">
                       <label for="Username">
                            Username
                        </label>
                   </div>
                   <div class="span-5">
                        <input type="text" name="username" value="${username}" class="textbox" />
                   </div>
                    <div class="span-4 last error-field">
                        ${usernameError}
                    </div>

                   <div class="span-3 append-1">
                        <label for="aboutMe">
                            About Me
                        </label>
                   </div>
                   <div class="span-5">
                        <textarea rows="4" cols="10" name="aboutMe">${aboutMe}</textarea>
                   </div>
                    <div class="span-4 last">
                        ${aboutMeError}
                    </div>
                   <div class="span-3 prepend-5 last">
                        <input type="Submit" value="SAVE" class="signup_button"/>
                   </div>
                </form>
            </div>
        </div>
    </body>
</html>