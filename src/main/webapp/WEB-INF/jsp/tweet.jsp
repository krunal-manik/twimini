<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <body>
    <h4>Tweet</h4><br>
        Hello ${sessionScope.username}  <a href="/logout">Log out</a>
        <br>
        <a href="/followed">People u r following !!! </a>
        <br>
        <a href="/all_users">All Users !!! </a>
    </body>
</html>
