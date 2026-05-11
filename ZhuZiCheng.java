import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 控制台多功能计算器
 * 功能：加减乘除取模运算、保存计算历史、查看历史记录、清空历史、异常处理
 * 核心知识点：面向对象、集合、异常捕获、数学运算、控制台交互
 */
public class CalculatorApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CalculatorManager manager = new CalculatorManager();

    public static void main(String[] args) {
        System.out.println("===== 欢迎使用多功能计算器 =====");
        boolean isRunning = true;

        while (isRunning) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> calculate();
                    case 2 -> showHistory();
                    case 3 -> clearHistory();
                    case 0 -> {
                        isRunning = false;
                        System.out.println("计算器已退出，感谢使用！");
                    }
                    default -> System.out.println("输入错误，请输入0-3的数字！");
                }
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入有效数字！");
            }
        }
        scanner.close();
    }

    // 打印功能菜单
    private static void printMenu() {
        System.out.println("\n-------- 计算器菜单 --------");
        System.out.println("1. 执行新计算");
        System.out.println("2. 查看计算历史");
        System.out.println("3. 清空计算历史");
        System.out.println("0. 退出计算器");
        System.out.print("请选择操作：");
    }

    // 核心计算逻辑
    private static void calculate() {
        try {
            System.out.print("请输入第一个数字：");
            double num1 = Double.parseDouble(scanner.nextLine());

            System.out.print("请输入运算符(+ - * / %)：");
            String operator = scanner.nextLine().trim();

            System.out.print("请输入第二个数字：");
            double num2 = Double.parseDouble(scanner.nextLine());

            double result = compute(num1, num2, operator);
            // 格式化输出结果
            String record = String.format("%.2f %s %.2f = %.2f", num1, operator, num2, result);
            System.out.println("计算结果：" + record);
            // 保存到历史记录
            manager.addRecord(record);
        } catch (ArithmeticException e) {
            System.out.println("计算错误：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("输入错误：" + e.getMessage());
        }
    }

    // 数学运算方法
    private static double compute(double num1, double num2, String operator) {
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> {
                if (num2 == 0) throw new ArithmeticException("除数不能为0");
                yield num1 / num2;
            }
            case "%" -> {
                if (num2 == 0) throw new ArithmeticException("取模模数不能为0");
                yield num1 % num2;
            }
            default -> throw new IllegalArgumentException("不支持的运算符！");
        };
    }

    // 查看历史记录
    private static void showHistory() {
        List<String> history = manager.getHistory();
        if (history.isEmpty()) {
            System.out.println("暂无计算历史！");
            return;
        }
        System.out.println("\n-------- 计算历史记录 --------");
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, history.get(i));
        }
    }

    // 清空历史记录
    private static void clearHistory() {
        manager.clearAll();
        System.out.println("历史记录已清空！");
    }
}

// 计算器管理类：负责历史记录的增删查清空
class CalculatorManager {
    private final List<String> historyList;

    public CalculatorManager() {
        historyList = new ArrayList<>();
    }

    // 添加记录
    public void addRecord(String record) {
        historyList.add(record);
    }

    // 获取所有记录
    public List<String> getHistory() {
        return new ArrayList<>(historyList);
    }

    // 清空所有记录
    public void clearAll() {
        historyList.clear();
    }
}
