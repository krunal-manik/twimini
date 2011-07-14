function changeFollowStatus(userId) {
        var buttonName = "follow_" + userId;
        if( document.getElementById(buttonName).value == "Follow" ){
            $.ajax({
               type : "POST",
               url : "/all_users/addFollowing",
               data : "userId=" + userId ,
               success : function() {
                    document.getElementById(buttonName).value = "Unfollow";
               }
            });
        }
        else {
            $.ajax({
               type : "POST",
               url : "/all_users/removeFollowing",
               data : "userId=" + userId ,
               success : function() {
                   alert( 'success in remove' );
                   document.getElementById(buttonName).value = "Follow";
                   alert( 'final success in remove' );
               }
            });
        }
    }

function addTweet(){
    var tweetContent = document.getElementById("tweet").value;
    if( tweetContent == null || tweetContent.trim() == '' )return;
    if( tweetContent.length > 140 )alert( 'You cannot tweet more than 140 characters' );
    $.ajax({
           type : "POST",
           url : "tweet/addTweet",
           data : "tweetContent=" + tweetContent ,
           success : function( data ){
               document.getElementById("tweet").value = "";
                var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
                var tweetHTML = $(html);
                $("#tweetsList").prepend(tweetHTML.hide().fadeIn( 'slow' ) );
           }
    });
}
function prependTweet(data){
    var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
    var tweetHTML = $(html);
    $("#tweetsList").prepend(tweetHTML);
}
function toggle_show() {
    $("#userTweetsContainer").toggle();
    $("#newsFeedContainer").toggle();
}
function prependTweet_o(data){
    var html = new EJS( {url:'/static/ejs_templates/tweet.ejs'} ).render( data ) ;
    var tweetHTML = $(html);
    $("#tweetsList_o").prepend(tweetHTML);
}

function imposeMaxLength(Event, Object, MaxLen) {
        return (Object.value.length <= MaxLen)||(Event.keyCode == 8 ||Event.keyCode==46||(Event.keyCode>=35&&Event.keyCode<=40))
}
