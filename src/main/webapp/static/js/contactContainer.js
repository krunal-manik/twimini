dojo.provide("js.contactContainer");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");

dojo.declare("js.contactContainer",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
            },
            templateString : dojo.cache( "js/widgets", "contactContainer.html"),
            postCreate : function() {
                this.inherited(arguments);
                var domNode = this;
                dojo.connect( this.statusNode , "onmouseover" , function() {
                    if( domNode.args.status == 'Following' ) {
                        domNode.statusNode.innerHTML = 'Unfollow';
                    }
                    else if( domNode.args.status == 'Marked For Invite' ) {
                        domNode.statusNode.innerHTML = 'Cancel Invite';
                    }
                    else if( domNode.args.status == 'Invite' ) {
                        domNode.statusNode.innerHTML = 'Invite';
                    }
                });
                dojo.connect( this.statusNode , "onmouseout" , function() {
                    if( domNode.args.status == 'Following' ) {
                        domNode.statusNode.innerHTML = 'Following';
                    }
                    else if( domNode.args.status == 'Marked For Invite' ) {
                        domNode.statusNode.innerHTML = 'Marked For Invite';
                    }
                    else if( domNode.args.status == 'Invite' ) {
                        domNode.statusNode.innerHTML = 'Invite';
                    }
                });
                dojo.connect( this.statusNode , 'onclick' , function() {
                   if( domNode.args.status == 'Follow' || domNode.args.status == 'Following' ) {
                        domNode._changeFollowStatus(domNode);
                   }
                   else {
                       alert(domNode.args.status);
                       domNode._changeInviteStatus(domNode);
                   }

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
                if( domNode.args.status == 'Following' ) {
                    urlString = "/all_users/removeFollowing";
                }
                dojo.xhrPost({
                    url: urlString,
                    content : {userId : domNode.args.userId},
                    handleAs: 'json',
                    load: function(data) {
                        if( domNode.args.status == 'Following' ) {
                            domNode.args.status = 'Follow';
                            domNode.statusNode.innerHTML = 'Follow';
                        }
                        else {
                            domNode.args.status = 'Following';
                        }
                    },
                    error: function(data) {
                        alert( 'error in follow unfollow' );
                    }
                });
            },
            _changeInviteStatus : function(domNode) {
                if( domNode.args.status == 'Marked For Invite' ) {
                    domNode.args.status = 'Invite';
                    domNode.args.statusNode.innerHTML = 'Invite';
                }
                else {
                    domNode.args.status = 'Marked For Invite';
                    domNode.args.statusNode.innerHTML = 'Marked For Invite';
                }
            }
        }
);
