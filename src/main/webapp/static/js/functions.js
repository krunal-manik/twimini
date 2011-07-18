function changeFollowStatus(userId) {
        var buttonName = document.getElementById("follow" + userId);
        var button_Name = document.getElementById("follow_" + userId);
        if( button_Name.value == "Follow" ){
            $.ajax({
               type : "POST",
               url : "/all_users/addFollowing",
               data : "userId=" + userId ,
               success : function() {
                    button_Name.value = "Unfollow";
                    if (buttonName != null) {
                        buttonName.value = "Unfollow";
                    }
               }
            });
        }
        else {
            $.ajax({
                type : "POST",
                url : "/all_users/removeFollowing",
                data : "userId=" + userId ,
                success : function() {
                    button_Name.value = "Follow";
                    if (buttonName != null) {
                        buttonName.value = "Follow";
                    }
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
                $("#tweetsList_o").prepend(tweetHTML.hide().fadeIn( 'slow' ) );
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

function checkPasswords( form ) {
    if( form.password.value != form.cpassword.value ) {
        alert( "Passwords dont match" );
        return false;
    }

    if( form.password.value == "" ) {
        alert( "Password cannot be blank" );
        return false;
    }
    return true;
}

function markFavorite( tweetId ) {
    $.ajax({
        type : "POST",
        url : "tweet/markFavorite",
        data : "tweetId=" + tweetId ,
        success : function(){
            document.getElementById('favorite_' + tweetId).style.backgroundImage = "url('/static/images/filled_star.png')";
        }
    });
}