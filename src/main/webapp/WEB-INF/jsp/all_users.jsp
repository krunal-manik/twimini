<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <head>
        <script type="text/javascript" src="/static/js/jquery.min.js"></script>
        <script type="text/javascript" src="/static/ejs/ejs_production.js"></script>

        <script type="text/javascript">

            function changeFollowStatus(userId) {
                alert( 'hereee' );
                var buttonName = "follow_" + userId;
                if( document.getElementById(buttonName).value == "Follow" ){
                    $.ajax({
                       type : "POST",
                       url : "/all_users/addFollowing",
                       data : "userId=" + userId ,
                       success : function() {
                           alert( 'success in add' );
                            document.getElementById(buttonName).value = "Unfollow";
                           alert( 'final success in add' );
                       }
                    });
                }
                else {
                    alert( "In remove js" );
                    $.ajax({
                       type : "POST",
                       url : "/all_users/removeFollowing",
                       data : "userId=" + userId ,
                       success : function() {
                           alert( 'success in remove' );
                            document.getElementById(buttonName).value = "Follow";
                           alert( 'final success in remove' );
                       }
                    });
                }
            }

            function load_data(data) {
                alert(data.user_id);
                var html = new EJS( {url:'/static/ejs_templates/add_new_followers.ejs'} ).render( data ) ;
                var userHTML = $(html);
                $("#user_list").append(userHTML);
            }

        </script>
    </head>

    <body>
        <div id="user_list">
            <c:forEach var='item' items='${userList}' varStatus='status'>
                <script type="text/javascript">
                    load_data({'userId':${item.userId}, 'name':'${item.name}', 'followStatus': '${item.followStatus}' });
                </script>
            </c:forEach>
        </div>
    </body>
</html>