<html>
    <head>
        <link rel="stylesheet" type="text/css" href="/static/css/basestyle.css" />
    </head>

    <body>
        <div class="header">
            <div class="banner">
                <img src="/static/images/tweet_banner.jpg" style="width:130; height:45;"/>
            </div>
            <div class="input">
                <table>
                     <form action="/login" method=post>
                        <tr>
                            <td>
                                Login :
                            </td>
                            <td>
                                Password :
                            </td>
                            <td colspan="2">
                                <input type="submit" value="login">
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <input type="text" class="input">
                            </td>
                            <td>
                                <input type="passsword" class="input">
                            </td>
                        </tr>
                     </form>
                <table>
            </div>
        </div>

        <div class="sign_up">
            <form action="/register" method=post>
                <fieldset>
                    <legend> SIGN UP</legend>
                        <table>
                        <tr>
                            <td class="label_sup"> Name : </td>
                            <td> <input type = "text" name = "name" value = "" /> </td>
                        </tr>
                        <tr>
                            <td class="label_sup"> UserName : </td>
                            <td> <input type = "text" name = "username" value = "" /> </td>
                        </tr>
                        <tr>
                            <td class="label_sup"> Email : </td>
                            <td> <input type = "text" name = "email" value = "" /> </td>
                        </tr>
                        <tr>
                            <td class="label_sup"> Password : </td>
                            <td> <input type = "password" name = "password" value = "" /> </td>
                        </tr>
                        <tr>
                            <td class="label_sup"> Confirm Password : </td>
                            <td> <input type = "password" name = "password2" value = "" /> </td>
                        </tr>
                        <tr><input type = "submit" /></tr>
                    </table>
                </fieldset>
            </form>
        </div>
    </body>
</html>