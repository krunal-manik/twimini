function return_followers_onpane() {
    document.getElementById("sidepane").innerHTML = '';
    $.ajax({
           type : "GET",
           url : "follower_onpane",
           success : function( data ){
                for(i=0;i<data.length;i++){
                    arr = JSON.parse( JSON.stringify(data[i]));
                    var html = new EJS( {url:'/static/ejs_templates/follower.ejs'} ).render(arr);
                    var todoItemList = $(html);
                    $("#sidepane").append(todoItemList);
                }
           }
    });
}

function return_followings_onpane() {
    document.getElementById("sidepane").innerHTML = '';
    $.ajax({
           type : "GET",
           url : "following_onpane",
           success : function( data ){
                for(i=0;i<data.length;i++){
                    arr = JSON.parse( JSON.stringify(data[i]));
                    var html = new EJS( {url:'/static/ejs_templates/follower.ejs'} ).render(arr);
                    var todoItemList = $(html);
                    $("#sidepane").append(todoItemList);
                }
           }
    });
}

function all_users_onpane() {
    document.getElementById("sidepane").innerHTML = '';
    $.ajax({
           type : "GET",
           url : "all_users_onpane",
           success : function( data ){
                for(i=0;i<data.length;i++){
                    arr = JSON.parse( JSON.stringify(data[i]));
                    var html = new EJS( {url:'/static/ejs_templates/add_new_followers.ejs'} ).render(arr);
                    var todoItemList = $(html);
                    $("#sidepane").append(todoItemList);
                }
           }
    });
}

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
