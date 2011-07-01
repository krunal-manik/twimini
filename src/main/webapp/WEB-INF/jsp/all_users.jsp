<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

    <body>

        <ul>

            <c:forEach var='item' items='${userList}' varStatus='status'>

                <li>
                    <form action = "/all_users" method = "post">
                        <input type = "text" name="name" value = ${item.name} />
                        <input type = "hidden" name="userId" value = ${item.user_id} />
                        <input type = "submit" value = "Follow" />
                    </form>
                </li>

            </c:forEach>

        </ul>

    </body>

</html>