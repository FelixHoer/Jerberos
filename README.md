# Jerberos

Jerberos provides an implementation of the 
[Kerberos-Principle](http://www.ietf.org/rfc/rfc4120.txt) 
via an API similar to Java's 
[Socket](http://docs.oracle.com/javase/6/docs/api/java/net/Socket.html) and 
[ServerSocket](http://docs.oracle.com/javase/6/docs/api/java/net/ServerSocket.html).

# Kerberos-Principle

The Kerberos-System enables a client to establish a connection to a service, 
within which both communication partners have been authenticated. 
In addition to that, a secret session key is exchanged, 
that is used to encrypt all transmissions.

To achieve this, both client and service trust in a common "Trusted Thrid Party" 
(Authentication Server and Ticket Granting Server), 
with which they share their long term secret keys beforehand.

![Kerberos-Principle](https://github.com/FelixHoer/Jerberos/raw/master/principle.scg)

If a client wants to use a service, at first he has to request a 
Ticket Granting Ticket (TGT) from the Authentication Server (AS).
In addition to that Ticket he will receive a session-key, that is used to 
encrypt all following communication with the Ticket Granting Server (TGS).

After that the client has to forward the TGT to the TGS and request another 
ticket for the desired service.
In the response, there will again be a new session-key for communication with 
the service.

In the last step, the client forwards the newly issued ticket to the service and
receives an answer, that can only be created by the real service.

So after these steps both communication partners can be sure of the other's 
identity and both got a shared session-key to encrypt further communication.

# Usage

At first you will need an `AuthenticationServer` and `TicketGrantingServer`, 
so just instantiate them with their configuration files.

```java
// create config
ASConfiguration asConfig = new ASConfiguration(); 
// ... fill config file ...

// Authentication Server
AuthenticationServer as = new AuthenticationServer(asConfig);
```

```java
// create config
TGSConfiguration tgsConfig = new TGSConfiguration(); 
// ... fill config file ...

// start Ticket Granting Server
TicketGrantingServer tgs = new TicketGrantingServer(tgsConfig);
```

After that, you can start a Server like you would with Java's `ServerSocket`.
Listen to some port, accept socket-connections from clients and send or receive 
data via the `OutputStream` or `InputStream`.

```java
// create config
ServiceConfiguration serviceConfig = new ServiceConfiguration(); 
// ... fill config file ...

// open server on port
KerberosServerSocket serverSocket = new KerberosServerSocket(serviceConfig);

// accept client
KerberosSocket clientSocket = serverSocket.accept();
OutputStream out = clientSocket.getOutputStream();
InputStream in = clientSocket.getInputStream();

while (true) {
    // receive message
    byte[] buffer = new byte[100];
    int red = in.read(buffer);

    byte[] input = new byte[red];
    System.arraycopy(buffer, 0, input, 0, red);

    String inputString = new String(input);
    System.out.println("service received: " + inputString);

    // send capitalized version
    String outputString = inputString.toUpperCase();
    out.write(outputString.getBytes());
    out.flush();
    System.out.println("service sent: " + outputString);
}

// close connection
out.close();
in.close();
clientSocket.close();
serverSocket.close();
```

This example accepts only one client, and echoes all received messages in 
upper-case.

Now you can connect to the Server using a `KerberosSocket`. 
To send and receive use the Socket's `OutputStream` and `InputStream`.

```java
// create config
ClientConfiguration config = new ClientConfiguration();
// ... fill config file ...

// connect
KerberosSocket clientSocket = new KerberosSocket(config);
OutputStream out = clientSocket.getOutputStream();
InputStream in = clientSocket.getInputStream();

// send message
String output = "Hello World!";
out.write(output.getBytes());
out.flush();
System.out.println("client sent: " + output);

// receive message
byte[] buffer = new byte[100];
int red = in.read(buffer);

byte[] input = new byte[red];
System.arraycopy(buffer, 0, input, 0, red);

String message = new String(input);
System.out.println("client received: " + message);

// close connection
out.close();
in.close();
clientSocket.close();
```

This Client connects to the Server and sends the message `Hello World!`.
The Server should then respond with `HELLO WORLD!`.

# Implementation

* implements an API like Java's Socket and ServerSocket
* reliable communication via `TCP`-Sockets
* messages structured using the Extensible Markup Language (`XML`)
* binary data is `Base64`-encoded 
* encryption with the Advanced Encryption Standard (`AES`)

# Demo

The demo consists of a server that echoes all received messages in upper-case
and a GUI-client that sends such messages.

To compile all classes and to start the demo you can use the `makefile`.

```
cd /path/to/Jerberos
make
make run
```

# Dependencies

[Apache's Commons Codec](http://commons.apache.org/codec/) for Base64 encoding

# License

Jerberos is MIT licensed.
See the links above for licenses of dependencies.