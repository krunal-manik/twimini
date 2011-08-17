    <div class="header">
            <div class="container">
                <div class="empty_box"></div>
                <div class="span-5">
                    <a href="/"> <img src="/static/images/Twitter-Logo.png" style="width:130px; height:45px;"/> </a>
                </div>
                <div class="span-19 last" style="padding-top:1" >
                    <div class="span-8">
                        <form method="get" action="/search" style="padding-top: 23px;">
                            <div class="span-2" style="padding-top: 5px;">
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
                    <form action="/login" method=post >
                        <div class="span-3 append-2">
                            <label for="Username">
                                Username
                            </label>
                        </div>
                        <div class="span-3">
                            <label for="Password">
                                Password
                            </label>
                        </div>
                        <div class="span-3 last">
                            <a href="/reset_password">Forgot Password</a>
                        </div>
                        <div class="span-3 append-2">
                            <input type="text" name="username" value=""/>
                        </div>
                        <div class="span-3 append-2">
                            <input type="password" name="password" value=""/>
                        </div>
                        <div class="span-1 last">
                            <input type = "Submit" class="button" value="Login" />
                        </div>
                    </form>
                </div>
            </div>
    </div>