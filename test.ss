<% if $Var %>
	asdada
<% end_if %>
<% if $Var %>
<% end_if %>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>Test</title>
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width">
	<link rel="stylesheet" href="css/main.css">
</head>
<body class="my-class">
<p>
	# You are reading the ".properties" entry.
	! The exclamation mark can also mark text as comments.
	website = http://en.wikipedia.org/
	language = English
	# The backslash below tells the application to continue reading
	# the value onto the next line.
	message = Welcome to \
	Wikipedia!
</p>
<p>
	# Add spaces to the key
	key\ with\ spaces = This is the value that could be looked up with the key "key with spaces".
	# Unicode
	tab : \u0009
	Variable $Var
	Variable in quote "$Var"
	Delimited variable {$Var}
</p>
<% loop $Var %>
Variable $Var
<% end_loop %>
<% base_tag %>
<% include MetaTags %>

$Var

<ul>
	<li>
		<ul>
			<li><b>Features</b></li>
			<li>Basic tag recognition. Recognizes the following tags:
				<ul>
					<li><% if $Var %> - <% else_if %> - <% else %> - <% end_if %></li>
					<li><% loop $Var %> - <% end_loop %></li>
					<li><% with $Var %> - <% end_with %></li>
					<li><% control $Var %> - <% end_control %></li>
					<li><%-- Comment --%></li>
					<li><% include File %></li>
					<li><% base_tag %></li>
					<li>Var statements, both {$Var} and $Var is supported.</li>
				</ul>
			</li>
			<li>Basic syntax highlighting</li>
			<li>Brace matching</li>
			<li>Code folding</li>
			<li>HTML is recognized and can be formatted.</li>
			<li>Error messages for mismatching blocks and unexpected blocks.<br>
				Error messages for syntax errors in Var statements.
			</li>
			<li>Message about unrecognized tags.</li>
		</ul>
	</li>
</ul>
<% if $var %>
// This is a comment
<%-- This is a comment --%>
// Simple string translation
<%t Namespace.Entity "String to translate" %>

// Using the natural languate comment parameter to supply additional context information to translators
<%t SearchResults.NoResult "There are no results matching your query." is "A message displayed to users when the search produces no results." %>

// Using injection to add variables into the translated strings (note that $Name and $Greeting must be available in the current template scope).
<%t Header.Greeting "Hello {name} {greeting}" name=$Name greeting=$Greeting %>
<% if $Var %>
<script>
	window.FBUserStatus = false;
	window.fbAsyncInit = function() {
	FB.init({
	appId      : '$Top.FacebookAppId',
	status     : true,
	cookie     : true,
	xfbml      : true,
	oauth      : true
	});

	FB.Event.subscribe('auth.login', function() {
	if ($("#carousel").length == 0)
	window.location.reload();
	});

	FB.getLoginStatus(function(response) {
	window.FBUserStatus = response;
	if (response.status === 'connected') {
	$('#carousel, #arrow').animate({opacity:1},2000);

	var uid = response.authResponse.userID;
	var accessToken = response.authResponse.accessToken;
	}
	else {
	if ($("#carousel").length > 0)
	window.location.href = $("#carousel").data("url");
	// the user isn't logged in to Facebook.
	}
	});
	};

	// Load the SDK Asynchronously
	(function(d){
	var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
	if (d.getElementById(id)) {return;}
	js = d.createElement('script'); js.id = id; js.async = true;
	js.src = "//connect.facebook.net/sv_SE/all.js";
	ref.parentNode.insertBefore(js, ref);
	}(document));
</script>
</body>
</html>