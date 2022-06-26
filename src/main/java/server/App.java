package server;

import base.Exception.NotDeclaredLimitsException;
import base.Exception.WrongAmountOfElementsException;
import server.commands.*;
import server.utility.*;

public class App {
    private static String databaseUsername = "s336186";
    public static  int PORT = 5555;
    private static String databaseHost;
    private static String databasePassword;
    private static String databaseAddress;

    public static void  main(String[] args) {
//        if(!initialize(args)) return ;
//        DatabaseHandle databaseHandle = new DatabaseHandle(databaseAddress, databaseUsername, databasePassword);
        DatabaseHandle databaseHandle = new DatabaseHandle("jdbc:postgresql://localhost:5432/336186", "postgres", "05052001");
        DatabaseUserManager databaseUserManager = new DatabaseUserManager(databaseHandle);
        DatabaseCollectionManager databaseCollectionManager =
                new DatabaseCollectionManager(databaseHandle, databaseUserManager);
        VehicleData vehicleData = new VehicleData(databaseCollectionManager);
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(vehicleData),
                new ShowCommand(vehicleData, databaseCollectionManager),
                new AddCommand(vehicleData, databaseCollectionManager),
                new UpdateIdCommand(vehicleData, databaseCollectionManager),
                new RemoveByIdCommand(vehicleData, databaseCollectionManager),
                new ClearCommand(vehicleData,databaseCollectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new RemoveFirstCommand(vehicleData,databaseCollectionManager),
                new AddIfMaxCommand(vehicleData,databaseCollectionManager),
                new RemoveGreaterCommand(vehicleData, databaseCollectionManager),
                new GroupCountingBycapacityCommand(vehicleData),
                new CountLessThanFuelTypeCommand(vehicleData, databaseCollectionManager),
                new FilterLessThanFuelConsumptionCommand(vehicleData),
                new LoginCommand(databaseUserManager),
                new RegisterCommand(databaseUserManager),
                new RefreshCommand()
        );

        Server server = new Server(PORT, commandManager, vehicleData);
        server.run();
        databaseHandle.closeConnection();
    }

//    private static boolean initialize(String[] args) {
//        try{
//            if(args.length != 3) throw new WrongAmountOfElementsException();
//            PORT = Integer.parseInt(args[0]);
//            if(PORT < 0) throw new NotDeclaredLimitsException();
//            databaseHost = args[1];
//            databasePassword = args[2];
//            databaseAddress = "jdbc:postgresql://" + databaseHost + ":5432/studs";
//            return true;
//        } catch (WrongAmountOfElementsException e ) {
//            String jarName = new java.io.File(App.class.getProtectionDomain()
//                    .getCodeSource()
//                    .getLocation()
//                    .getPath())
//                    .getName();
//            serverOutput.println("Use: 'java -jar " + jarName + " <port> <db_host> <db_password>'");
//        } catch (NumberFormatException e) {
//            serverOutput.printError("Port must be bigger than 0");
//        } catch (NotDeclaredLimitsException e) {
//            serverOutput.printError("Cannot use this port");
//        }
//        return false;
//    }
}
