package vyshkat.console;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import vyshkat.exception.DomainValidationException;
import vyshkat.exception.DuplicateInventoryNumberException;
import vyshkat.model.*;
import vyshkat.service.FleetService;

// Runs the command line menu
@Component
@Profile("!test")
public final class ConsoleRunner implements CommandLineRunner {

    private final FleetService fleetService;
    private final Scanner scanner;
    private final PrintStream output;

    // Creates the command line menu
    @Autowired
    public ConsoleRunner(final FleetService service) {
        this(service, System.in, System.out);
    }

    ConsoleRunner(
            final FleetService service,
            final InputStream input,
            final PrintStream printStream
    ) {
        this.fleetService = service;
        this.scanner = new Scanner(input);
        this.output = printStream;
    }

    @Override
    public void run(final String... args) {
        output.println("Fleet service management");

        while (true) {
            printMenu();
            if (!scanner.hasNextLine()) {
                return;
            }

            String command = scanner.nextLine().trim();
            try {
                if (execute(command)) {
                    return;
                }
            } catch (DomainValidationException
                     | DuplicateInventoryNumberException
                     | NumberFormatException exception) {
                output.println("Error: " + exception.getMessage());
            }
        }
    }

    private boolean execute(final String command) {
        MenuAction action = MenuAction.fromCode(command);
        return switch (action) {
            case ADD_TRANSPORT -> {
                addTransport();
                yield false;
            }
            case ADD_EQUIPMENT -> {
                addEquipment();
                yield false;
            }
            case SHOW_TRANSPORT -> {
                printTransport();
                yield false;
            }
            case SHOW_INVENTORY -> {
                printInventory();
                yield false;
            }
            case SHOW_SUMMARY -> {
                printSummary();
                yield false;
            }
            case SHOW_BEGINNER_TRANSPORT -> {
                printBeginnerTransport();
                yield false;
            }
            case EXIT -> {
                output.println("Goodbye");
                yield true;
            }
            case UNKNOWN -> {
                output.println("Unknown command");
                yield false;
            }
        };
    }

    private void printMenu() {
        output.println();
        output.println("1 - Add transport");
        output.println("2 - Add equipment");
        output.println("3 - Show transport");
        output.println("4 - Show all inventory");
        output.println("5 - Show summary");
        output.println("6 - Show beginner transport");
        output.println("0 - Exit");
        output.print("Choose an action: ");
    }

    private void addTransport() {
        output.println("1 - Scooter");
        output.println("2 - Bicycle");
        output.println("3 - Electric bicycle");
        output.println("4 - Custom transport");

        TransportType type = TransportType.fromCode(
                readText("Choose transport type: ")
        );
        String number = readText("Inventory number: ");
        int beginnerEase = readInt("Beginner level (1-10): ");
        boolean serviceable = readBoolean("Serviceable (yes/no): ");

        Transport transport = switch (type) {
            case SCOOTER -> new Scooter(
                    number,
                    beginnerEase,
                    serviceable,
                    readDouble("Daily energy consumption: ")
            );
            case BICYCLE -> new Bicycle(
                    number,
                    beginnerEase,
                    serviceable
            );
            case ELECTRIC_BICYCLE -> new ElectricBicycle(
                    number,
                    beginnerEase,
                    serviceable,
                    readDouble("Daily energy consumption: ")
            );
            case NEW_TRANSPORT -> new CustomTransport(
                        readText("Transport name: "),
                        number,
                        beginnerEase,
                        serviceable
            );
            case UNKNOWN -> throw new DomainValidationException(
                    "Unknown transport type"
            );
        };

        if (fleetService.acceptTransport(transport)) {
            output.println("Transport accepted.");
        } else {
            output.println("Transport failed inspection.");
        }
    }

    private void addEquipment() {
        output.println("1 - Helmet");
        output.println("2 - Charging station");

        EquipmentType type = EquipmentType.fromCode(
                readText("Choose equipment type: ")
        );
        String number = readText("Inventory number: ");

        switch (type) {
            case HELMET -> fleetService.addEquipment(new Helmet(number));
            case CHARGING_STATION -> fleetService.addEquipment(
                    new ChargingStation(
                    number,
                    readDouble("Daily energy consumption: ")
                    )
            );
            case UNKNOWN -> throw new DomainValidationException(
                    "Unknown equipment type"
            );
        }
        output.println("Equipment added.");
    }

    private void printTransport() {
        output.println("Transport");
        fleetService.inventoryItems().stream()
                .filter(Transport.class::isInstance)
                .map(Transport.class::cast)
                .forEach(this::printTransportItem);
    }

    private void printInventory() {
        output.println("Inventory");
        fleetService.inventoryItems().forEach(this::printInventoryItem);
    }

    private void printSummary() {
        output.println("Summary");
        output.println("Transport: " + fleetService.transportCount());
        output.println("Equipment: " + fleetService.equipmentCount());
        output.println("Total items: " + fleetService.totalInventoryCount());
        output.printf(
                "Daily energy consumption: %.2f kWh%n",
                fleetService.totalDailyEnergyConsumption()
        );
    }

    private void printBeginnerTransport() {
        output.println("Beginner transport");
        fleetService.beginnerFriendlyTransport()
                .forEach(this::printTransportItem);
    }

    private void printTransportItem(final Transport transport) {
        output.printf(
                "%s - item # %s, beginner level: %d%n",
                transport.name(),
                transport.inventoryNumber(),
                transport.beginnerEase()
        );
    }

    private void printInventoryItem(final InventoryItem item) {
        output.printf(
                "%s - item # %s%n",
                item.name(),
                item.inventoryNumber()
        );
    }

    private String readText(final String prompt) {
        output.print(prompt);
        if (!scanner.hasNextLine()) {
            throw new DomainValidationException("Input ended");
        }
        return scanner.nextLine().trim();
    }

    private int readInt(final String prompt) {
        return Integer.parseInt(readText(prompt));
    }

    private double readDouble(final String prompt) {
        return Double.parseDouble(readText(prompt));
    }

    private boolean readBoolean(final String prompt) {
        String answer = readText(prompt);
        if (answer.equalsIgnoreCase("yes")) {
            return true;
        }
        if (answer.equalsIgnoreCase("no")) {
            return false;
        }
        throw new DomainValidationException("Enter yes or no");
    }
}
