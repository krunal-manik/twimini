dojo.provide("js.tweetContainerWidget");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");
dojo.require("dijit.Dialog");

dojo.declare("js.tweetContainerWidget",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
            },
            templateString : dojo.cache( "js/widgets", "tweetContainer.html"),
            postCreate : function() {
                this.inherited(arguments);
                this.args.tweet = addslashes(this.args.tweet);
                if( this.args.isFavorite == "false" ) {
                    dojo.addClass( this.favoriteNode , "fav" );
                    this.favoriteNode.innerHTML = "Favorite";
                }
                else {
                    dojo.addClass( this.favoriteNode , "unfav" );
                    this.favoriteNode.innerHTML = "Unfavorite";
                }
                var domNode = this;
                dojo.connect( this.favoriteNode , "onclick" , function() {
                    domNode._toggleFavorite(domNode);
                });
                dojo.connect( this.replyNode , "onclick" , function() {
                    reply(domNode.args.tweetId);
                });
            },
            _toggleFavorite : function(domNode) {

                urlString = "tweet/markFavorite";
                favClassName = "unfav";
                unfavClassName = "fav";

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
            }
        }
);
