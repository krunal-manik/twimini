<div class = "prepend-2 span-8 last" style="padding:7px; padding-bottom:0px; border-width:1px; border-color:#AAAAAA; border-style:solid">
    <div class = "span-8 last sidebar">
        <div class = "padding">
            <div class="sidebar-info border">
                <div class="prepend-2 span-4 append-2 last">
                    <a href="/${currentUsername}">
                        @${currentUsername}
                        <img src='/photos/${currentUserId}.jpg' height="80" width="80"/>
                    </a>

                </div>
                <br>

                    <div class="span-2">
                        <a href="/${currentUsername}">&nbsp;&nbsp;&nbsp;&nbsp;${tweetCount}<br>Tweets</a>
                    </div>

                    <div class="span-2">
                        <a href="/${currentUsername}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${followerCount} <br>Followers</a>
                    </div>

                    <div class="span-2 last">
                        <a href="/${currentUsername}">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${followingCount}<br>Following</a>
                    </div>
                About ${currentUsername} : ${aboutCurrentUser}
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