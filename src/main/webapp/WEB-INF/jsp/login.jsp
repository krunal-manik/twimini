<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
    <body>
    <h4>Login</h4><br>
    ${message}
    <form action = "/login" method=post>
        <table>
            <tr>
                <td> Username : </td>
                <td> <input type = "text" name = "username" value = "" /> </td>
            </tr>
            <tr>
                <td> Password : </td>
                <td> <input type = "password" name = "password" value = "" /> </td>
            </tr>
            <tr><input type = "submit" /></tr>
        </table>
    </form>
    Not a user ? <a href="/register">Sign up here</a>

    </body>
</html>
