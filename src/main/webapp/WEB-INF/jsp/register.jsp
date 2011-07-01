<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>

    <head>
        <script type="text/javascript">
            function validate(form){
                if( form.password.value != form.password2.value ){
                    alert( "Passwords don't match" );
                    return false;
                }
                return true;
            }
        </script>
    </head>

    <body>
    <h6>Sign up/Register</h6>

    <form action="/register" method=post>
        <table>
            <tr>
                <td> Name : </td>
                <td> <input type = "text" name = "name" value = "" /> </td>
            </tr>
            <tr>
                <td> Name : </td>
                <td> <input type = "text" name = "username" value = "" /> </td>
            </tr>
            <tr>
                <td> Email : </td>
                <td> <input type = "text" name = "email" value = "" /> </td>
            </tr>
            <tr>
                <td> Password : </td>
                <td> <input type = "password" name = "password" value = "" /> </td>
            </tr>
            <tr>
                <td> Confirm Password : </td>
                <td> <input type = "password" name = "password2" value = "" /> </td>
            </tr>
            <tr><input type = "submit" /></tr>
        </table>
    </form>

    </body>
</html>
