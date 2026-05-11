import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 个人待办事项管理系统
 * 功能：添加待办、查看所有、修改状态、删除待办、文件持久化(自动保存/加载)
 * 核心知识点：枚举、LocalDateTime、文件IO、集合、异常处理、控制台交互
 */
// 主程序类
public class TodoListApp {
    private static final String FILE_PATH = "todo_list.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        TodoManager manager = new TodoManager(FILE_PATH);
        // 启动时加载本地数据
        manager.loadFromFile();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("===== 欢迎使用个人待办事项管理系统 =====");
        while (running) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> addTodo(manager, scanner);
                    case 2 -> manager.showAllTodos();
                    case 3 -> updateTodoStatus(manager, scanner);
                    case 4 -> deleteTodo(manager, scanner);
                    case 0 -> {
                        manager.saveToFile();
                        running = false;
                        System.out.println("数据已保存，系统退出成功！");
                    }
                    default -> System.out.println("输入错误，请输入0-4的数字！");
                }
            } catch (Exception e) {
                System.out.println("输入格式错误，请输入数字！");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    // 打印菜单
    private static void printMenu() {
        System.out.println("\n-------- 功能菜单 --------");
        System.out.println("1. 添加新的待办事项");
        System.out.println("2. 查看所有待办事项");
        System.out.println("3. 修改待办完成状态");
        System.out.println("4. 删除待办事项");
        System.out.println("0. 退出系统并保存数据");
        System.out.print("请输入操作序号：");
    }

    // 添加待办
    private static void addTodo(TodoManager manager, Scanner scanner) {
        System.out.print("请输入待办标题：");
        String title = scanner.nextLine();
        System.out.print("请输入待办详情：");
        String content = scanner.nextLine();
        TodoItem todo = new TodoItem(title, content);
        manager.addTodo(todo);
        System.out.println("待办事项添加成功！");
    }

    // 修改状态
    private static void updateTodoStatus(TodoManager manager, Scanner scanner) {
        System.out.print("请输入要修改的待办ID：");
        int id = scanner.nextInt();
        System.out.print("请输入新状态(1-已完成，0-未完成)：");
        int status = scanner.nextInt();
        manager.updateStatus(id, status == 1);
    }

    // 删除待办
    private static void deleteTodo(TodoManager manager, Scanner scanner) {
        System.out.print("请输入要删除的待办ID：");
        int id = scanner.nextInt();
        manager.deleteTodo(id);
    }
}

// 待办状态枚举（未完成/已完成）
enum TodoStatus {
    UNFINISHED("未完成"), FINISHED("已完成");
    private final String desc;

    TodoStatus(String desc) { this.desc = desc; }
    public String getDesc() { return desc; }
}

// 待办事项实体类
class TodoItem {
    private static int idCounter = 1;
    private final int id;
    private final String title;
    private final String content;
    private TodoStatus status;
    private final String createTime;

    public TodoItem(String title, String content) {
        this.id = idCounter++;
        this.title = title;
        this.content = content;
        this.status = TodoStatus.UNFINISHED;
        this.createTime = LocalDateTime.now().format(TodoListApp.FORMATTER);
    }

    // Getter & Setter
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public TodoStatus getStatus() { return status; }
    public void setStatus(TodoStatus status) { this.status = status; }
    public String getCreateTime() { return createTime; }

    @Override
    public String toString() {
        return String.format("ID：%d | 标题：%s | 状态：%s | 创建时间：%s\n详情：%s\n",
                id, title, status.getDesc(), createTime, content);
    }
}

// 待办管理核心类
class TodoManager {
    private final List<TodoItem> todoList;
    private final String filePath;

    public TodoManager(String filePath) {
        this.todoList = new ArrayList<>();
        this.filePath = filePath;
    }

    // 添加待办
    public void addTodo(TodoItem todo) { todoList.add(todo); }

    // 查看所有
    public void showAllTodos() {
        if (todoList.isEmpty()) {
            System.out.println("暂无待办事项！");
            return;
        }
        System.out.println("\n-------- 所有待办事项 --------");
        todoList.forEach(System.out::println);
    }

    // 修改状态
    public void updateStatus(int id, boolean isFinished) {
        for (TodoItem todo : todoList) {
            if (todo.getId() == id) {
                todo.setStatus(isFinished ? TodoStatus.FINISHED : TodoStatus.UNFINISHED);
                System.out.println("状态修改成功！");
                return;
            }
        }
        System.out.println("未找到该ID的待办事项！");
    }

    // 删除待办
    public void deleteTodo(int id) {
        for (TodoItem todo : todoList) {
            if (todo.getId() == id) {
                todoList.remove(todo);
                System.out.println("待办事项删除成功！");
                return;
            }
        }
        System.out.println("未找到该ID的待办事项！");
    }

    // 保存数据到文件
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (TodoItem todo : todoList) {
                writer.write(String.format("%s|%s|%s|%s|%s%n",
                        todo.getId(), todo.getTitle(), todo.getContent(),
                        todo.getStatus().name(), todo.getCreateTime()));
            }
        } catch (IOException e) {
            System.out.println("数据保存失败：" + e.getMessage());
        }
    }

    // 从文件加载数据
    public void loadFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                // 重置ID计数器
                TodoItem.idCounter = Integer.parseInt(data[0]) + 1;
            }
            System.out.println("成功加载本地历史待办数据！");
        } catch (IOException e) {
            System.out.println("数据加载失败：" + e.getMessage());
        }
    }
}
