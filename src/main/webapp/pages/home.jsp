<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html ng-app="bucketlist">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Bucketlist</title>
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css">
<link rel="stylesheet" href="/resources/css/app.css" />
</head>
<body ng-controller="AppCtrl">
	<!--  <div ng-include="'/resources/partials/components/modals/login.html'"></div>  -->
	<div loginform></div>
	<div class="container">
		<section id="header" class="col-md-12">	
			<div class="row">
				<div class="col-md-12">		
					<h1>Needful Tings</h1>	
				</div>
			</div>
			<div id="main-navigation"><a href="javascript:void(0);" ng-click="openMessageBox()" ng-hide="isLoggedIn">Login/Register</a></div>
			<div id="main-navigation"><a href="javascript:void(0);" ng-click="logout()" ng-show="isLoggedIn">Logout</a></div>
			<div id="search-bar" ng-include="'/resources/partials/components/search_bar.html'"></div>
		</section>
		
		<div ng-view></div>
	</div>
	<!--  jQuery Libs -->
	<script src="http://code.jquery.com/jquery-1.10.2.min.js"></script>
	<script src="//code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

	<!--  Angular Libs -->
	<script src="//ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular.min.js"></script>
	<script src="http://code.angularjs.org/1.2.5/angular-route.min.js"></script>
	<script src="http://code.angularjs.org/1.2.5/angular-resource.min.js"></script>
	<script src="/resources/lib/angularjs-ui/sortable.js"></script>
	<script src="/resources/lib/angularjs-ui/autocomplete.js"></script>

	<!--  Bootstrap Libs -->
	<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
	
	
	<!--  App Scripts -->
	<script src="/resources/js/app.js"></script>
	<script src="/resources/js/services.js"></script>
	<script src="/resources/js/controllers.js"></script>
	<script src="/resources/js/filters.js"></script>
	<script src="/resources/js/directives.js"></script>
</body>
</html>