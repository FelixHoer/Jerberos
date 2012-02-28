SRC = "./src"
DEST = "./bin"
CP = ".:./bin:./lib/commons-codec-1.5.jar"
JFLAGS = -g -cp $(CP) -sourcepath $(SRC) -d $(DEST)
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
    ./src/kerberos/serialize/SerializeUtilities.java \
	./src/kerberos/serialize/Serializable.java \
	./src/kerberos/serialize/StringSerializer.java \
	./src/kerberos/serialize/ObjectSerializer.java \
	./src/kerberos/serialize/ByteSerializer.java \
	./src/kerberos/serialize/DateSerializer.java \
    ./src/kerberos/serialize/Serializer.java \
	./src/kerberos/protocol/dto/Authenticator.java \
	./src/kerberos/protocol/dto/Encrypted.java \
	./src/kerberos/protocol/dto/TicketAuthenticatorRequest.java \
	./src/kerberos/protocol/dto/TicketSessionResponse.java \
	./src/kerberos/protocol/dto/ASRequest.java \
	./src/kerberos/protocol/dto/ASResponse.java \
	./src/kerberos/protocol/dto/ServiceRequest.java \
	./src/kerberos/protocol/dto/ServiceResponse.java \
	./src/kerberos/protocol/dto/TGSRequest.java \
	./src/kerberos/protocol/dto/TGSResponse.java \
	./src/kerberos/protocol/dto/Ticket.java \
	./src/kerberos/stack/WriteQ.java \
	./src/kerberos/stack/SerializeLayer.java \
	./src/kerberos/stack/ReadQ.java \
	./src/kerberos/stack/Stack.java \
	./src/kerberos/stack/BinaryStreamLayer.java \
	./src/kerberos/stack/Layer.java \
	./src/kerberos/stack/MessageParseLayer.java \
	./src/kerberos/stack/QueueLayer.java \
    ./src/kerberos/protocol/SecurityUtilities.java \
	./src/kerberos/protocol/Processable.java \
	./src/kerberos/protocol/SocketAcceptor.java \
	./src/kerberos/protocol/tgs/TGSProtocol.java \
	./src/kerberos/protocol/tgs/TGSConfiguration.java \
	./src/kerberos/protocol/tgs/TicketGrantingServer.java \
	./src/kerberos/protocol/service/Authenticator.java \
	./src/kerberos/protocol/service/ServiceConfiguration.java \
	./src/kerberos/protocol/service/AuthThread.java \
	./src/kerberos/protocol/service/ServiceProtocol.java \
	./src/kerberos/protocol/service/AcceptThread.java \
	./src/kerberos/protocol/client/SocketAddress.java \
	./src/kerberos/protocol/client/ClientConfiguration.java \
	./src/kerberos/protocol/client/ASClientProtocol.java \
	./src/kerberos/protocol/client/TGSClientProtocol.java \
	./src/kerberos/protocol/client/ServiceClientProtocol.java \
	./src/kerberos/protocol/as/ASProtocol.java \
	./src/kerberos/protocol/as/ASConfiguration.java \
	./src/kerberos/protocol/as/AuthenticationServer.java \
	./src/kerberos/protocol/ProtocolServer.java \
	./src/kerberos/socket/KerberosInputStream.java \
	./src/kerberos/socket/KerberosOutputStream.java \
	./src/kerberos/socket/KerberosSocket.java \
	./src/kerberos/socket/KerberosServerSocket.java \
	./src/example/client/MessagePanel.java \
	./src/example/client/SettingsPanel.java \
	./src/example/client/CommunicationPanel.java \
	./src/example/client/InputPanel.java \
	./src/example/client/ClientFrame.java \
    ./src/example/client/Client.java \
    ./src/example/service/TestService.java \
	./src/example/Main.java


default: createdir classes

classes: $(CLASSES:.java=.class)

run:
	java -cp $(CP) example.Main 

createdir:
	mkdir $(DEST)

clean:
	$(RM) -R $(DEST)
