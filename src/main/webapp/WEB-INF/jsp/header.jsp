<div class="header">
    <div class="container">
        <div class="empty_box"></div>
        <div class="span-5">
            <a href="/"><img src="/static/images/Twitter-Logo.png" style="width:130; height:45;"/></a>
        </div>
        <div class="span-19 last" style="padding-top:6;" >
            <div class="span-10">
                <form method="get" action="/search">
                    <div class="span-2 prepend-2">
                        SEARCH
                    </div>
                    <div class="span-6 last" style="position:relative">
                        <input  type="text" name="pattern" id = "pattern"/>
                        <!--select id="search_dropdown" style="display:none" class="dropdown_select"> </select-->
                    </div>
                </form>
            </div>
            <div class="span-2 prepend-1">
                <a href="/"> <img src="/static/images/home.png"/> Home</a>
            </div>
            <div class="span-2 prepend-1">
                <a href="/<%= session.getAttribute("username") %>"> <img src="/static/images/profile.png"> Profile</a>
            </div>
            <div class="span-2 prepend-1 last">
                <a href="/logout">Logout</a>
            </div>
        </div>
    </div>
</div>