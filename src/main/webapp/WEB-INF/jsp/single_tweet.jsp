<div class = "span-16 tweet_container">
    <div class = "span-2 usr_image_box">
        <img src="/static/images/def_user.jpg" />
    </div>
    <div class= "span-14 last tweet_box">
        <img src="/static/images/comment_arrow.png" class="comment_arrow">
        tweet id : <%= request.getParameter("tweetId") %><br>
        tweet: <%= request.getParameter("tweet") %><br>
        user id: <%= request.getParameter("tweetedBy") %><br>
        timestamp<%= request.getParameter("timestamp") %><br>
    </div>
</div>