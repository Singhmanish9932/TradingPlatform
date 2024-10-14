import java.util.*;

// Represents a Stock with symbol and price
class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

// Represents a Trader with balance and portfolio
class Trader {
    private String name;
    private double balance;
    private Portfolio portfolio;

    public Trader(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.portfolio = new Portfolio();
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds!");
        }
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void buyStock(Stock stock, int quantity) {
        double totalCost = stock.getPrice() * quantity;
        if (balance >= totalCost) {
            portfolio.addStock(stock, quantity);
            withdraw(totalCost);
            System.out.println("Bought " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("Not enough balance to buy " + stock.getSymbol());
        }
    }

    public void sellStock(Stock stock, int quantity) {
        if (portfolio.hasStock(stock.getSymbol(), quantity)) {
            portfolio.removeStock(stock, quantity);
            deposit(stock.getPrice() * quantity);
            System.out.println("Sold " + quantity + " shares of " + stock.getSymbol());
        } else {
            System.out.println("Not enough shares to sell.");
        }
    }

    public void displayPortfolio() {
        portfolio.display();
    }
}

// Manages the trader's portfolio of stocks
class Portfolio {
    private Map<String, Integer> stocks;

    public Portfolio() {
        this.stocks = new HashMap<>();
    }

    public void addStock(Stock stock, int quantity) {
        stocks.put(stock.getSymbol(), stocks.getOrDefault(stock.getSymbol(), 0) + quantity);
    }

    public void removeStock(Stock stock, int quantity) {
        String symbol = stock.getSymbol();
        if (stocks.containsKey(symbol)) {
            int currentQuantity = stocks.get(symbol);
            if (currentQuantity >= quantity) {
                stocks.put(symbol, currentQuantity - quantity);
                if (stocks.get(symbol) == 0) {
                    stocks.remove(symbol);
                }
            }
        }
    }

    public boolean hasStock(String symbol, int quantity) {
        return stocks.containsKey(symbol) && stocks.get(symbol) >= quantity;
    }

    public void display() {
        if (stocks.isEmpty()) {
            System.out.println("Portfolio is empty.");
        } else {
            System.out.println("Portfolio:");
            for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
                System.out.println("Stock: " + entry.getKey() + ", Quantity: " + entry.getValue());
            }
        }
    }
}

// Simulates market data with fluctuating stock prices
class MarketData {
    private Map<String, Stock> market;

    public MarketData() {
        market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", 150));
        market.put("GOOGL", new Stock("GOOGL", 2800));
        market.put("TSLA", new Stock("TSLA", 800));
    }

    public Stock getStock(String symbol) {
        return market.get(symbol);
    }

    public void simulatePriceChanges() {
        Random random = new Random();
        for (Stock stock : market.values()) {
            double changePercent = (random.nextDouble() - 0.5) * 0.1; // +/- 5% change
            double newPrice = stock.getPrice() * (1 + changePercent);
            stock.setPrice(Math.round(newPrice * 100.0) / 100.0);
        }
    }

    public void displayMarket() {
        System.out.println("Market Data:");
        for (Stock stock : market.values()) {
            System.out.println(stock.getSymbol() + ": $" + stock.getPrice());
        }
    }
}

// Main class to manage the trading platform
public class TradingPlatform {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MarketData marketData = new MarketData();
        Trader trader = new Trader("Manish", 5000.0); // Initial balance

        boolean running = true;
        while (running) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. View Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    marketData.simulatePriceChanges();
                    marketData.displayMarket();
                    break;
                case 2:
                    System.out.print("Enter stock symbol: ");
                    String symbolToBuy = scanner.next().toUpperCase();
                    Stock stockToBuy = marketData.getStock(symbolToBuy);
                    if (stockToBuy != null) {
                        System.out.print("Enter quantity: ");
                        int quantityToBuy = scanner.nextInt();
                        trader.buyStock(stockToBuy, quantityToBuy);
                    } else {
                        System.out.println("Stock not found!");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol: ");
                    String symbolToSell = scanner.next().toUpperCase();
                    Stock stockToSell = marketData.getStock(symbolToSell);
                    if (stockToSell != null) {
                        System.out.print("Enter quantity: ");
                        int quantityToSell = scanner.nextInt();
                        trader.sellStock(stockToSell, quantityToSell);
                    } else {
                        System.out.println("Stock not found!");
                    }
                    break;
                case 4:
                    trader.displayPortfolio();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}
