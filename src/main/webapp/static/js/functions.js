dojo.require("dijit.Dialog");
dojo.require("js.tweetContainer");
dojo.require("js.tweetContainerWithoutOptions");
function addslashes( str ) {
    return (str+'').replace(/([\\"'])/g, "\\$1").replace(/\0/g, "\\0");
}
function filterEscapeCharacters(str) {
    str = str.replace(/[&]/g, '&amp;');
    str = str.replace(/[<]/g, '&lt;');
    str = str.replace(/[>]/g, '&gt;');
    str = str.replace(/[']/g, '&#39;');
    str = str.replace(/["]/g, '&quot;');
    str = str.replace(/[\n]/g, '<br>');
    str = str.replace(/[ ]/g, '&nbsp;');
    return str;
}
function appendTweetsToNewsFeedContainer(data) {
    var widget = new js.tweetContainer(data);
    widget.placeAt( dojo.byId("newsFeedContainer") , "first" );
}


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



function changeFollowStatusForDivs(userId) {
    var button_Name = document.getElementById("follow_" + userId);
    if( button_Name.innerHTML == "Follow" ){
        $.ajax({
           type : "POST",
           url : "/all_users/addFollowing",
           data : "userId=" + userId ,
           success : function() {
                button_Name.innerHTML = "Unfollow";
                $('#follow_' + userId).removeClass("fav");
                $('#follow_' + userId).addClass("unfav");
           }
        });
    }
    else {
        $.ajax({
            type : "POST",
            url : "/all_users/removeFollowing",
            data : "userId=" + userId ,
            success : function() {
                button_Name.innerHTML = "Follow";
                $('#follow_' + userId).removeClass("unfav");
                $('#follow_' + userId).addClass("fav");
            }
        });
    }
}

function addTweet(){
    var tweetContent = document.getElementById("tweet").value;
    if( tweetContent == null || tweetContent.trim() == '' )return;
    if( tweetContent.length > 140 ) {
        alert( 'You cannot tweet more than 140 characters' );
        return;
    }
    dojo.xhrPost({
           url : "tweet/addTweet",
           content : {tweetContent:tweetContent},
           handleAs : 'json',
           load : function(data) {
               appendTweetsToNewsFeedContainer(data);
               document.getElementById("tweet").value = '';
           },
           error: function(data) {
               alert( 'error ' + data );
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

var tag_mode = false;
var selected_option = 0;

var keycode = {
    getKeyCode : function(e) {
        var keycode = null;
        if(window.event) {
            keycode = window.event.keyCode;
        }else if(e) {
            keycode = e.which;
        }
        return keycode;
    },
    getKeyCodeValue : function(keyCode, shiftKey) {
        shiftKey = shiftKey || false;
        var value = null;
        if(shiftKey === true) {
            value = this.modifiedByShift[keyCode];
        }else {
            value = this.keyCodeMap[keyCode];
        }
        return value;
    },
    getValueByEvent : function(e) {
        return this.getKeyCodeValue(this.getKeyCode(e), e.shiftKey);
    },
    keyCodeMap : {
        8:"backspace", 9:"tab", 13:"return", 16:"shift", 17:"ctrl", 18:"alt", 19:"pausebreak", 20:"capslock", 27:"escape", 32:" ", 33:"pageup",
        34:"pagedown", 35:"end", 36:"home", 37:"left", 38:"up", 39:"right", 40:"down", 43:"+", 44:"printscreen", 45:"insert", 46:"delete",
        48:"0", 49:"1", 50:"2", 51:"3", 52:"4", 53:"5", 54:"6", 55:"7", 56:"8", 57:"9", 59:";",
        61:"=", 65:"a", 66:"b", 67:"c", 68:"d", 69:"e", 70:"f", 71:"g", 72:"h", 73:"i", 74:"j", 75:"k", 76:"l",
        77:"m", 78:"n", 79:"o", 80:"p", 81:"q", 82:"r", 83:"s", 84:"t", 85:"u", 86:"v", 87:"w", 88:"x", 89:"y", 90:"z",
        96:"0", 97:"1", 98:"2", 99:"3", 100:"4", 101:"5", 102:"6", 103:"7", 104:"8", 105:"9",
        106: "*", 107:"+", 109:"-", 110:".", 111: "/",
        112:"f1", 113:"f2", 114:"f3", 115:"f4", 116:"f5", 117:"f6", 118:"f7", 119:"f8", 120:"f9", 121:"f10", 122:"f11", 123:"f12",
        144:"numlock", 145:"scrolllock", 186:";", 187:"=", 188:",", 189:"-", 190:".", 191:"/", 192:"`", 219:"[", 220:"\\", 221:"]", 222:"'"
    },
    modifiedByShift : {
        192:"~", 48:")", 49:"!", 50:"@", 51:"#", 52:"$", 53:"%", 54:"^", 55:"&", 56:"*", 57:"(", 109:"_", 61:"+",
        219:"{", 221:"}", 220:"|", 59:":", 222:"\"", 188:"<", 189:">", 191:"?",
        96:"insert", 97:"end", 98:"down", 99:"pagedown", 100:"left", 102:"right", 103:"home", 104:"up", 105:"pageup"
    }
};

function crt_pos (ctrl) {
	var CaretPos = 0;	// IE Support
	if (document.selection) {
	ctrl.focus ();
		var Sel = document.selection.createRange ();
		Sel.moveStart ('character', -ctrl.value.length);
		CaretPos = Sel.text.length;
	}
	// Firefox support
	else if (ctrl.selectionStart || ctrl.selectionStart == '0')
		CaretPos = ctrl.selectionStart;
	return (CaretPos);
};

function givesuggestions(Event, Object, TagBox) {

    if (tag_mode) {
        var to_search = Object.value.substring(0, crt_pos(Object)).split('@')[Object.value.substring(0, crt_pos(Object)).split('@').length - 1];
        $.ajax({
            type : "POST",
            url : "search_user",
            data : "pattern=" + to_search ,
            success : function( data ){
                var sel = TagBox;
                sel.innerHTML = "";
                if (selected_option === -1) {
                    selected_option = data.length - 1;
                } else if (selected_option === data.length) {
                    selected_option = 0;
                }
                for (var i = 0; i < data.length; i++) {
                    var c = dojo.doc.createElement('option');
                    var arr = JSON.parse ( JSON.stringify(data[i]) );
                    c.innerHTML = arr.username;
                    c.value = i;
                    if (i === (selected_option)) {
                        c.selected = true;
                    }
                    sel.appendChild(c);
                }
            }
        });
    }
}


/*
function search(Event, Object) {

    dojo.require("dijit.form.MultiSelect");

    document.getElementById("search_dropdown").style.display = "block";
    var to_search = Object.value;
    $.ajax({
        type : "POST",
        url : "search_user",
        data : "pattern=" + to_search ,
        success : function( data ){
            var sel = dojo.byId('search_dropdown');
            sel.innerHTML = "";
            for (var i = 0; i < data.length; i++) {
                var c = dojo.doc.createElement('option');
                var arr = JSON.parse ( JSON.stringify(data[i]) );
                c.innerHTML = arr.username;
                c.value = i;
                sel.appendChild(c);
            }
            new dijit.form.MultiSelect({
                name: 'dynamic'
            },
            sel);
        }
    });
}
*/

function imposeMaxLength(Event, Object, MaxLen, TagBox) {
        if  (String.fromCharCode(Event.which) == "@") {
            selected_option = 0;
            tag_mode = true;
            TagBox.style.display = "block";
        } else if (keycode.getKeyCode(Event)==13) {
            tag_mode = false;
            var to_change = TagBox.options[TagBox.selectedIndex].innerHTML;
            Object.value =
                Object.value.substring(0, crt_pos(Object)).substring(0, Object.value.substring(0, crt_pos(Object)).lastIndexOf('@') + 1) + to_change + Object.value.substring(crt_pos(Object), Object.value.length);
            TagBox.style.display = "none";
            selected_option = 0;
            return false;
        } else if (keycode.getKeyCode(Event)==32) {
            tag_mode = false;
            TagBox.style.display = "none";
            selected_option = 0;
        } else if (Event.keyCode==38) {
            selected_option--;
        } else if (Event.keyCode==40) {
            selected_option++;
        }
        if (Event.keyCode==40||Event.keyCode==38) {
            return false;
        }
        return (Object.value.length <= MaxLen)||(Event.keyCode == 8 ||Event.keyCode==46||(Event.keyCode>=35&&Event.keyCode<=40));
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


function loadContactImporter() {
    alert( 'here' );
    win = window.open( ["https://accounts.google.com/o/oauth2/auth?client_id=205184315336.apps.googleusercontent.com&redirect_uri=http://localhost:8080/gmail&response_type=token&scope=https://www.google.com/m8/feeds/"],
            "Import Your Gmail Contacts",
            "width=400,height=450,resizable=0,dependent=0,top=150,left=450" );
    win.onunload = function() {
        win.opener.location.href = "/krunalmanik";
    }
}

function reply(tweetId) {
    dijit.byId("replyPopUp").attr("title", "Reply to Tweet ID " + tweetId);
    var content =
    "<div style = 'position:relative'>" +
        "<textarea id = 'reply' onkeypress='return imposeMaxLength(event, this, 140, dojo.byId(\"tagging_dropdown_dialog\"));' " +
        "name = 'tweetContent' value = '' class='span-16' placeholder='tweet !!!'" +
        "style='resize:none; height:60px;' onkeyup = 'givesuggestions(event, this, dojo.byId(\"tagging_dropdown_dialog\"));'></textarea>" +
        "<select id='tagging_dropdown_dialog' style='display:none' class='dropdown_select'> </select>" +
    "</div> " +
    "<input type = 'button' value = 'Reply' onclick='replyToTweet( "+ tweetId + ")'/>";
    dijit.byId("replyPopUp").attr("content", content);
    dijit.byId("replyPopUp").show();
}

function replyToTweet(replyTo) {
    var reply = dojo.byId('reply');
    var tweetContent = reply.value;
    if( tweetContent == null || tweetContent.trim() == '' )return;
    if( tweetContent.length > 140 ) {
        alert( 'You cannot tweet more than 140 characters' );
        return;
    }

    $.ajax({
           type : "POST",
           url : "tweet/replyToTweet",
           data : { "tweetContent" : tweetContent ,  "replyTo" : replyTo } ,
           success : function( data ){
                prependTweet_o(data);
               dijit.byId("replyPopUp").hide();
           }
    });
}
