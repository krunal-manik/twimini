dojo.provide("js.userDropdown");
dojo.provide("js.functions");
dojo.require("dijit._Widget");
dojo.require("dijit._Templated");
dojo.require("dojo.cache");

dojo.declare("js.userDropdown",
        [dijit._Widget,dijit._Templated],{

            constructor : function(args) {
                this.args = args;
                this.widgetsInTemplate = true;
            },
            templateString : dojo.cache( "js/widgets", "userDropdown.html"),
            postCreate : function() {
                this.inherited(arguments);
                var domNode = this;
                dojo.connect(this, "onclick", function() {
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
            }
        }
);