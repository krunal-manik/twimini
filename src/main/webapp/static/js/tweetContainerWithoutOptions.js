dojo.provide("js.tweetContainerWithoutOptions");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");
dojo.require("dijit.Dialog");

dojo.declare("js.tweetContainerWithoutOptions",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
            },
            templateString : dojo.cache( "js/widgets", "tweetContainerWithoutOptions.html"),
            postCreate : function() {
                this.inherited(arguments);
                this.args.tweet = addslashes(this.args.tweet);
            },
            getSelf : function() {
                return this;
            },
            getAttribute : function() {
                return this.args;
            }
        }
);
