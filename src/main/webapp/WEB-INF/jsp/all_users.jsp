<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

    <head>

        <script type="text/javascript" src="/static/js/jquery.min.js"></script>
        <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>

        <script type="text/javascript">

            function addFollower(id) {
                $.ajax({
                   type : "POST",
                   url : "/all_users",
                   data : "userId=" + id,
                   success : function() {
                    $('#abc'+id).remove();
                    }
                });
            };

            function load_data(data) {
                alert(data.user_id);
                var html = new EJS( {url:'/static/ejs_templates/add_new_followers.ejs'} ).render( data ) ;
                var userHTML = $(html);
                $("#user_list").append(userHTML);
            }

        </script>
    </head>

    <body>

        <ul id="user_list">

            <c:forEach var='item' items='${userList}' varStatus='status'>

                <script type="text/javascript">
                    load_data({'user_id':${item.user_id}, 'name':'${item.name}'});
                </script>

            </c:forEach>

        </ul>

    </body>

</html>