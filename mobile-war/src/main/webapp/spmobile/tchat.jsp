<!DOCTYPE HTML>
<html>

<head>
   <title>Spmobile tchat</title>
   <meta http-equiv="content-type" content="text/html; charset=utf-8" />
   <meta name="viewport" content="width=device-width, initial-scale=1">

   <!-- require:dependencies -->
   <link href="jsxc/css/jquery-ui.min.css" media="all" rel="stylesheet" type="text/css" />
   <link href="jsxc/css/jsxc.css" media="all" rel="stylesheet" type="text/css" />
   <!--  endrequire -->

   <link href="tchat.css" media="all" rel="stylesheet" type="text/css" />

   <!-- require:dependencies -->
   <script src="jsxc/lib/jquery.min.js"></script>
   <script src="jsxc/lib/jquery-ui.min.js"></script>
   <script src="jsxc/lib/jquery.slimscroll.js"></script>
   <script src="jsxc/lib/jquery.fullscreen.js"></script>
   <script src="jsxc/lib/jsxc.dep.min.js"></script>
   <!--  endrequire -->

   <script src="jsxc/lib/bootstrap.min.js"></script>

   <!-- jsxc library -->
   <script src="jsxc/jsxc.min.js"></script>

   <!-- init script -->
   <script src="tchat.js"></script>

</head>

<body class="page-tchat">
	<h1 class="tchat-header">Spmobile tchat</h1>
        <input type="hidden" id="xmpp-domain" name="xmpp-domain" class="form-control" value="intranoo.silverpeas.com" />
        <input type="hidden" id="bosh-url" name="bosh-url" class="form-control" value="https://intranoo.silverpeas.com/http-bind/" />
        <form id="form">
                <input type="hidden" id="username" class="form-control" value="sebastien.vuillet"/>
                <input type="hidden" id="password" class="form-control" value="392b17c7eb68436a866e98706adb8670"/>
                <button type="submit" id="submit" class="submit btn btn-primary" data-loading-text="Logging in...">Log in chat</button>
                <button class="logout btn btn-default">Log out</button>
        </form>
</body>

</html>
