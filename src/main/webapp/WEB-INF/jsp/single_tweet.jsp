<div class = "span-16 tweet_container">
    <div class = "span-2 usr_image_box">
        <img src="/static/images/def_user.jpg" style="border-color:#000000; border-width:2px; border-style:solid; width:50px; height:50px"/>
    </div>
    <div class= "span-14 last tweet_box">
        <img src="/static/images/comment_arrow.png" class="comment_arrow" />
        <div class="padding">
            <div class = "tweeter_name"> <a href = '/<%= request.getParameter("username") %>'> <%= request.getParameter("name") %> </a> posted </div>
            <div class = "tweet_content"> <%= request.getParameter("tweet") %> </div>
            <div class = "tweet_time">at <%= request.getParameter("timestamp") %> </div>
            <div style="width:100%; height:25px" class = "buttons_tab">

                <% if (request.getParameter("favorite").equals("true")) {%>
                    <div id = 'favorite_<%= request.getParameter("tweetId") %>' class = "i unfav span-3" onclick = 'toggleFavorite(<%= request.getParameter("tweetId") %>)'>Unfavorite</div>
                <% } else { %>
                   <div id = 'favorite_<%= request.getParameter("tweetId") %>' class = "i fav span-3" onclick = 'toggleFavorite(<%= request.getParameter("tweetId") %>)'>Favorite</div>
                <% } %>
                <div class = "span-3"><i> </i>  Reply</a> </div>
                <div class = "span-3"><i> </i>  Retweet</a> </div>
            </div>
        </div>
    </div>
</div>