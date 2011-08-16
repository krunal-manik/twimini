<div class = "span-7 last" style="padding:7px; padding-bottom:0px; border-width:1px; border-color:#AAAAAA; border-style:solid">
    <div class = "span-7 last sidebar">
        <div class="padding">
            <div class="sidebar-info border">
                <div class="span-6 last">

                    <div class="span-3" >
                        <a href="/${currentUsername}">
                            <div  style="padding-left:20px; padding-top:10px">
                                <img src='/photos/${currentUserId}.jpg' class="span-2" height="70" width="70"/>
                                <span class="span-2 as-link" style="text-align:center">@${currentUsername}</span>
                            </div>
                        </a>
                    </div>

                    <div class="span-3 last" style="text-align: center">
                        <div class="span-3 last" style="height:30px"></div>
                        <a class="as-link" href="/${currentUsername}">${tweetCount}<br>Tweets</a>
                    </div>
                </div>

                <div class="span-6 last">

                    <div class="span-3" style="text-align:center">
                        <div class="span-3 last" style="height:30px"></div>
                        <a class="as-link" href="/${currentUsername}">${followerCount}<br>Followers</a>
                    </div>

                    <div class="span-3 last" style="text-align:center">
                        <div class="span-3 last" style="height:30px"></div>
                        <a class="as-link" href="/${currentUsername}">${followingCount}<br>Following</a>
                    </div>

                </div>

                    <div class="span-6 last padding">
                        About ${currentUsername} : ${aboutCurrentUser}
                    </div>
            </div>
            <br>
            <ul class = "border">
                <div class = "tweeter-name"> <a href = "/${currentUsername}/followers"> FOLLOWERS (${followerCount}): </a> </div>
                <c:forEach var='item' items='${followerList}'>
                    <li> <a href = "/${item.username}"><img src='/photos/${item.userId}.jpg' width='20' height = '20'/> ${item.name}</a> </li>
                </c:forEach>
            </ul>
            <ul class = "border">
                <div class = "tweeter-name"><a href = "/${currentUsername}/followings"> FOLLOWING (${followingCount}): </a> </div>
                <c:forEach var='item' items='${followedList}'>
                    <li> <a href = "/${item.username}"><img src='/photos/${item.userId}.jpg' width='20' height = '20'/> ${item.name}</a> </li>
                </c:forEach>
            </ul>

            <% if( session.getAttribute("username") != null ) { %>

                <ul class = "border">
                    <div class = "tweeter-name"> ALL USERS: </div>
                    <c:forEach var='item' items='${allUserList}'>
                        <li>
                            <a href = "/${item.username}"> ${item.name} </a>
                            <input type = "button" id = 'follow_${item.userId}' class = "btn border"
                            value = ${item.followStatus} onclick="changeFollowStatus(${item.userId})" />
                        </li>

                    </c:forEach>
                </ul>
            <% } %>

        </div>
    </div>
</div>