<div class = "span-8 last sidebar">
    <div class = "padding">
        <ul class = "border">
            <div class = "tweeter_name"> FOLLOWERS : </side_heading>
            <c:forEach var='item' items='${followerList}'>
                <li> <a href = "/${item.username}"> ${item.name} </a> </li>
            </c:forEach>
        </ul>
        <ul class = "border">
            <div class = "tweeter_name"> U ARE FOLLOWING : </div>
            <c:forEach var='item' items='${followedList}'>
                <li> <a href = "/${item.username}"> ${item.name}  </a> </li>
            </c:forEach>
        </ul>


        <ul class = "border">
            <div class = "tweeter_name"> ALL USERS: </div>
            <c:forEach var='item' items='${allUserList}'>
                <li>
                    <a href = "/${item.username}"> ${item.name} </a>
                    <input type = "button" id = 'follow_${item.userId}' class = "btn border"
                    value = ${item.followStatus} onclick='changeFollowStatus(${item.userId})' />
                </li>

            </c:forEach>
        </ul>



    </div>
</div>