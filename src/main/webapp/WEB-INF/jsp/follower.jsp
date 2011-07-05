<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <body>
        <ul>
            <c:forEach var='item' items='${followerList}'>
                <li>
                    <a href="/profile?userId=${item.userId}">${item.name}</a>
                </li>
            </c:forEach>
        </ul>
    </body>
</html>