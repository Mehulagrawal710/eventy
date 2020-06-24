# Eventy
#### An API service and a full fledged application
<a href="https://eventyapp.herokuapp.com">https://eventyapp.herokuapp.com</a>

### Introduction
Eventy is a one stop solution for Users and Developers. Eventy Application comes with an interactive interface and notification system giving convenience to the users to easily manage their schedule.

### Eventy API
Click <a href="https://documenter.getpostman.com/view/7982251/Szzn7xBM">API Documentation</a> for full API documentation.

### Features
> Authenticated Login/Signup with email confirmation.

> Token Authentication system.

> Manage Events with all the detailed parameters.

> Get Email notification for your events.

> Detailed API responses equipped with HAETOS.

> UI for non-Developer users. link- <a href="https://eventyapp.herokuapp.com">Eventy Application</a>

### Overview
Eventy Application works by integrating with it's own REST API which is finely architectured and secured with a self developed token authentication system. The API endpoints are available for everyone's use, therefore creating a scheduling service alternative on top of which, developers can build there applications.

### Authentication
The API seeks user authentication with a self developed token authentication system. On hitting the login endpoint, user gets a token which can then be used for sending further requests. The token is valid for a certain period of time, this time is specified in the response.

### Error Codes
400 - Bad Request

401 - Unauthorized

404 - Not Found

406 - Not Acceptable

### Rate limit
There is NO limit put by the application on the number of requests sent. But the rate of incoming requests is monitored and limited by the third party hosting servive Heroku, so user may get a "Server not responding" response message if requests are sent too frequently.

<h2>Technologies Used:</h2>
<img src="https://cdn-images-1.medium.com/max/1600/1*J_-vtvcqV1-v14WqkPWhiQ.png" width="75" height="75" alt="ANGULAR">
<img src="https://miro.medium.com/max/800/1*C4NqkSHWuQu-GC5zakLZAw.jpeg" width="150" height="75" alt="Java EE, Jax-RS, Tomcat">
<br>
