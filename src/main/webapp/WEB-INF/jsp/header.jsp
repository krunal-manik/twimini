<div class="header">
    <div class="container">
        <div class="empty_box"></div>
        <div class="span-5">
            <a href="/"><img src="/static/images/Twitter-Logo.png" style="width:130; height:45;"/></a>
        </div>
        <div class="span-19 last" style="padding-top:6;" >
            <div class="span-10">
                <div class="span-8">
                    <div class="span-8 last" style="height:20px"></div>
                    <form method="get" action="/search">
                        <div class="span-2">
                            SEARCH
                        </div>
                        <div class="span-6 last" style="position:relative">
                            <!--input id="peopleSelect"-->
                            <input  type="text" autocomplete="off" name="pattern" id = "pattern" onmouseup="clear_search()" onkeyup="show_search(<%= session.getAttribute("userId") %>)"/>
                            <div id="search_dropdown" class="span-4" style="z-index: 25; position:absolute; top:30px"></div>
                            <!--select id="search_dropdown" style="display:none" class="dropdown_select"> </select-->
                        </div>
                    </form>
                </div>
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