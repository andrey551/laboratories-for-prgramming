package server.utility;

import base.Exception.ClosingSocketException;
import base.Interaction.Request;
import base.Interaction.Response;
import base.Interaction.ResponseStatus;
import server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandle implements Runnable{
    private Server server;
    static volatile Socket clientSocket;
    private CommandManager commandManager;
    private VehicleData vehicleData;


    static volatile Request request = null;
    static volatile  Response response = null;

    public ConnectionHandle(Server server, Socket socket, CommandManager commandManager, VehicleData vehicleData) {
        this.server = server;
        this.clientSocket = socket;
        this.commandManager = commandManager;
        this.vehicleData = vehicleData;
    }
    @Override

   public void run() {
        boolean stopFlag = false;

        try{
            ObjectInputStream clientReader = new ObjectInputStream((clientSocket.getInputStream()));
            ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            do{
                request = (Request) clientReader.readObject();
                Thread process = new Thread(()->{
                    response = new RequestHandle(commandManager, request, vehicleData).compute();
                });
                process.start();
                process.join();
                Thread send = new Thread(()->{
                    try {
                        clientWriter.writeObject(response);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                send.start();
        } while(response.getRes() != ResponseStatus.SERVER_EXIT && response.getRes() != ResponseStatus.CLIENT_EXIT);
            if(response.getRes() == ResponseStatus.SERVER_EXIT) stopFlag = true;
        } catch (IOException e) {
            Output.println("restart the server");
        } catch (ClassNotFoundException e) {
            Output.println("restart the server");
        } catch (InterruptedException e) {
            Output.println("Client has interrupt!");
        } finally {
            try{
                clientSocket.close();
            } catch (IOException e) {
                ResponseOutput.appendln("Restart the client");
            }

            if(stopFlag) {
                try {
                    server.stop();
                    server.releaseConnection();
                } catch (ClosingSocketException e) {
                   Output.println("An error occurred!");
                }
            }
        }
    }
}
