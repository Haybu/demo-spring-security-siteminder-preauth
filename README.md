# Demo spring security siteminder preauth

Assuming users are pre-authenticated using siteminder before accessing this application. After authentication siteminder passes the username in a header called SM_USER. 
While the application performs no further authentication, spring security framework can use the `RequestHeaderAuthenticationFilter` filter to extract the username from this header and
continue the flow to perform other tasks. Through the collaborating pre-authentication authentication manager and user details service the flow can for example pull users' authorities from other external system (such as LDAP).

To run the application and be able to access it, use `ModeHeader` browser plugin and provide a header named `SM_USER` with a username value.
