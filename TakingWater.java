import java.io.*;
import java.util.*;

class WaterIntakeLog implements Serializable {
    private String time;
    private double amount;

    public WaterIntakeLog(String time, double amount) {
        this.time = time;
        this.amount = amount;
    }

    public void display() {
        System.out.printf("Time: %s | Amount: %.2f L\n", time, amount);
    }

    public double getAmount() {
        return amount;
    }
}

class HydrationTracker {
    private List<WaterIntakeLog> logs;

    public HydrationTracker() {
        logs = new ArrayList<>();
        loadLogs();
    }

    public void logWaterIntake(String time, double amount) {
        logs.add(new WaterIntakeLog(time, amount));
        saveLogs();
    }

    public void viewLogs() {
        for (WaterIntakeLog log : logs) {
            log.display();
        }
    }

    public void viewTotalIntake() {
        double total = 0;
        for (WaterIntakeLog log : logs) {
            total += log.getAmount();
        }
        System.out.printf("Total Water Intake: %.2f L\n", total);
    }

    private void saveLogs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("hydration_logs.dat"))) {
            oos.writeObject(logs);
        } catch (IOException e) {
            System.err.println("Error saving logs: " + e.getMessage());
        }
    }

    private void loadLogs() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("hydration_logs.dat"))) {
            logs = (List<WaterIntakeLog>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logs = new ArrayList<>();
            System.err.println("Error loading logs: " + e.getMessage());
        }
    }
}

public class TakingWater {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HydrationTracker tracker = new HydrationTracker();
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Reminder: It's time to drink water!");
            }
        }, 0, 3600000); 

        while (true) {
            System.out.println("\nHydration Tracker Menu:");
            System.out.println("1. Log Water Intake");
            System.out.println("2. View Logs");
            System.out.println("3. View Total Intake");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter time (HH:mm): ");
                    String time = scanner.nextLine();
                    System.out.print("Enter amount (L): ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    tracker.logWaterIntake(time, amount);
                    break;
                case 2:
                    tracker.viewLogs();
                    break;
                case 3:
                    tracker.viewTotalIntake();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    timer.cancel();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}