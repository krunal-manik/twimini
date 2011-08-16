dojo.provide("js.tweetContainer");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");
dojo.require("dijit.Dialog");

dojo.declare("js.tweetContainer",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
            },
            templateString : dojo.cache( "js/widgets", "tweetContainer.html"),
            postCreate : function() {
                this.inherited(arguments);
                this.args.timestamp = getISO8601formattedTime(this.args.timestamp);
                this.timestampNode.innerHTML = this.args.timestamp;
                this.args.tweetOptions = this.args.tweetOptions.toString();
                this.args.isFavorite = this.args.isFavorite.toString();
                this.tweetNode.innerHTML = addTags(filterEscapeCharacters(this.args.tweet));
                var domNode = this;
                if (this.args.retweetedBy == '0') {
                    this.whyHereNode.innerHTML = '';
                } else {
                    this.whyHereNode.innerHTML = "retweeted by <a href='/" + this.args.retweetedBy + "' >" + this.args.retweetedBy + '</a>';
                }
                if( this.args.tweetOptions == 'true' ) {
                    if( this.args.isFavorite ==  'false' ) {
                        dojo.addClass( this.favoriteNode , "favorite" );
                        this.favoriteNode.innerHTML = "Favorite";
                    }
                    else {
                        dojo.addClass( this.favoriteNode , "unfavorite" );
                        this.favoriteNode.innerHTML = "Unfavorite";
                    }
                    dojo.connect( this.favoriteNode , "onclick" , function() {
                        domNode._toggleFavorite(domNode);
                    });
                    dojo.connect( this.replyNode , "onclick" , function() {
                        reply(domNode.args.tweetId);
                    });
                    dojo.connect( this.retweetNode , "onclick" , function() {
                        domNode._retweet(domNode);
                    });
                    dojo.addClass( this.favoriteNode , "display-inline" );
                    dojo.addClass( this.replyNode , "display-inline" );
                    if (this.args.canRetweet) {
                        dojo.addClass( this.retweetNode , "display-inline" );
                    } else {
                        dojo.addClass( this.retweetNode , "display-none" );
                    }
                } else {
                    dojo.addClass( this.favoriteNode , "display-none" );
                    dojo.addClass( this.replyNode , "display-none" );
                    dojo.addClass( this.retweetNode , "display-none" );
                }
            },
            getAttribute : function() {
                return this.args;
            },
            getTimestamp : function () {
                return this.args.timestamp;
            },
            appendChild : function() {
                alert(' in append child here');
                dojo.parser.parse(this);
            },
            getSelf : function() {
                return this;
            },
            _toggleFavorite : function(domNode) {

                var urlString = "tweet/markFavorite";
                var favClassName = "unfavorite";
                var unfavClassName = "favorite";

                if( domNode.args.isFavorite == "true" ) {
                    urlString = "tweet/deleteFavorite";
                    var t = favClassName;
                    favClassName = unfavClassName;
                    unfavClassName = t;
                }

                dojo.xhrPost({
                     url: urlString,
                     content: {tweetId:domNode.args.tweetId},
                     load: function(data) {
                         dojo.removeClass(domNode.favoriteNode, unfavClassName);
                         dojo.addClass(domNode.favoriteNode, favClassName);
                         if( domNode.args.isFavorite == "false" ) {
                            domNode.args.isFavorite = "true";
                            domNode.favoriteNode.innerHTML = "Unfavorite";
                         }
                         else {
                            domNode.args.isFavorite = "false";
                            domNode.favoriteNode.innerHTML = "Favorite";
                         }

                     },
                     error: function(data) {
                        alert( 'error' + data );
                     }
                });
            },
            _retweet : function(domNode) {
                var tweetId = domNode.args.tweetId;
                var confirm_box = confirm("do you want to retweet tweetId " + tweetId);
                if (confirm_box) {
                    dojo.xhrPost({
                         url: "tweet/retweet",
                         content: {tweetId:domNode.args.tweetId},
                         load: function(data) {
                             alert("retweet done");
                             dojo.removeClass( domNode.retweetNode , "display-inline" );
                             dojo.addClass( domNode.retweetNode , "display-none" );
                         },
                         error: function(data) {
                            alert( 'error' + data );
                         }
                    });
                }
            }
        }
);