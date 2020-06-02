<!DOCTYPE html>
<html>

<head>
	<title>Calendar</title>
	<meta charset="utf-8">
	<!--Bootstrap-->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<!--AngularJS-->
	<script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.6.9/angular.min.js"></script>

<style>

body{background-repeat: no-repeat;
	 background-size: 100% auto;}

#heading{font-size: 80px;
		 width: 300px;
		 margin: 0 auto;
		 color: #000; 
		 font-family: freestyle script;}

#calendar{background-color: rgba(0,0,0,0.5);
		  color: #eee;
		  padding: 20px;
		  border: 5px solid #000;
		  border-radius: 20px;}

.day{border: 0px solid black;
	 border-radius: 10px;
	 padding: 10px;
	 height: 100px;
	 width: 100px;
	 float: left;}

.today{background-color: lightgreen;
	   color: #000;}	  

.day:hover{background-color: powderblue;
		   color: #000;}	  	

.event_hr{background-color: gray;
	      height: 3px;
	      border:0;
	      margin-top: -5px;}

.event_hr_green{background-color: #dc3545;}

.blackfade{height: 100vh;
		   width: 100vw;
		   position: fixed;
		   top: 0;
		   left: 0;
		   transition: all 200ms ease-in-out;
		   background-color: rgba(0,0,0,0.6);}

.addevent{height: 0;
		  width: 0;
		  position: fixed;
		  top:50%;
		  left:50%;
		  z-index: -1;
		  transition: all 200ms ease-in-out;}

.addevent_clicked{border-radius: 20px;
		  height: 80%;
		  width: 40%;
		  position: fixed;
		  top: 10%;
		  left: 30%;
		  z-index: 0;
		  background-color: rgba(255,255,255,1);
		  transform: translate(0,0);
		  transition: all 200ms ease-in-out;}

.content{opacity: 0; 
	  transition: all 100ms ease-in-out;}
.content_clicked{opacity: 1;
			  transition: all 500ms ease-in-out;}

</style>

</head>

<body ng-app="myApp" ng-style="{'background-image': 'url(' + months[current_month].bgimg + ')'}" ng-controller="ctrl">
	
	<div align="center" id="heading">My Calendar</div>
	
	<!--CALENDAR DATES SHOW UP HERE
		==================================================================-->
	<div class="container" id="calendar">
		
		<button ng-click="decrement_year()" class="btn btn-sm btn-info"><<</button>
		<span>year</span>
		<button ng-click="increment_year()" class="btn btn-sm btn-info">>></button>
		
		<button ng-click="decrement_month()" class="btn btn-sm btn-info"><<</button>
		<span>month</span>
		<button ng-click="increment_month()" class="btn btn-sm btn-info">>></button>
		<button ng-click="show()" style="float: right;" class="btn btn-md btn-success">all events</button>
		<button ng-click="today()" style="float: right;margin-right: 5px;" class="btn btn-md btn-success">today</button>
		<h2>{{months[current_month].month}}, {{current_year}}</h2>
		<hr>
		<div>
			<div ng-repeat="i in get_days_array(current_month)">
				<div class="day" ng-click="add_event(i)" ng-class="{today: i==current_date && current_month==month && current_year == year}">
					<h3>{{i}}</h3>
					<h4>{{weekdays[weekday(i)]}}</h4>
					<hr class="event_hr" ng-class="{event_hr_green: event_exist(i)}">
				</div>
			</div>
		</div>
		<p style="margin-top: 100px;margin-bottom:-20px; float: right;">*click on a date to add event</p>
	</div>
	<!--========================================================================-->	
	
	<!--NEW EVENTS ADD BOX
		==================================================================-->
	<div ng-class="{blackfade: open_add_event==1}">
		<div class="addevent" ng-class="{addevent_clicked: open_add_event==1}">
			<div class="container content" ng-class="{content_clicked: open_add_event==1}">
				<h2>Add Event for:</h2>
				<h1>{{add_event_date}}</h1>
				<br><br>
				<h3>Event Title:</h3>
				<input ng-model="event_title" style="border: 0;border-bottom: 2px solid lightblue;">
				<br>
				<h3>Event Description:</h3>
				<input ng-model="event_description" style="border: 0;border-bottom: 2px solid lightblue;">
				<br><br><br><br><br>
				<button ng-click="add_this_event(event_title, event_description)" style="margin-left: 130px;" class="btn btn-lg btn-success">Add Event</button>
				<button ng-click="close_add_event()" style="margin-left: 20px;" class="btn btn-lg btn-danger">close</button>
			</div>
		</div>
	</div>
	<!--========================================================================-->

	<!--ADDED EVENTS BOX
		=======================================================================-->
	<div ng-class="{blackfade: show_all_event==1}">
		<div class="addevent" ng-class="{addevent_clicked: show_all_event==1}">
			<div class="container content" ng-class="{content_clicked: show_all_event==1}" style="width: 500px; height: 480px;overflow-y: hidden;overflow-x: hidden;">
				<span style="font-size: 40px;padding: 10px;">Added Events:</span>
				<button ng-click="close_all_event_page()" class="btn btn-sm btn-danger" style="margin:15px 0 0 0;float: right;">&times;</button>
				<center><p ng-if="events.length==0" style="color: grey; font-size: 15px;font-family: Comic Sans MS;"><i>No events Added...</i></p></center>
				<div style="overflow-y: scroll;height: 400px;width: 107%;">
					<div ng-repeat="event in events">
						<span style="font-size: 50px;">{{event.t}}</span>
						<span style="font-size: 20px;margin-left: 30px;">{{event.d}}, {{months[event.m].month}}, {{event.y}}</span>
						<br>
						<p style="width: 500px;">{{event.desc}}</p>
						{{event.message}}
						<br><br>
					</div>
				</div>	
			</div>
		</div>
	</div>
	<!--========================================================================-->

<script>
var app = angular.module('myApp', []);
app.controller('ctrl', function ($scope, $http) {

	date = new Date();
	$scope.current_date = date.getDate();
	$scope.current_day = date.getDay();
	$scope.month = date.getMonth();
	$scope.year = date.getFullYear();

	$scope.current_year = $scope.year;
	$scope.current_month = $scope.month;
	
	$scope.today = function(){
		$scope.current_date = date.getDate();
		$scope.current_day = date.getDay();
		$scope.month = date.getMonth();
		$scope.current_year = date.getFullYear();

		$scope.current_year = $scope.year;
		$scope.current_month = $scope.month;
	}

	$scope.months = [
    	{month:'January', no_of_days:31, bgimg:"https://farm5.staticflickr.com/4894/46445790692_5f261d4921_o.jpg"},
    	{month:'February', no_of_days:28, bgimg:"https://farm5.staticflickr.com/4849/45584065565_c513645758_o.jpg"},
    	{month:'March', no_of_days:31, bgimg:"https://farm8.staticflickr.com/7815/46445790272_b9e95ff1ba_o.jpg"},
    	{month:'April', no_of_days:30, bgimg:"https://farm5.staticflickr.com/4812/46445789542_64e907cdee_o.jpg"},
    	{month:'May', no_of_days:31, bgimg:"https://farm5.staticflickr.com/4812/46445789542_64e907cdee_o.jpg"},
    	{month:'June', no_of_days:30, bgimg:"https://farm8.staticflickr.com/7876/45584065525_0d0efbc14d_o.png"},
    	{month:'July', no_of_days:31, bgimg:"https://farm8.staticflickr.com/7876/45584065525_0d0efbc14d_o.png"},
    	{month:'August', no_of_days:31, bgimg:"https://farm8.staticflickr.com/7865/46445789122_7c72b87d44_o.jpg"},
    	{month:'September', no_of_days:30, bgimg:"https://farm8.staticflickr.com/7865/46445789122_7c72b87d44_o.jpg"},
    	{month:'October', no_of_days:31, bgimg:"https://farm5.staticflickr.com/4805/45584066195_4cb77e6610_o.jpg"},
    	{month:'November', no_of_days:30, bgimg:"https://farm5.staticflickr.com/4813/45584065255_688c15914d_o.jpg"},
    	{month:'December', no_of_days:31, bgimg:"https://farm8.staticflickr.com/7898/44680372600_3d861f0256_o.jpg"}
    ];

    $scope.weekdays = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

    $scope.events = [];

    $scope.show = function(){
    	$http.get("http://localhost:8080/RestApplication/calendar/events/38?token=fcdfe88fd4ed4905a53fb467f5053c53")
			.then(function(response) {
			$scope.events = response.data;
			console.log($scope.events);
		});
    }		

    $scope.increment_year = function(){
    	if ($scope.current_year !=3000) {
    			$scope.current_year +=1;
    	}
    }

    $scope.decrement_year = function(){
    	if ($scope.current_year !=1900) {
    			$scope.current_year -=1;
    	}	
    }

    $scope.increment_month = function(){
    	if ($scope.current_month !=11) {
    			$scope.current_month +=1;
    	}
    	else{
    		$scope.current_year +=1;
    		$scope.current_month = 0;
    	}
    }

    $scope.decrement_month = function(){
    	if ($scope.current_month !=0) {
    			$scope.current_month -=1;
    	}
    	else{
    		$scope.current_year -=1;
    		$scope.current_month = 11;
    	}
    }

    $scope.get_days_array = function(n){
    	$scope.days_array = [];
    	for(x=1; x<=$scope.months[n].no_of_days; x++){
    		$scope.days_array.push(x);
    	}
    	return $scope.days_array;
    }
    
    zellers_month = [11,12,1,2,3,4,5,6,7,8,9,10];

    $scope.weekday = function(i){
    	k = i;
    	m = zellers_month[$scope.current_month];
    	d = ($scope.current_year)%100;
    	c = Math.floor(($scope.current_year)/100);

    	f = k + Math.floor((13*m-1)/5) + d + Math.floor(d/4) + Math.floor(c/4) - 2*c;
    	rem = Math.floor(f%7);
    	if(rem<0){index = rem + 7;}
    	else{index = rem;}
    	return index;
    }

    $scope.open_add_event = 0;
    var event_date;
    
    $scope.add_event = function(date){
	    event_date = date;	
    	$scope.open_add_event = 1;
    	$scope.add_event_date = date+", "+$scope.months[$scope.current_month].month+", "+$scope.current_year;
    }
    
    $scope.close_add_event = function(){
    	$scope.open_add_event = 0;
    	$scope.event_title = "";
    	$scope.event_description = "";	
    }
    
    $scope.add_this_event = function(title, description){
    	$scope.events.push(
    		{
    			d: event_date,
    			m: $scope.current_month,
    			y: $scope.current_year,
    			t: title,
    			desc: description
    		}
    	);
    }

    $scope.show_all_event = 0;

    $scope.show_all_event_page = function(){
    	$scope.show_all_event = 1;
    }

    $scope.close_all_event_page = function(){
    	$scope.show_all_event = 0;
    }

    $scope.event_exist = function(date){
    	for(var i=0; i<$scope.events.length; i++){
    		if(date == $scope.events[i].d && $scope.current_month == $scope.events[i].m && $scope.current_year == $scope.events[i].y){
    			return true;
    		}
    	}
    	return false;
    }


});
</script>

</body>
</html>
