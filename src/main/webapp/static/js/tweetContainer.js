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
                this.args.tweetOptions = this.args.tweetOptions.toString();
                this.args.isFavorite = this.args.isFavorite.toString();
                this.tweetNode.innerHTML = addTags(filterEscapeCharacters(this.args.tweet));
                var domNode = this;
                if( this.args.tweetOptions == 'true' ) {
                    if( this.args.isFavorite ==  'false' ) {
                        dojo.addClass( this.favoriteNode , "fav" );
                        this.favoriteNode.innerHTML = "Favorite";
                    }
                    else {
                        dojo.addClass( this.favoriteNode , "unfav" );
                        this.favoriteNode.innerHTML = "Unfavorite";
                    }
                    dojo.connect( this.favoriteNode , "onclick" , function() {
                        domNode._toggleFavorite(domNode);
                    });
                    dojo.connect( this.replyNode , "onclick" , function() {
                        reply(domNode.args.tweetId);
                    });
                    dojo.addClass( this.favoriteNode , "display-inline" );
                    dojo.addClass( this.replyNode , "display-inline" );
                    dojo.addClass( this.retweetNode , "display-inline" );
                } else {
                    dojo.addClass( this.favoriteNode , "display-none" );
                    dojo.addClass( this.replyNode , "display-none" );
                    dojo.addClass( this.retweetNode , "display-none" );
                }
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
            _toggleFavorite : function(domNode) {

                var urlString = "tweet/markFavorite";
                var favClassName = "unfav";
                var unfavClassName = "fav";

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
