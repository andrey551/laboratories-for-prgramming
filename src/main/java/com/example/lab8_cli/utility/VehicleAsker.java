package com.example.lab8_cli.utility;

import base.Exception.IncorrectInputScriptException;
import base.Exception.MustBeNotEmptyException;
import base.Exception.NotDeclaredLimitsException;
import base.Vehicle.Coordinates;
import base.Vehicle.FuelType;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks a user a marine's value.
 */
public class VehicleAsker {
    private final Integer MAX_X = 717;
    private final double MAX_Y = 12.0;

    public static final String PS1 = ">";
    public static final String PS2 = "$";

    private Scanner userScanner;
    private boolean fileMode;

    public VehicleAsker(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    /**
     * Sets a scanner to scan user input.
     * @param userScanner Scanner to set.
     */
    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * @return Scanner, which uses for user input.
     */
    public Scanner getUserScanner() {
        return this.userScanner;
    }

    /**
     * Sets vehicle asker mode to 'File Mode'.
     */
    public void setFileMode() {
        this.fileMode = true;
    }

    /**
     * Sets vehicle asker mode to 'User Mode'.
     */
    public void setUserMode() {
        this.fileMode = false;
    }

    /**
     * Ask vehicle's name
     * @return Vehicle's name
     * @throws IncorrectInputScriptException
     * @throws MustBeNotEmptyException
     */
    public String askName() throws IncorrectInputScriptException, MustBeNotEmptyException{
        String name;
        while(true) {
            try{
                Outputer.println("EnterName");
                Outputer.print(PS2);
                name = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(name);
                if(name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch(NoSuchElementException e) {
                Outputer.printError("NameNotIdentifiedException");
                if(fileMode) throw new IncorrectInputScriptException();
            } catch (MustBeNotEmptyException e) {
                Outputer.printError("NameEmptyException");
                if(fileMode) throw new IncorrectInputScriptException();
            } catch(IllegalStateException e) {
                Outputer.printError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        }

        return name;
    }

    /**
     * Ask coordinate X
     * @return X coordinate (Integer)
     * @throws IncorrectInputScriptException
     */
    public Integer askX() throws IncorrectInputScriptException{
        String strX;
        Integer x;
        while (true) {
            try {
                Outputer.println("EnterX");
                Outputer.print(PS2);
                strX = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strX);
                x = Integer.parseInt(strX);
                if (x > MAX_X) throw new NotDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printError("XNotIdentifiedException");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NotDeclaredLimitsException exception) {
                Outputer.printError("Coordinate must be smaller than " + MAX_X + "!");
                if (fileMode) throw new IncorrectInputScriptException();
            }catch (NumberFormatException exception) {
                Outputer.printError("XMustBeNumberException");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Ask Y coordinate
     * @return coordinate Y ( double)
     * @throws IncorrectInputScriptException
     */
    public double askY() throws IncorrectInputScriptException {
        String strY;
        double y;
        while (true) {
            try {
                Outputer.println("EnterY", String.valueOf(MAX_Y+1));
                Outputer.print(PS2);
                strY = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strY);
                y = Double.parseDouble(strY);
                if (y > MAX_Y) throw new NotDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printError("YNotIdentifiedException");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NotDeclaredLimitsException exception) {
                Outputer.printError("YMustBeLessException", String.valueOf(MAX_Y));
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printError("YMustBeNumberException");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("UnexpectedException");
                OutputerUI.error("UnexpectedException");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Ask vehicle coordinate
     * @return Vehicle coordinate
     * @throws IncorrectInputScriptException
     */
    public Coordinates askCoordinates() throws IncorrectInputScriptException {
        Integer x;
        double y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Ask vehicle engine power
     * @return Vehicle engine power
     * @throws IncorrectInputScriptException
     */
    public Double askEnginePower() throws IncorrectInputScriptException {
        String strEnginePower;
        Double enginePower;
        while(true) {
            try{
                Outputer.println("Type engine power: ");
                Outputer.print(PS2);
                strEnginePower = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strEnginePower);
                enginePower = Double.parseDouble(strEnginePower);
                if(enginePower <= 0) throw new NotDeclaredLimitsException();
                break;
            } catch (NotDeclaredLimitsException e){
                Outputer.printError("Input must be bigger than 0");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printError("Must be a double number");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Buggggg");
                System.exit(0);                
            }
        }
        
        return enginePower;
    }

    /**
     * Ask vehicle capacity
     * @return Vehicle capacity
     * @throws IncorrectInputScriptException
     */
    public Long askCapacity() throws IncorrectInputScriptException {
        String strCapacity;
        Long capacity;
        
        while(true) {
            try{
                Outputer.println("Type capacity: ");
                Outputer.print(PS2);
                strCapacity = userScanner.nextLine().trim();
                capacity = Long.parseLong(strCapacity);
                if(capacity <= 0) throw new NotDeclaredLimitsException();
                break;
            } catch (NotDeclaredLimitsException e){
                Outputer.printError("Input must be bigger than 0");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printError("Must be a double number");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Buggggg");
                System.exit(0);                
            }
        }
        
        return capacity;
    }

    /**
     * Ask vehicle fuel consumption
     * @return Vehicle fuel consumption
     * @throws IncorrectInputScriptException
     */
    public int askFuelConsumption() throws IncorrectInputScriptException{
        String strFuelConsumption;
        Integer fuelConsumption;
        while(true) {
            try{
                Outputer.println("Type fuel consumption: ");
                Outputer.print(PS2);
                strFuelConsumption = userScanner.nextLine().trim();
                fuelConsumption = Integer.parseInt(strFuelConsumption);
                if(fuelConsumption <= 0) throw new NotDeclaredLimitsException();
                break;
            } catch (NotDeclaredLimitsException e){
                Outputer.printError("Input must be bigger than 0");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printError("Must be a double number");
                if (fileMode) throw new IncorrectInputScriptException();
            }catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("buggggg!");
                System.exit(0);
            }
        }

        return (int)fuelConsumption;
    }

    /**
     * Ask vehicle fuel type
     * @return Vehicle fuel type
     * @throws IncorrectInputScriptException
     */
    public FuelType askFuelType() throws IncorrectInputScriptException {
        String strFuelType;
        FuelType fuel;

        while(true) {
            try{
                Outputer.println("List of Fuel Type: " + FuelType.nameList());
                Outputer.println("Type Fuel Type: ");
                Outputer.print(PS2);
                strFuelType = userScanner.nextLine().trim();
                if(fileMode) Outputer.println(strFuelType);
                fuel = FuelType.valueOf(strFuelType.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printError("Input is not legal!");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printError("Input must be a type of Fuel!");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printError("Bug...Bug...Bug...");
                System.exit(0);
            }
        }

        return fuel;
    }

    /**
     *
     * @param question
     * @return boolean mode
     * @throws IncorrectInputScriptException
     */
    public boolean askQuestion(String question) throws IncorrectInputScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(PS2);
                answer = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printError("Answer is not legal!");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (NotDeclaredLimitsException exception) {
                Outputer.printError("Answer must be represent by '+' or '-'!");
                if (fileMode) throw new IncorrectInputScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printError("bug>output");
                System.exit(0);
            }
        }
        return (answer.equals("+")) ? true : false;
    }    

    @Override
    public String toString() {
        return "VehicleAsker (class for helping user request)";
    }
    

}
