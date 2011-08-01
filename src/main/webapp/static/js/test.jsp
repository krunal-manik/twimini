<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html dir="ltr">
    
    <head>
        <style type="text/css">
            body, html { font-family:helvetica,arial,sans-serif; font-size:90%; }
        </style>
        <script src="dojo/dojo/dojo.js"
        djConfig="parseOnLoad: true">
        </script>
        <script type="text/javascript">
            dojo.require("dijit.form.Button");
            dojo.require("dijit.Dialog");

            dojo.addOnLoad(function() {
                // create the dialog:
                var dialogColor = dijit.byId("dialogColor");

                // connect t the button so we display the dialog onclick:
                //dojo.connect(dijit.byId("button4"), "onClick", dialogColor, "show");
            });
        </script>
        <link rel="stylesheet" type="text/css" href="../css/claro.css"/ >
    </head>
    
    <body class=" claro ">
        <div id="dialogColor" title="Colorful" dojoType="dijit.Dialog">
            My background color is Green
        </div>
        <p>
            When pressing this button the dialog will popup:
        </p>
        <button id="button4" dojoType="dijit.form.Button" type="button">
            Show me!
        </button>
        <!-- NOTE: the following script tag is not intended for usage in real
        world!! it is part of the CodeGlass and you should just remove it when
        you use the code -->
        <script type="text/javascript">
            dojo.addOnLoad(function() {

            });
        </script>
    </body>

</html>
