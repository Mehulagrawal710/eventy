<!DOCTYPE html>
<html>

<head>
	<title>Calendar</title>
	<meta charset="utf-8">
	<!--Bootstrap-->
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<link href="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/css/bootstrap4-toggle.min.css" rel="stylesheet">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	<script src="https://cdn.jsdelivr.net/gh/gitbrent/bootstrap4-toggle@3.6.1/js/bootstrap4-toggle.min.js"></script>
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

	<div align="center" id="heading" style="color: white;">Eventy</div>
	<img ng-if="loadAnim" style="position: fixed;width: 30%;top: 50%;left:50%;transform: translate(-50%, -50%);z-index: 10;" src="https://flevix.com/wp-content/uploads/2019/07/Bubble-Preloader-1.gif">
	<button ng-click="" style="margin-left: 100px;margin-top: -120px;" class="btn btn-md btn-primary">API Reference</button>
	<button ng-show="loggedIn.login==false" ng-click="show_login_page()" style="float: right;margin-top: -70px;margin-right: 170px;" class="btn btn-md btn-primary">Login/Signup</button>
	<button ng-show="loggedIn.login==true" ng-click="logout()" style="float: right;margin-top: -70px;margin-right: 170px;" class="btn btn-md btn-primary">Logout {{loggedIn.username}}</button>
	<button ng-click="do()" style="float: right;margin-right: 100px;margin-top: -70px;" class="btn btn-md btn-primary">About</button>
	
	<!--LOGIN/SIGNUP BOX
		==================================================================-->
	<div ng-class="{blackfade: open_login_page}">
		<div class="addevent" ng-class="{addevent_clicked: open_login_page}">
			<div class="container content" ng-class="{content_clicked: open_login_page}">
				<ul class="nav nav-pills">
					<li class="active"><a data-toggle="tab" href="#login">Login</a></li>
					<li><a data-toggle="tab" href="#signup">Signup</a></li>
				</ul>

				<div class="tab-content">
					<div id="login" class="tab-pane fade in active">
						<h3>Login</h3>
						<form name="loginForm" ng-submit="login(username, password);">
							Username: <input name="username" ng-model="username" required><br>
							Password: <input name="password" ng-model="password" required><br>
							<button type="submit" class="btn btn-lg btn-success">Login</button>
							<p style="color: red;">{{loginResponse}}</p>
						</form>
					</div>
					<div id="signup" class="tab-pane fade">
						<h3>SignUp</h3>
						<form name="signupForm" ng-submit="signup(username, password, email);">
							Username: <input name="username" ng-model="username" required><br>
							Password: <input name="password" ng-model="password" required><br>
							Email: <input type="email" name="email" ng-model="email" required><br>
							<button type="submit" class="btn btn-lg btn-success">SignUp</button>
							<p style="color: red;">{{signupResponse}}</p>
						</form>	
					</div>
				</div>
				
				<button ng-click="close_login_page()" style="margin-left: 20px;" class="btn btn-lg btn-danger">close</button>
			</div>
		</div>
	</div>
	<!--========================================================================-->

	<!--EMAIL VERIFICATION MESSAGE BOX
		==================================================================-->
	<div ng-class="{blackfade: open_verification_message_page}">
		<div class="addevent" ng-class="{addevent_clicked: open_verification_message_page}">
			<div class="container content" ng-class="{content_clicked: open_verification_message_page}">
				
				<p>An email verification link has been sent on your provided email address.</p>
				<p>Click on the link to Activate your account</p>
				
				<button ng-click="close_verification_message_page()" style="margin-left: 20px;" class="btn btn-lg btn-primary">Okay</button>
			</div>
		</div>
	</div>
	<!--========================================================================-->

	<!--CALENDAR DATES SHOW UP HERE
		==================================================================-->
	<div class="container" id="calendar">
		
		<button ng-click="decrement_year()" class="btn btn-sm btn-info"><<</button>
		<span>year</span>
		<button ng-click="increment_year()" class="btn btn-sm btn-info">>></button>
		
		<button ng-click="decrement_month()" class="btn btn-sm btn-info"><<</button>
		<span>month</span>
		<button ng-click="increment_month()" class="btn btn-sm btn-info">>></button>
		<button ng-click="show_all_event_page()" style="float: right;" class="btn btn-md btn-success">all events</button>
		<button ng-click="today()" style="float: right;margin-right: 5px;" class="btn btn-md btn-success">today</button>
		<h2>{{months[current_month].month}}, {{current_year}}</h2>
		<hr>
		<div>
			<div ng-repeat="i in get_days_array(current_month)">
				<div class="day" ng-click="day_click(i)" ng-class="{today: i==current_date && current_month==month && current_year == year}">
					<h3>{{i}}</h3>
					<h4>{{weekdays[weekday(i)]}}</h4>
					<!-- ---------------------------------------------------- -->
					<div ng-show="event_exist(i)">
						<div ng-repeat="e in events">
							<span ng-if="e.date.year==current_year">
								<span ng-if="e.date.monthValue==current_month+1">
									<span ng-if="e.date.dayOfMonth==i">
										{{e.title}}
										<br>
									</span>
								</span>
							</span>
						</div>
					</div>
					<!-- ---------------------------------------------------- -->
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
				<form ng-submit="createEvent(title, description, add_event_date, time.getHours(), time.getMinutes(), getNotif, minBefore, notifMsg);">
					Event Title: <input ng-model="title" required style="border: 0;border-bottom: 2px solid lightblue;"><br>
					Event Description: <input ng-model="description" required style="border: 0;border-bottom: 2px solid lightblue;"><br>
					Time: <input ng-model="time" type="time" placeholder="HH:mm" required><br>
					<input ng-model="getNotif" type="checkbox" ng-checked="false" ng-init="getNotif=false">
					<label for="email-noti" class="form-check-label">Get Email Notification</label>
					<div ng-show="getNotif">
						Time before to notify: <input ng-model="minBefore" type="number" ng-init="minBefore=0"> min
						<br>
						Message for yourself: <textarea ng-model="notifMsg" type="text" style="height: 50px;width: 150px;"></textarea>
					</div>
					<button style="margin-left: 130px;" class="btn btn-lg btn-success">Add Event</button>
				</form>
				<button ng-click="close_add_event()" style="margin-left: 20px;" class="btn btn-lg btn-danger">close</button>
			</div>
		</div>
	</div>
	<!--========================================================================-->

	<!--UPDATE EVENTS BOX
		==================================================================-->
	<div ng-class="{blackfade: open_update_event_page==1}">
		<div class="addevent" ng-class="{addevent_clicked: open_update_event_page==1}">
			<div class="container content" ng-class="{content_clicked: open_update_event_page==1}">
				<h2>Update Event:</h2>
				<p style="color: grey;">
					Note: It is not possible to change the timing of an event currently.
					If still you need to change the timing, it is recommended to delete this event and create a new one.
				</p>
				<br><br>
				<form ng-submit="updateThisEvent();">
					Event Title: <input ng-model="utitle" required style="border: 0;border-bottom: 2px solid lightblue;"><br>
					Event Description: <input ng-model="udescription" required style="border: 0;border-bottom: 2px solid lightblue;"><br>
					
					<input ng-model="ugetNotif" type="checkbox">
					<label class="form-check-label">Get Email Notification</label>
					<div ng-show="ugetNotif">
						Time before to notify: <input ng-model="utimeBefore" type="number"> min
						<br>
						Message for yourself: <textarea ng-model="unotifMsg" type="text" style="height: 50px;width: 150px;"></textarea>
						<p style="color: grey;">
							Note: If you turn off notification for an event which was previously turned onn, the email notification will still be delivered on the mentioned time.
						</p>
					</div>
					<button style="margin-left: 130px;" class="btn btn-lg btn-success">Update Event</button>
				</form>
				<button ng-click="close_update_event_page()" style="margin-left: 20px;" class="btn btn-lg btn-danger">close</button>
			</div>
		</div>
	</div>
	<!--========================================================================-->
	
	<!--PREVIEW EVENTS BOX
		=======================================================================-->
	<div ng-class="{blackfade: open_preview_event_page==1}">
		<div class="addevent" ng-class="{addevent_clicked: open_preview_event_page==1}">
			<div class="container content" ng-class="{content_clicked: open_preview_event_page==1}" style="width: 500px; height: 480px;overflow-y: hidden;overflow-x: hidden;">
				<span style="font-size: 40px;padding: 10px;">Added Events for {{preview_event_date}}:</span>
				<button ng-click="close_preview_event_page()" class="btn btn-sm btn-danger" style="margin:15px 0 0 0;float: right;">&times;</button>
				<button ng-click="add_event(preview_event_day)" class="btn btn-sm btn-primary" style="margin:15px 0 0 0;float: right;"><i class="fa fa-plus-circle"></i></button>
				<center><p ng-if="events.length==0" style="color: grey; font-size: 15px;font-family: Comic Sans MS;"><i>No events Added...</i></p></center>
				<div style="overflow-y: scroll;height: 400px;width: 107%;">
					<div ng-repeat="event in events | orderBy: ['date.year', 'date.monthValue', 'event.date.dayOfMonth', 'event.date.hour', 'event.date.minute'] : true">
						<span ng-if="event.date.year==current_year">
							<span ng-if="event.date.monthValue==current_month+1">
								<span ng-if="event.date.dayOfMonth==preview_event_day">
									
									<!-- <p>Event ID: {{event.eventId}}</p> -->
									<p><b>Title: {{event.title}}</b></p>
									 <p>Description: {{event.description}}</p>
									<p>Event Date: {{event.date.dayOfMonth}}-{{event.date.monthValue}}-{{event.date.year}}</p>
									<p>Event Time: {{event.date.hour}}:{{event.date.minute}}</p>
									<p>Status: {{event.status}}</p>
									<p ng-show="{{event.noOfTimesUpdated==0}}">Updated: Not updated.</p>
									<p ng-show="!{{event.noOfTimesUpdated==0}}">Updated: {{event.noOfTimesUpdated}} time(s)</p>
									<p ng-show="!{{event.noOfTimesUpdated==0}}">Last Updated On: {{event.lastUpdatedDatetime}}</p>
									<p ng-show="!{{event.notification.notifActive}}" style="color: red;">No Notification Added</p>
									<p ng-show="{{event.notification.notifActive}}" style="color: lightgreen;">Notify {{event.notification.timeBeforeToNotify/60}} min(s) before the event.</p>
									<p ng-show="{{event.notification.notifActive}}" style="color: lightblue;">Message: {{event.notification.notifMessage}}</p>
									<button ng-click="deleteEvent(event.eventId)" class="btn btn-sm btn-danger"><i class="fa fa-trash"></i></button>
									<button ng-click="updateEvent(event.eventId)" class="btn btn-sm btn-warning"><i class="fa fa-edit"></i></button>
									<br><br>
									<br><br>

								</span>
							</span>
						</span>
						
					</div>
				</div>	
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
					<div ng-repeat="event in events | orderBy: ['date.year', 'date.monthValue', 'event.date.dayOfMonth', 'event.date.hour', 'event.date.minute'] : true">
						<!-- <p>Event ID: {{event.eventId}}</p> -->
						<p><b>Title: {{event.title}}</b></p>
						 <p>Description: {{event.description}}</p>
						<p>Event Date: {{event.date.dayOfMonth}}-{{event.date.monthValue}}-{{event.date.year}}</p>
						<p>Event Time: {{event.date.hour}}:{{event.date.minute}}</p>
						<p>Status: {{event.status}}</p>
						<p ng-show="{{event.noOfTimesUpdated==0}}">Updated: Not updated.</p>
						<p ng-show="!{{event.noOfTimesUpdated==0}}">Updated: {{event.noOfTimesUpdated}} time(s)</p>
						<p ng-show="!{{event.noOfTimesUpdated==0}}">Last Updated On: {{event.lastUpdatedDatetime}}</p>
						<p ng-show="!{{event.notification.notifActive}}" style="color: red;">No Notification Added</p>
						<p ng-show="{{event.notification.notifActive}}" style="color: lightgreen;">Notify {{event.notification.timeBeforeToNotify/60}} min(s) before the event.</p>
						<p ng-show="{{event.notification.notifActive}}" style="color: lightblue;">Message: {{event.notification.notifMessage}}</p>
						<button ng-click="deleteEvent(event.eventId)" class="btn btn-sm btn-danger"><i class="fa fa-trash"></i></button>
						<button ng-click="updateEvent(event.eventId)" class="btn btn-sm btn-warning"><i class="fa fa-edit"></i></button>
						<br><br>
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

    var event_date;

    $scope.do = function(){
    	console.log($scope.events);
    }
	
	/*-----------------------DOMAIN------------------------*/
	var domain = "http://localhost:8080/RestApplication/calendar/";
	

	$scope.loadAnim = false;

	$scope.show_load_animation = function(){
		$scope.loadAnim = true;		
	}
	$scope.stop_load_animation = function(){
		$scope.loadAnim = false;		
	}

	/*-----------------LOGIN/SIGNUP------------------------*/
	if(localStorage.getItem('loginInfo') === null) {
		$scope.loggedIn = {
			login: false,
			username: null,
			token: null
		};
    	localStorage.setItem('loginInfo', JSON.stringify($scope.loggedIn));
	}else{
		$scope.loggedIn = JSON.parse(localStorage.getItem('loginInfo'));
	}

	$scope.open_login_page = 0;
	$scope.open_verification_message_page = 0;

	$scope.show_login_page = function(){
    	$scope.open_login_page = 1;
    }

    $scope.close_login_page = function(){
    	$scope.open_login_page = 0;
    }

    $scope.show_verification_message_page = function(){
    	$scope.open_verification_message_page = 1;
    }

    $scope.close_verification_message_page = function(){
    	$scope.open_verification_message_page = 0;
    }

    $scope.loginResponse = null;
    $scope.signupResponse = null;

	$scope.login= function(username, password){
    	$scope.show_load_animation();
    	$http.post(domain+"login?username="+username+"&password="+password)
			.then(function(successCallback) {
				$scope.stop_load_animation();     
			    if (successCallback.status == 200) {
			    	$scope.loginResponse = null;
			    	$scope.loggedIn.login = true;
			    	$scope.loggedIn.username = username;
			    	$scope.loggedIn.token = successCallback.data.token;
			    	localStorage.setItem('loginInfo', JSON.stringify($scope.loggedIn));
			        $scope.close_login_page();
			        $scope.populateEvents();
			        // ..........................
			    }
			}, function(errorCallback){
				$scope.stop_load_animation();     
			    if (errorCallback.status == 401) {
			        $scope.loginResponse = errorCallback.data.message;
			    }else{
					window.alert("Server Not Responding");
				}
			}
		);
    }

    $scope.logout= function(woutconfirm){
    	if(woutconfirm){
    		$scope.loggedIn.login = false;
	    	$scope.loggedIn.username = null;
	    	$scope.loggedIn.token = null;
	    	localStorage.removeItem('loginInfo');
    	}
    	else if(window.confirm("Are you sure that you want to logout?")){
	    	$scope.loggedIn.login = false;
	    	$scope.loggedIn.username = null;
	    	$scope.loggedIn.token = null;
	    	localStorage.removeItem('loginInfo');
    	}
    }

    $scope.signup= function(username, password, email){
    	$scope.show_load_animation();
    	$http.post(domain+"signup?username="+username+"&password="+password+"&email="+email)
			.then(function success(response) {
				$scope.stop_load_animation();
				if(response.status == 200){
					$scope.signupResponse = null;
					$scope.close_login_page();
					$scope.show_verification_message_page();
				}
			}, function error(response) {
				$scope.stop_load_animation();
				if(response.status == 406){
					$scope.signupResponse = response.data.message;
				}
			}
		);
    }
    /*-----------------------------------------------------*/


    /*-------------------ALL EVENTS------------------------*/

    $scope.events = null;

    $scope.populateEvents = function(){
    	token = $scope.loggedIn.token;
    	$scope.show_load_animation();
    	$http.get(domain+"events?token="+token)
		.then(function success(response) {
				$scope.stop_load_animation();     
				if(response.status == 200){
					$scope.events = response.data.events;
				}
			}, function error(response) {
				$scope.stop_load_animation();
				if(response.status == 406){
					$scope.logout(true);
					window.alert("Session Expired!\nPlease Login Again.");
				}else{
					window.alert("Server Not Responding");
				}
			}
		);
    }

    if($scope.loggedIn.login){
    	$scope.populateEvents();
    }

    $scope.show_all_event = 0;

    $scope.show_all_event_page = function(){
    	$scope.show_all_event = 1;
    }

    $scope.close_all_event_page = function(){
    	$scope.show_all_event = 0;
    }

    $scope.event_exist = function(date){
    	if($scope.events === null){return false;}
    	for(var i=0; i<$scope.events.length; i++){
    		if(date == $scope.events[i].date.dayOfMonth && $scope.current_month+1 == $scope.events[i].date.monthValue && $scope.current_year == $scope.events[i].date.year){
    			return true;
    		}
    	}
    	return false;
    }

	/*-----------------------------------------------------*/


	/*-----------------PREVIEW EVENTS------------------------*/
	$scope.open_preview_event_page = 0;

	$scope.show_preview_event_page = function(){
		$scope.open_preview_event_page = 1;
	}
	
	$scope.close_preview_event_page = function(){
		$scope.open_preview_event_page = 0;
	}

	$scope.day_click = function(date){
    	if($scope.event_exist(date)){
    		$scope.preview_event_day = date;
    		$scope.preview_event_date = date+", "+$scope.months[$scope.current_month].month+", "+$scope.current_year;
    		$scope.show_preview_event_page();
    	}else{
    		$scope.add_event(date);
    	}
    }

	/*-----------------------------------------------------*/


    /*-----------------ADD EVENTS------------------------*/
    $scope.open_add_event = 0;

    $scope.add_event = function(date){
	    event_date = date;	
    	$scope.open_add_event = 1;
		$scope.close_preview_event_page();
    	$scope.add_event_date = date+", "+$scope.months[$scope.current_month].month+", "+$scope.current_year;
    }
    
    $scope.close_add_event = function(){
    	$scope.open_add_event = 0;
    	$scope.event_title = "";
    	$scope.event_description = "";	
    }

    $scope.createEvent = function(title, desc, date, hrs, mins, getNotif, minBefore, notifMsg){
    	$scope.show_load_animation();
    	token = $scope.loggedIn.token;
    	day = date.slice(0, date.search(","));
    	month = $scope.current_month+1;
    	year = $scope.current_year;
    	if(hrs==0){hrs = "00";}
    	else if(hrs<10){hrs = "0"+hrs;}
    	if(mins==0){mins = "00";}
    	else if(mins<10){mins = "0"+mins;}
    	if(month<10){month = "0"+month;}
    	if(day<10){day = "0"+day;}
    	date = year+"-"+month+"-"+day+" "+hrs+":"+mins;
    	if(getNotif==false){
    		minBefore = 0;
    		notifMsg = null;
    	}
    	timeBefore = minBefore*60;
    	if(notifMsg==undefined){notifMsg = "";}
    	//console.log(token, title, desc, date, getNotif, minBefore, notifMsg);
    	
    	$http.post(domain+"events?token="+token+"&title="+title+"&description="+desc+"&date="+date+"&isNotifActive="+getNotif+"&timeBeforeToNotify="+timeBefore+"&notifMessage="+notifMsg)
			.then(function success(response) {
				if(response.status == 201){
					$scope.close_add_event();
					$scope.populateEvents();
					$scope.stop_load_animation();
				}
			}, function error(response) {
				$scope.stop_load_animation();
				if(response.status == 406){
					$scope.logout(true);
					window.alert("Session Expired!\nPlease Login Again.");
				}else{
					window.alert("Server Not Responding");
				}
			}
		);

    	$scope.close_add_event();
    }

	/*-----------------------------------------------------*/

	/*------------------UPDATE EVENTS---------------------*/

	$scope.open_update_event_page = 0;

	$scope.show_update_event_page = function(){
		$scope.open_update_event_page = 1;
	}

	$scope.close_update_event_page = function(){
		$scope.open_update_event_page = 0;
	}

	$scope.uevent = null;
	$scope.updateEvent = function(eventId){
		$scope.close_preview_event_page();
		$scope.close_all_event_page();
		for(var i=0; i<$scope.events.length; i++){
    		if($scope.events[i].eventId == eventId){
    			$scope.uevent = $scope.events[i];
    			break;
    		}
    	}
    	$scope.utitle = $scope.uevent.title;
    	$scope.udescription = $scope.uevent.description;
    	$scope.ugetNotif = $scope.uevent.notification.notifActive;
    	$scope.utimeBefore = $scope.uevent.timeBeforeToNotify/60;
    	$scope.unotifMsg = $scope.uevent.notifMessage;

    	$scope.show_update_event_page();
	}

	$scope.updateThisEvent = function(){
		token = $scope.loggedIn.token;
		$scope.show_load_animation();     
		$http.put(domain+"events/"+$scope.uevent.eventId+"?token="+token+"&title="+$scope.utitle+"&description="+$scope.udescription+"&isNotifActive="+$scope.ugetNotif+"&timeBeforeToNotify="+$scope.utimeBefore*60+"&notifMessage="+$scope.unotifMsg)
			.then(function success(response) {
				$scope.stop_load_animation();     
				if(response.status == 200){
					$scope.close_update_event_page();
					$scope.populateEvents();
				}
			}, function error(response) {
				$scope.stop_load_animation();     
				if(response.status == 404){
						window.alert("No such event found!");
				}
				else if(response.status == 406){
					$scope.logout(true);
					window.alert("Session Expired!\nPlease Login Again.");
				}else{
					window.alert("Server Not Responding");
				}
			}
		);
	}

	/*-----------------------------------------------------*/

	/*------------------DELETE EVENTS---------------------*/

	$scope.deleteEvent = function(eventId){
		token = $scope.loggedIn.token;
		if(window.confirm("Are you sure that you want to delete this event?")){
            $scope.show_load_animation();
            $http.delete(domain+"events/"+eventId+"?token="+token)
				.then(function success(response) {
					$scope.stop_load_animation();     
					if(response.status == 200){
						$scope.populateEvents();
					}
				}, function error(response) {
					$scope.stop_load_animation();     
					if(response.status == 404){
						window.alert("No such event found!");
					}else if(response.status == 406){
						$scope.logout(true);
						window.alert("Session Expired!\nPlease Login Again.");
					}else{
						window.alert("Server Not Responding");
					}
				}
			);
        }
	
	}
	/*-----------------------------------------------------*/

});
</script>

</body>
</html>
