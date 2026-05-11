import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 简易银行账户管理系统
 * 功能：开户、存款、取款、转账、查询余额、查看交易记录
 * 知识点：面向对象封装、集合、自定义异常、异常处理、控制台交互
 */
public class BankSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BankManager bankManager = new BankManager();

    public static void main(String[] args) {
        System.out.println("===== 欢迎使用简易银行管理系统 =====");
        boolean isRun = true;
        while (isRun) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> openAccount();
                    case 2 -> deposit();
                    case 3 -> withdraw();
                    case 4 -> transfer();
                    case 5 -> checkBalance();
                    case 6 -> showTransactions();
                    case 0 -> {
                        isRun = false;
                        System.out.println("系统已退出，祝您生活愉快！");
                    }
                    default -> System.out.println("输入错误，请输入0-6的数字！");
                }
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入有效数字！");
            }
        }
        scanner.close();
    }

    // 打印菜单
    private static void printMenu() {
        System.out.println("\n-------- 银行功能菜单 --------");
        System.out.println("1. 开设新账户");
        System.out.println("2. 账户存款");
        System.out.println("3. 账户取款");
        System.out.println("4. 账户转账");
        System.out.println("5. 查询账户余额");
        System.out.println("6. 查看交易记录");
        System.out.println("0. 退出系统");
        System.out.print("请选择操作：");
    }

    // 开户
    private static void openAccount() {
        System.out.print("请输入账户姓名：");
        String name = scanner.nextLine();
        System.out.print("请输入初始存款金额：");
        double money = Double.parseDouble(scanner.nextLine());
        String accountId = bankManager.addAccount(name, money);
        System.out.println("开户成功！您的账户ID：" + accountId);
    }

    // 存款
    private static void deposit() {
        System.out.print("请输入账户ID：");
        String id = scanner.nextLine();
        System.out.print("请输入存款金额：");
        double money = Double.parseDouble(scanner.nextLine());
        try {
            bankManager.deposit(id, money);
            System.out.println("存款成功！");
        } catch (AccountException e) {
            System.out.println("存款失败：" + e.getMessage());
        }
    }

    // 取款
    private static void withdraw() {
        System.out.print("请输入账户ID：");
        String id = scanner.nextLine();
        System.out.print("请输入取款金额：");
        double money = Double.parseDouble(scanner.nextLine());
        try {
            bankManager.withdraw(id, money);
            System.out.println("取款成功！");
        } catch (AccountException e) {
            System.out.println("取款失败：" + e.getMessage());
        }
    }

    // 转账
    private static void transfer() {
        System.out.print("请输入转出账户ID：");
        String fromId = scanner.nextLine();
        System.out.print("请输入转入账户ID：");
        String toId = scanner.nextLine();
        System.out.print("请输入转账金额：");
        double money = Double.parseDouble(scanner.nextLine());
        try {
            bankManager.transfer(fromId, toId, money);
            System.out.println("转账成功！");
        } catch (AccountException e) {
            System.out.println("转账失败：" + e.getMessage());
        }
    }

    // 查询余额
    private static void checkBalance() {
        System.out.print("请输入账户ID：");
        String id = scanner.nextLine();
        try {
            double balance = bankManager.getBalance(id);
            System.out.printf("账户余额：%.2f 元%n", balance);
        } catch (AccountException e) {
            System.out.println("查询失败：" + e.getMessage());
        }
    }

    // 查看交易记录
    private static void showTransactions() {
        System.out.print("请输入账户ID：");
        String id = scanner.nextLine();
        bankManager.showTransaction(id);
    }
}

// 自定义账户异常
class AccountException extends Exception {
    public AccountException(String message) {
        super(message);
    }
}

// 银行账户实体类
class Account {
    private final String accountId;
    private final String name;
    private double balance;
    private final List<String> transactions;

    public Account(String accountId, String name, double balance) {
        this.accountId = accountId;
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
        addTransaction("开户，初始金额：" + balance);
    }

    // 添加交易记录
    public void addTransaction(String msg) {
        transactions.add("记录：" + msg);
    }

    // Getter/Setter
    public String getAccountId() { return accountId; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public List<String> getTransactions() { return transactions; }
}

// 银行管理核心类
class BankManager {
    private final List<Account> accountList = new ArrayList<>();
    private int idCounter = 10001;

    // 生成唯一账户ID
    private String createId() {
        return "BK" + idCounter++;
    }

    // 开户
    public String addAccount(String name, double money) {
        String id = createId();
        Account account = new Account(id, name, money);
        accountList.add(account);
        return id;
    }

    // 根据ID查找账户
    private Account findAccount(String id) throws AccountException {
        for (Account acc : accountList) {
            if (acc.getAccountId().equals(id)) {
                return acc;
            }
        }
        throw new AccountException("账户不存在");
    }

    // 存款
    public void deposit(String id, double money) throws AccountException {
        if (money <= 0) throw new AccountException("存款金额必须大于0");
        Account acc = findAccount(id);
        acc.setBalance(acc.getBalance() + money);
        acc.addTransaction("存款：+" + money + "，余额：" + acc.getBalance());
    }

    // 取款
    public void withdraw(String id, double money) throws AccountException {
        Account acc = findAccount(id);
        if (money <= 0) throw new AccountException("取款金额必须大于0");
        if (acc.getBalance() < money) throw new AccountException("余额不足");
        acc.setBalance(acc.getBalance() - money);
        acc.addTransaction("取款：-" + money + "，余额：" + acc.getBalance());
    }

    // 转账
    public void transfer(String fromId, String toId, double money) throws AccountException {
        Account from = findAccount(fromId);
        Account to = findAccount(toId);
        if (from.getBalance() < money) throw new AccountException("转出账户余额不足");
        from.setBalance(from.getBalance() - money);
        to.setBalance(to.getBalance() + money);
        from.addTransaction("转账给" + toId + "：-" + money);
        to.addTransaction("收到" + fromId + "转账：+" + money);
    }

    // 查询余额
    public double getBalance(String id) throws AccountException {
        return findAccount(id).getBalance();
    }

    // 查看交易记录
    public void showTransaction(String id) {
        try {
            Account acc = findAccount(id);
            System.out.println("-------- 交易记录 --------");
            acc.getTransactions().forEach(System.out::println);
        } catch (AccountException e) {
            System.out.println("查询失败：" + e.getMessage());
        }
    }
}
