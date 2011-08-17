dojo.provide("js.tweetBoxContainer");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");

dojo.declare("js.tweetBoxContainer",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
                this.widgetsInTemplate = true;
            },
            templateString : dojo.cache( "js/widgets", "tweetBoxContainer.html"),
            postCreate : function() {
                this.inherited(arguments);
                this.args.title = this.args.title.toString();
                this.firstURL = "/first_" + this.args.title;
                //this.latestURL = "/latest_" + this.args.title;
                this.moreURL = "/more_" + this.args.title;
                this.args.user = this.args.user.toString();
                var domNode = this;
                dojo.connect( dijit.byId(this.args.id), "onShow", function() {
                    domNode.first(domNode);
                });
                dojo.connect(this.moreNode, "onclick", function() {
                        var x = domNode.containerNode;
                        var lastId = x.children[x.children.length - 1].id;
                        domNode.more(dijit.byId(lastId).getTimestamp(), 2, domNode);
                    }
                );
                if (this.args.title == 'newsfeed') {
                    dojo.connect(this.latestNode, "onclick", function() {
                            var x = domNode.containerNode;
                            var firstId = x.children[0].id;
                            domNode.latest(domNode);
                        }
                    );
                    setInterval(function(){
                            var x = domNode.containerNode;
                            var firstId = x.children[0].id;
                            domNode.latestValue(domNode);
                        },
                    30000);
                }
            },
            getLatestTimestamp: function(domNode) {
                var x = domNode.containerNode;
                var firstId = x.children[0].id;
                return dijit.byId(firstId).getTimestamp();
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
            first : function(domNode) {
                document.location = domNode.args.href;
                dijit.byId(domNode.containerNode).innerHTML = '';
                dojo.xhrGet({
                    url : this.firstURL.toString() + "?user=" + domNode.args.user + "&favoriter=" + domNode.args.favoriter,
                    handleAs : 'json',
                    load : function(data) {
                        for (var i =0; i < data.length; i++) {
                            data[i].tweet = unEscapeCharacters(data[i].tweet);
                            if (domNode.args.favoriter != "null") {
                                data[i].tweetOptions = "true";
                            } else {
                                data[i].tweetOptions = "false";
                            }
                            var widget = new js.tweetContainer(data[i]);
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
            latest: function(domNode) {
                var latestURL = "/latest_" + this.args.title;
                dojo.xhrGet({
                    url : latestURL + "?timestamp=" + domNode.getLatestTimestamp(domNode) + "&favoriter=" + this.args.favoriter,
                    handleAs : 'json',
                    load : function(data) {
                        dojo.removeClass(domNode.latestNode, "display-inline");
                        dojo.addClass(domNode.latestNode, "display-none");

                        for (var i =data.length-1; i>=0; i--) {
                            data[i].tweet = unEscapeCharacters(data[i].tweet);
                            data[i].tweetOptions = "true";
                            var widget = new js.tweetContainer(data[i]);
                            widget.placeAt( domNode.containerNode , "first" );
                        }
                    },
                    error: function(data) {
                       alert( 'error ' + data );
                    }
                });
            },
            more : function(timestamp, n, domNode) {
                dojo.xhrGet({
                    url : domNode.moreURL + "?timestamp=" + timestamp + "&n=" + n + "&favoriter=" + this.args.favoriter,
                    handleAs : 'json',
                    load : function(data) {
                        if (data.length < n) {
                            dojo.removeClass(domNode.moreNode, "display-inline");
                            dojo.addClass(domNode.moreNode, "display-none");
                        }
                        for (var i =0; i < data.length; i++) {
                            data[i].tweet = unEscapeCharacters(data[i].tweet);
                            data[i].tweetOptions = "true";
                            var widget = new js.tweetContainer(data[i]);
                            widget.placeAt( domNode.containerNode , "last" );
                        }
                    },
                    error: function(data) {
                       alert( 'error ' + data );
                    }
                });
            },
            latestValue : function(domNode) {
                var latestURL = "/latest_" + domNode.args.title;
                dojo.xhrGet({
                    url : latestURL + "?timestamp=" + domNode.getLatestTimestamp(domNode) + "&favoriter=" + this.args.favoriter,
                    handleAs : 'json',
                    load : function(data) {
                        if (data.length > 0) {
                            dojo.removeClass(domNode.latestNode, "display-none");
                            dojo.addClass(domNode.latestNode, "display-inline");
                        }
                        domNode.latestValueNode.innerHTML = data.length;
                    },
                    error: function(data) {
                       alert( 'error ' + data );
                    }
                });
            }
        }
);