    <div class="header">
            <div class="container">
                <div class="empty_box"></div>
                <div class="span-5 append-8">
                    <img src="/static/images/Twitter-Logo.png" style="width:130; height:45;"/>
                </div>
                <div class="span-11 last" style="padding-top:1" >
                    <div class="span-2">
                        <a href="#">Hello <%= session.getAttribute("username") %></a>
                    </div>
                    <div class="span-2 prepend-1">
                        <a href="#">Home</a>
                    </div>
                    <div class="span-2 prepend-1">
                        <a href="#">Profile</a>
                    </div>
                    <div class="span-2 prepend-1 last">
                        <a href="/logout">Logout</a>
                    </div>
                </div>
            </div>
    </div>