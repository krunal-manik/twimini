dojo.provide("js.userBoxContainer");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");

dojo.declare("js.userBoxContainer",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
                this.widgetsInTemplate = true;
            },
            templateString : dojo.cache( "js/widgets", "userBoxContainer.html"),
            postCreate : function() {
                this.inherited(arguments);
                this.args.title = this.args.title.toString();
                this.firstURL = "/first_" + this.args.title;
                this.moreURL = "/more_" + this.args.title;
                this.args.user = this.args.user.toString();
                var domNode = this;
                dojo.connect( dijit.byId(this.args.id), "onShow", function() {
                    domNode.first(domNode);
                });
                dojo.connect(this.moreNode, "onclick", function() {
                        var x = domNode.containerNode;
                        var lastId = x.children[x.children.length - 1].id;
                        domNode.more( 2, domNode);
                    }
                );
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
            first: function(domNode) {
                document.location = domNode.args.href;
                dijit.byId(domNode.containerNode).innerHTML = '';
                dojo.xhrGet({
                    url : domNode.firstURL.toString() + "?user=" + domNode.args.user + "&follower=" + domNode.args.loggedInUser,
                    handleAs : 'json',
                    load : function(data) {
                        for (var i =0; i < data.length; i++) {
                            //  data[i].tweet = unEscapeCharacters(data[i].tweet);
                            data[i].loggedInUser = domNode.args.loggedInUser;
                            var widget = new js.userContainer(data[i]);
                            widget.placeAt( domNode.containerNode , "last" );
                        }
                        if (data.length == 10) {
                            dojo.removeClass( domNode.moreNode , "display-none" );
                            dojo.addClass( domNode.moreNode , "display-inline" );
                        }
                    },
                    error: function(data) {
                       alert( 'error ' + data );
                    }
                });
            },
            more : function(n, domNode) {
                dojo.xhrGet({
                    url : this.moreURL.toString() + "?user=" + this.args.user + "&follower=" + this.args.loggedInUser + "&from=" + domNode.containerNode.children.length,
                    handleAs : 'json',
                    load : function(data) {
                        for (var i =0; i < data.length; i++) {
                            //  data[i].tweet = unEscapeCharacters(data[i].tweet);
                            data[i].loggedInUser = domNode.args.loggedInUser;
                            var widget = new js.userContainer(data[i]);
                            widget.placeAt( domNode.containerNode , "last" );
                        }
                    },
                    error: function(data) {
                       alert( 'error ' + data );
                    }
                });
            }
        }
);