<!DOCTYPE HTML>
<html>
    <head>
    <%@include file="/static/includes.txt" %>
    </head>

    <body>
    <div class="header">
            <div class="container">

                <div class="span-5 append-8">
                    <img src="/static/images/Twitter-Logo.png" style="width:130px; height:45px;"/>
                </div>
                <div class="span-11 last" style="padding-top:1" >
                    <form action="/login" method=post >
                        <div class="span-3 append-2"> Email </div>
                        <div class="span-6 last"> Password </div>
                        <div class="span-3 append-2"> <input type="text" /> </div>
                        <div class="span-3 append-2"> <input type="text"> </div>
                        <div class="span-1 last"> <input type = "Submit" class="button" value="Login" /> </div>
                    </form>
                </div>
            </div>

    </div>
    <div class="container body">
        <div class="span-6 append-2">
             <div class="empty_box" style="height:150px; width=100%"></div>
             <img src="/static/images/twitter_bird.gif" />
        </div>
     <div class="prepend-8 span-8 last">
            <div class="empty_box" style="height:100px; width=100%"></div>
            Want to Tweet? Join us.<br>
            <form action="/register" method="post" style="padding-top: 10px;">
                <div class="span-3 append-1"> Full Name </div>
                <div class="span-4 last"> <input type="text" class="textbox" /> </div>
                <div class="span-3 append-1"> Username </div>
                <div class="span-4 last"> <input type="text" class="textbox" /> </div>
                <div class="span-3 append-1"> Email address </div>
                <div class="span-4 last"> <input type="text" class="textbox" /> </div>
                <div class="span-3 append-1"> Password </div>
                <div class="span-4 last"> <input type="text" class="textbox" /> </div>
                <div class="span-3 append-1"> Confirm password </div>
                <div class="span-4 last"> <input type="text" class="textbox" /> </div>
                <div class="span-3 prepend-5 last"> <input type="Submit" value="Sign up" class="signup_button"/> </div>
            </form>
        </div>
    </div>

      <div class="container footer">
          <div class="span-24-last">
                <a href="#">About Us</a> |
                <a href="#">Contact Us</a> |
                <a href="#">Terms of Service</a> |
                <a href="#">Privacy Policy</a>
          </div>
      </div>
    </body>
</html>