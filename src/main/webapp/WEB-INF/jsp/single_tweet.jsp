<div class = "span-16 tweet_container">
    <div class = "span-2 usr_image_box">
        <img src="/static/images/def_user.jpg" />
    </div>
    <div class= "span-14 last tweet_box">
        <img src="/static/images/comment_arrow.png" class="comment_arrow" />
        <div class = "tweeter_name"> <%= request.getParameter("name") %> posted </div>
        <div class = "tweet_content"> <%= request.getParameter("tweet") %> </div>
        <div class = "tweet_time">at <%= request.getParameter("timestamp") %> </div>
        <div class = "span-3"><a href="#"> <img src = "/static/images/empty_star.png" />Favorite</a> </div>
        <div class = "span-3"><a href="#"><img src = "/static/images/empty_star.png" />Reply</a> </div>
        <div class = "span-3"><a href="#"><img src = "/static/images/empty_star.png" />Retweet</a> </div>
    </div>
</div>