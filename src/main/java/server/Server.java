package server;

import base.Exception.ClosingSocketException;
import base.Exception.ConnectionErrorException;
import base.Exception.OpenServerSocketException;
import server.utility.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.*;

public class Server {
    private final int MAX_THREAD = Integer.MAX_VALUE;
    private int port;
    private ServerSocket serverSocket;
    private boolean isStopped;
    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(MAX_THREAD);
    private CommandManager commandManager;
    private VehicleData vehicleData;
    private Semaphore semaphore;


    public Server(int port, CommandManager commandManager, VehicleData vehicleData){
        this.port = port;
        this.commandManager = commandManager;
        this.vehicleData = vehicleData;
        this.semaphore = new Semaphore(MAX_THREAD);
    }

    public void run() {
        try{
            openServerSocket();
            while(!isStopped()) {
                try{
                    acquireConnection();
                    if(isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = connectToClient();
                    fixedThreadPool.submit(new ConnectionHandle(this, clientSocket, commandManager, vehicleData));
                } catch (ConnectionErrorException e) {
                    if(!isStopped()) {
                        ResponseOutput.appendError("An error occurred while processing request!");
                    } else break;
                }
            }
            fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Output.println("This session has finished");
        } catch (OpenServerSocketException e) {
            Output.printError("Cannot start the server!");
        } catch (InterruptedException| IOException e) {
            Output.printError("An error occurred while shutting down already connected clients!");
        }
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    public void acquireConnection() {
        try{
            semaphore.acquire();
            Output.println("Permission for a new connection has been received.");
        } catch (InterruptedException e) {
            Output.printError("An error occurred while asking a new connection");
        }
    }

    public void releaseConnection() {
        semaphore.release();
        Output.println("Connection disconnect registered.");
    }

    public void stop() throws ClosingSocketException {
        try{
            if(serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            serverSocket.close();
            fixedThreadPool.shutdown();
            Output.println("Session has finished!");
        } catch (IOException | ClosingSocketException e) {
            Output.printError("Error when turn off the server!");
        }
    }
    public void openServerSocket() throws IOException, OpenServerSocketException {
        try {
            Output.println("Starting the server socket");
            serverSocket = new ServerSocket(port);
            Output.println("Server socket has started");
        } catch (IllegalArgumentException e) {
            Output.printError("Port " + port + " is not available!");
            throw new OpenServerSocketException();
        } catch (IOException e) {
            Output.printError("Cannot connect to port " + port );
            throw new OpenServerSocketException();
        }
    }

    public Socket connectToClient () throws ConnectionErrorException {
        try {
            Output.println("Connecting...");
            Socket socket = serverSocket.accept();
            Output.println("Connection has setup successful!");
            return socket;
        }catch (SocketTimeoutException e) {
            Output.printError("Process has time out!");
            throw new ConnectionErrorException();
        } catch (IOException e) {
            Output.printError("Problems connecting to the server");
            throw new ConnectionErrorException();
        }
    }



}
