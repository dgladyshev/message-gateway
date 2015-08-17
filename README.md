# message_gateway
This is a working template of REST service via Spring Boot - message gateway.

##purpose

The main purpose of this application is to provide unified and simple interface for sending messages via any channel (sms, emails, etc...) and to store this messages in some queue. It is quite usefull then you have many services and you don't won't to teach each of them to work with your sms, email and other clients.

##how to run
Just run MessageGatewayApplication class and Spring Boot will start application server automatically. 

##what is it

This is a useful working example of how you can implement REST service with Spring techonologies. 

This is a light version of real project, so I cutted some stuff, but it still working ^_^ 

You can use this as a template with how-to on: Spring Boot, Spring Security, Spring JdbcTemplate, Spring MVC.

##how it works 

Application has @RestController, you can POST a message via /message with XML or JSON (see SendRequest class for protocol) or GET message status via /message/{id}. 

All request must be signed with basic auth (see WebSecurityConfiguration class for details). Important: don't forget to enable CSRF if you wish to make this application safely accessible from outer network.

Application inserts incoming messages in some table via JDBC Template. This table is a queue for other applications which would actually send this messages. 

