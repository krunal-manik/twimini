<div class = "span-8 last sidebar">
    <div class = "padding">
        <ul class = "border">
            <div class = "tweeter_name"> <a href = "/${currentUsername}/followers"> FOLLOWERS (${followerCount}): </a> </div>
            <c:forEach var='item' items='${followerList}'>
                <li> <a href = "/${item.username}"> ${item.name} </a> </li>
            </c:forEach>
        </ul>
        <ul class = "border">
            <div class = "tweeter_name"><a href = "/${currentUsername}/followings"> FOLLOWING (${followingCount}): </a> </div>
            <c:forEach var='item' items='${followedList}'>
                <li> <a href = "/${item.username}"> ${item.name}  </a> </li>
            </c:forEach>
        </ul>

        <% if( session.getAttribute("username") != null ) { %>

            <ul class = "border">
                <div class = "tweeter_name"> ALL USERS: </div>
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