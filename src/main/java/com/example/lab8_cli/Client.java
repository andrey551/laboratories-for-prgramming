package com.example.lab8_cli;

import base.Interaction.Request;
import base.Interaction.Response;
import base.Interaction.ResponseStatus;
import base.Interaction.User;

import base.Exception.ConnectionErrorException;
import base.Exception.NotDeclaredLimitsException;
import base.Vehicle.Vehicle;
import com.example.lab8_cli.controller.MainWindowController;
import com.example.lab8_cli.utility.Outputer;
import com.example.lab8_cli.utility.OutputerUI;
import com.example.lab8_cli.utility.ScriptHandle;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;

public class Client implements Runnable{
    private String host;
    private int port;
    private SocketChannel socketChannel;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private User user;
    private boolean isConnected;


    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }
    @Override
    public void run () {
        try {
            connectToServer();
        } catch (NotDeclaredLimitsException e) {
            Outputer.printError("ClientException");
            System.exit(0);
        } catch(ConnectionErrorException e) {
           // e.printStackTrace();
        }
    }

    public void stop() {
        try{
            processRequestToServer(MainWindowController.EXIT_COMMAND_NAME, "", null);
            socketChannel.close();
            Outputer.println("EndWorkOfClient");
        } catch(IOException e) {
            Outputer.printError("EndWorkOfClientException");
            if (socketChannel == null) Outputer.printError("EndRunningWorkOfClientException");
        }
    }

    public Response processRequestToServer(String commandName, String commandStringArgument,
                                                      Serializable commandObjectArgument) {
        Request requestToServer = null;
        Response serverResponse = null;

        try{
            requestToServer = new Request(commandName, commandStringArgument, commandObjectArgument, user);
            objectOutputStream.writeObject(requestToServer);
            serverResponse = (Response) objectInputStream.readObject();
            if(!serverResponse.getBody().isEmpty()) {
                OutputerUI.tryError(serverResponse.getBody(), serverResponse.getResponseBodyArgs());
            }
        } catch(InvalidClassException | NotSerializableException e) {
            OutputerUI.error("DataSendingException");
        } catch(ClassNotFoundException e) {
            OutputerUI.error("DataReadingException");
        } catch(IOException e) {
            if(requestToServer.getCommandName().equals(MainWindowController.EXIT_COMMAND_NAME))
                return null;
            OutputerUI.error("EndConnectionToServerException");
            try{
                connectToServer();
                OutputerUI.info("ConnectionToServerComplete");
            } catch ( ConnectionErrorException| NotDeclaredLimitsException eq) {
                OutputerUI.info("TryCommandLater");
            }
        }
        return serverResponse ;
    }

    public boolean processScriptToServer(File scriptFile) {
        Request requestToServer = null;
        Response serverResponse = null;
        ScriptHandle scriptHandle = new ScriptHandle(scriptFile);
        do{
            try{
                requestToServer = serverResponse!=null ? scriptHandle.handle(serverResponse.getRes(), user):
                        scriptHandle.handle(null, user);
                if(requestToServer == null) return false;
                if(requestToServer.isEmpty()) continue;
                objectOutputStream.writeObject(requestToServer);
                serverResponse = (Response) objectInputStream.readObject();
                if(!serverResponse.getBody().isEmpty())
                    OutputerUI.tryError(serverResponse.getBody(), serverResponse.getResponseBodyArgs());
            } catch (ClassNotFoundException exception) {
                OutputerUI.error("DataReadingException");
            } catch (IOException exception) {
                OutputerUI.error("EndConnectionToServerException");
                try {
                    connectToServer();
                    OutputerUI.info("ConnectionToServerComplete");
                } catch (ConnectionErrorException | NotDeclaredLimitsException reconnectionException) {
                    OutputerUI.info("TryCommandLater");
                }
            }
        } while (!requestToServer.getCommandName().equals("exit"));
        return true;
    }

    public boolean processAuthentication(String username, String password, boolean register) {
        Request requestToServer = null;
        Response serverResponse = null;
        String command;
        try {
            command = register ? MainWindowController.REGISTER_COMMAND_NAME : MainWindowController.LOGIN_COMMAND_NAME;
            requestToServer = new Request(command, "", new User(username, password));
            if (objectOutputStream == null) throw new IOException();
            objectOutputStream.writeObject(requestToServer);
            serverResponse = (Response) objectInputStream.readObject();
            OutputerUI.tryError(serverResponse.getBody(), serverResponse.getResponseBodyArgs());
        } catch (InvalidClassException | NotSerializableException exception) {
            OutputerUI.error("DataSendingException");
        } catch (ClassNotFoundException exception) {
            OutputerUI.error("DataReadingException");
        } catch (IOException exception) {
            OutputerUI.error("EndConnectionToServerException");
            try {
                connectToServer();
                OutputerUI.info("ConnectionToServerComplete");
            } catch (ConnectionErrorException | NotDeclaredLimitsException reconnectionException) {
                OutputerUI.info("TryAuthLater");
            }
        }
        if (serverResponse != null && serverResponse.getRes().equals(ResponseStatus.OK)) {
            user = requestToServer.getUser();
            return true;
        }
        return false;
    }
    public boolean isConnected() {
        return isConnected;
    }

    public String getUsername() {
        return user == null ? null : user.getUsername();
    }

    private void connectToServer() throws ConnectionErrorException, NotDeclaredLimitsException {
        try {
            Outputer.println("ConnectionToServer");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            objectOutputStream = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            objectInputStream = new ObjectInputStream(socketChannel.socket().getInputStream());
            isConnected = true;
            Outputer.println("ConnectionToServerComplete");
        } catch (IllegalArgumentException exception) {
            Outputer.printError("ServerAddressException");
            isConnected = false;
            throw new NotDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printError("ConnectionToServerException");
            isConnected = false;
            throw new ConnectionErrorException();
        }
    }
}
