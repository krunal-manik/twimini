    <div class="header">
            <div class="container">
                <div class="empty_box"></div>
                <div class="span-5 append-7">
                    <img src="/static/images/Twitter-Logo.png" style="width:130; height:45;"/>
                </div>
                <div class="span-12 last" style="padding-top:6;" >
                    <div class="span-3">
                        <a href="#">Hello <%= session.getAttribute("username") %></a>
                    </div>
                    <div class="span-2 prepend-1">
                        <a href="#"> <img src="/static/images/home.png"/> Home</a>
                    </div>
                    <div class="span-2 prepend-1">
                        <a href="#"> <img src="/static/images/profile.png"> Profile</a>
                    </div>
                    <div class="span-2 prepend-1 last">
                        <a href="/logout">Logout</a>
                    </div>
                </div>
            </div>
    </div>