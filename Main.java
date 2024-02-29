import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class AddCalculation {
    static int add(int num) {
        return num + 10;
    }
}

class MultiplyCalculation {
    static int multiply(int num) {
        return num * 10;
    }
}

class Message {
    private String operation;
    private int number;

    Message(String operation, int number) {
        this.operation = operation;
        this.number = number;
    }

    String getOperation() {
        return operation;
    }

    int getNumber() {
        return number;
    }
}

class MessageQueueConnector {
    private BlockingQueue<Message> queue;

    public MessageQueueConnector(int maxSize) {
        queue = new LinkedBlockingQueue<>(maxSize);
    }

    public void send(Message message) throws InterruptedException {
        queue.put(message);
    }

    public Message receive() throws InterruptedException {
        return queue.take();
    }
}

class Producer implements Runnable {
    private MessageQueueConnector connector;

    Producer(MessageQueueConnector connector) {
        this.connector = connector;
    }

    @Override
    public void run() {
        String[] operations = {"add", "multiply", "multiply", "add", "add", "add", "multiply", "end"};
        int[] numbers = {4, 1, 8, 2, 3, 99, 53, 0};

        for (int i = 0; i < operations.length; i++) {
            try {
                connector.send(new Message(operations[i], numbers[i]));
                System.out.println("Producer sent a message: (" + operations[i] + ", " + numbers[i] + ")");
                Thread.sleep(100); // Simulating some delay between producing messages
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer implements Runnable {
    private MessageQueueConnector connector;

    Consumer(MessageQueueConnector connector) {
        this.connector = connector;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = connector.receive();
                if (message.getOperation().equals("end")) {
                    System.out.println("Consumer received ending message.");
                    System.out.println("Terminating the program");
                    break;
                }

                int result;
                if (message.getOperation().equals("add")) {
                    result = AddCalculation.add(message.getNumber());
                } else {
                    result = MultiplyCalculation.multiply(message.getNumber());
                }

                System.out.println("Consumer received (" + message.getOperation() + ", " + message.getNumber() + "), result: " + result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        MessageQueueConnector connector = new MessageQueueConnector(3); // Limit the queue size to 3

        Thread producerThread = new Thread(new Producer(connector));
        Thread consumerThread = new Thread(new Consumer(connector));

        producerThread.start();
        consumerThread.start();
    }
}
