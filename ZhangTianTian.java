import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 简易图书管理系统
 * 功能：添加图书、删除图书、查询所有图书、按编号搜索、按名称搜索、退出系统
 * 核心知识点：面向对象封装、ArrayList集合、控制台交互、异常处理、字符串匹配
 */
public class BookManagementSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookManager bookManager = new BookManager();

    public static void main(String[] args) {
        System.out.println("===== 欢迎使用简易图书管理系统 =====");
        boolean isRunning = true;

        while (isRunning) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1 -> addBook();
                    case 2 -> deleteBook();
                    case 3 -> showAllBooks();
                    case 4 -> searchByBookId();
                    case 5 -> searchByBookName();
                    case 0 -> {
                        isRunning = false;
                        System.out.println("系统已退出，感谢使用！");
                    }
                    default -> System.out.println("输入错误，请输入0-5的数字！");
                }
            } catch (NumberFormatException e) {
                System.out.println("输入格式错误，请输入有效数字！");
            }
        }
        scanner.close();
    }

    // 打印功能菜单
    private static void printMenu() {
        System.out.println("\n-------- 图书管理菜单 --------");
        System.out.println("1. 添加新图书");
        System.out.println("2. 删除图书");
        System.out.println("3. 查看所有图书");
        System.out.println("4. 按图书编号查询");
        System.out.println("5. 按图书名称查询");
        System.out.println("0. 退出系统");
        System.out.print("请选择操作：");
    }

    // 添加图书
    private static void addBook() {
        System.out.print("请输入图书编号：");
        String bookId = scanner.nextLine();
        System.out.print("请输入图书名称：");
        String bookName = scanner.nextLine();
        System.out.print("请输入图书作者：");
        String author = scanner.nextLine();
        System.out.print("请输入图书价格：");
        double price = Double.parseDouble(scanner.nextLine());

        Book book = new Book(bookId, bookName, author, price);
        boolean result = bookManager.addBook(book);
        System.out.println(result ? "图书添加成功！" : "添加失败：图书编号已存在！");
    }

    // 删除图书
    private static void deleteBook() {
        System.out.print("请输入要删除的图书编号：");
        String bookId = scanner.nextLine();
        boolean result = bookManager.deleteBook(bookId);
        System.out.println(result ? "图书删除成功！" : "删除失败：未找到该图书！");
    }

    // 查看所有图书
    private static void showAllBooks() {
        List<Book> books = bookManager.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("暂无图书信息！");
            return;
        }
        System.out.println("\n-------- 所有图书列表 --------");
        for (Book book : books) {
            System.out.println(book);
        }
    }

    // 按编号查询图书
    private static void searchByBookId() {
        System.out.print("请输入图书编号：");
        String bookId = scanner.nextLine();
        Book book = bookManager.getBookById(bookId);
        System.out.println(book != null ? book : "未查询到该图书！");
    }

    // 按名称查询图书
    private static void searchByBookName() {
        System.out.print("请输入图书名称：");
        String name = scanner.nextLine();
        List<Book> books = bookManager.getBooksByName(name);
        if (books.isEmpty()) {
            System.out.println("未查询到匹配的图书！");
            return;
        }
        System.out.println("\n-------- 查询结果 --------");
        books.forEach(System.out::println);
    }
}

// 图书实体类（封装图书属性）
class Book {
    private final String bookId;    // 图书编号（唯一）
    private final String bookName;  // 图书名称
    private final String author;    // 作者
    private final double price;     // 价格

    public Book(String bookId, String bookName, String author, double price) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.author = author;
        this.price = price;
    }

    // Getter方法
    public String getBookId() { return bookId; }
    public String getBookName() { return bookName; }

    // 重写toString，格式化输出图书信息
    @Override
    public String toString() {
        return "图书信息 → 编号：" + bookId +
                " | 名称：" + bookName +
                " | 作者：" + author +
                " | 价格：" + price + "元";
    }
}

// 图书管理核心类（业务逻辑）
class BookManager {
    private final List<Book> bookList;

    public BookManager() {
        bookList = new ArrayList<>();
    }

    // 添加图书（校验编号唯一性）
    public boolean addBook(Book book) {
        for (Book b : bookList) {
            if (b.getBookId().equals(book.getBookId())) {
                return false;
            }
        }
        bookList.add(book);
        return true;
    }

    // 删除图书（根据编号）
    public boolean deleteBook(String bookId) {
        for (Book b : bookList) {
            if (b.getBookId().equals(bookId)) {
                bookList.remove(b);
                return true;
            }
        }
        return false;
    }

    // 获取所有图书
    public List<Book> getAllBooks() {
        return new ArrayList<>(bookList);
    }

    // 按编号精确查询
    public Book getBookById(String bookId) {
        for (Book b : bookList) {
            if (b.getBookId().equals(bookId)) {
                return b;
            }
        }
        return null;
    }

    // 按名称模糊查询
    public List<Book> getBooksByName(String name) {
        List<Book> result = new ArrayList<>();
        for (Book b : bookList) {
            if (b.getBookName().contains(name)) {
                result.add(b);
            }
        }
        return result;
    }
}
