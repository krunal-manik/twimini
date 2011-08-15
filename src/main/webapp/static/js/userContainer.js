dojo.provide("js.userContainer");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");
dojo.require("dijit.Dialog");

dojo.declare("js.userContainer",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
            },
            templateString : dojo.cache( "js/widgets", "userContainer.html"),
            postCreate : function() {
                this.inherited(arguments);
                var domNode = this;

                dojo.connect( this.followNode , "onmouseover" , function() {
                    if( domNode.args.followStatus == 'Following' ) {
                        domNode.followNode.innerHTML = 'Unfollow';
                    }

                });
                dojo.connect( this.followNode , "onmouseout" , function() {
                    if( domNode.args.followStatus == 'Following' ) {
                        domNode.followNode.innerHTML = 'Following';
                    }
                });
                dojo.connect( this.followNode , 'onclick' , function() {
                   if( domNode.args.loggedInUser != 'null' )
                        domNode._changeFollowStatus(domNode);
                   else
                    alert('You arent logged in');
                });
            },
            getAttribute : function() {
                return this.args;
            },
            appendChild : function() {
                alert(' in append child here');
                dojo.parser.parse(this);
            },
            getSelf : function() {
                return this;
            },
            _changeFollowStatus : function(domNode) {
                var urlString = "/all_users/addFollowing";
                if( domNode.args.followStatus == 'Following' ) {
                    urlString = "/all_users/removeFollowing";
                }
                dojo.xhrPost({
                    url: urlString,
                    content : {userId : domNode.args.userId},
                    handleAs: 'json',
                    load: function(data) {
                        alert( domNode.args.followStatus );
                        if( domNode.args.followStatus == 'Following' ) {
                            domNode.args.followStatus = 'Follow';
                            domNode.followNode.innerHTML = 'Follow';
                        }
                        else {
                            domNode.args.followStatus = 'Following';
                        }
                    },
                    error: function(data) {
                        alert( 'error in follow unfollow' );
                    }
                });
            }
        }
);
