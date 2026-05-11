import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 学生成绩管理系统
 * 功能：添加学生、删除学生、修改成绩、查询所有学生、查询最高分、统计班级平均分
 * 纯控制台运行，无第三方依赖
 */
// 主程序类
public class StudentScoreManagementSystem {
    public static void main(String[] args) {
        // 初始化成绩管理器
        ScoreManager manager = new ScoreManager();
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        System.out.println("===== 欢迎使用学生成绩管理系统 =====");
        // 系统主循环
        while (isRunning) {
            printMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 吸收换行符

                switch (choice) {
                    case 1:
                        addStudent(manager, scanner);
                        break;
                    case 2:
                        deleteStudent(manager, scanner);
                        break;
                    case 3:
                        updateStudentScore(manager, scanner);
                        break;
                    case 4:
                        manager.showAllStudents();
                        break;
                    case 5:
                        manager.showHighestScoreStudent();
                        break;
                    case 6:
                        manager.showClassAverageScore();
                        break;
                    case 0:
                        isRunning = false;
                        System.out.println("系统已退出，感谢使用！");
                        break;
                    default:
                        System.out.println("输入错误，请输入0-6之间的数字！");
                }
            } catch (Exception e) {
                System.out.println("输入格式错误，请输入数字！");
                scanner.nextLine(); // 清空错误输入
            }
        }
        scanner.close();
    }

    // 打印系统菜单
    private static void printMenu() {
        System.out.println("\n-------- 功能菜单 --------");
        System.out.println("1. 添加学生信息");
        System.out.println("2. 删除学生信息");
        System.out.println("3. 修改学生成绩");
        System.out.println("4. 查看所有学生");
        System.out.println("5. 查看总分最高学生");
        System.out.println("6. 查看班级各科平均分");
        System.out.println("0. 退出系统");
        System.out.print("请输入操作序号：");
    }

    // 添加学生逻辑
    private static void addStudent(ScoreManager manager, Scanner scanner) {
        System.out.print("请输入学生ID：");
        String id = scanner.nextLine();
        System.out.print("请输入学生姓名：");
        String name = scanner.nextLine();
        System.out.print("请输入语文成绩：");
        double chinese = scanner.nextDouble();
        System.out.print("请输入数学成绩：");
        double math = scanner.nextDouble();
        System.out.print("请输入英语成绩：");
        double english = scanner.nextDouble();

        Student student = new Student(id, name, chinese, math, english);
        manager.addStudent(student);
    }

    // 删除学生逻辑
    private static void deleteStudent(ScoreManager manager, Scanner scanner) {
        System.out.print("请输入要删除的学生ID：");
        String id = scanner.nextLine();
        manager.deleteStudentById(id);
    }

    // 修改成绩逻辑
    private static void updateStudentScore(ScoreManager manager, Scanner scanner) {
        System.out.print("请输入要修改的学生ID：");
        String id = scanner.nextLine();
        System.out.print("请输入新的语文成绩：");
        double chinese = scanner.nextDouble();
        System.out.print("请输入新的数学成绩：");
        double math = scanner.nextDouble();
        System.out.print("请输入新的英语成绩：");
        double english = scanner.nextDouble();

        manager.updateScore(id, chinese, math, english);
    }
}

// 学生实体类（封装学生属性和行为）
class Student {
    private final String id;       // 学生ID
    private final String name;     // 学生姓名
    private double chinese;        // 语文成绩
    private double math;           // 数学成绩
    private double english;        // 英语成绩

    // 构造方法
    public Student(String id, String name, double chinese, double math, double english) {
        this.id = id;
        this.name = name;
        this.chinese = chinese;
        this.math = math;
        this.english = english;
    }

    // 计算总分
    public double getTotalScore() {
        return chinese + math + english;
    }

    // Getter方法
    public String getId() { return id; }
    public String getName() { return name; }
    public double getChinese() { return chinese; }
    public double getMath() { return math; }
    public double getEnglish() { return english; }

    // Setter方法（修改成绩）
    public void setChinese(double chinese) { this.chinese = chinese; }
    public void setMath(double math) { this.math = math; }
    public void setEnglish(double english) { this.english = english; }

    // 打印学生信息
    @Override
    public String toString() {
        return "学生{" +
                "ID='" + id + '\'' +
                ", 姓名='" + name + '\'' +
                ", 语文=" + chinese +
                ", 数学=" + math +
                ", 英语=" + english +
                ", 总分=" + getTotalScore() +
                '}';
    }
}

// 成绩管理类（核心业务逻辑）
class ScoreManager {
    // 存储学生列表
    private final List<Student> studentList;

    public ScoreManager() {
        studentList = new ArrayList<>();
    }

    // 1. 添加学生
    public void addStudent(Student student) {
        // 判断ID是否重复
        for (Student s : studentList) {
            if (s.getId().equals(student.getId())) {
                System.out.println("添加失败：学生ID已存在！");
                return;
            }
        }
        studentList.add(student);
        System.out.println("学生信息添加成功！");
    }

    // 2. 根据ID删除学生
    public void deleteStudentById(String id) {
        for (Student s : studentList) {
            if (s.getId().equals(id)) {
                studentList.remove(s);
                System.out.println("学生信息删除成功！");
                return;
            }
        }
        System.out.println("删除失败：未找到该ID的学生！");
    }

    // 3. 修改学生成绩
    public void updateScore(String id, double chinese, double math, double english) {
        for (Student s : studentList) {
            if (s.getId().equals(id)) {
                s.setChinese(chinese);
                s.setMath(math);
                s.setEnglish(english);
                System.out.println("成绩修改成功！");
                return;
            }
        }
        System.out.println("修改失败：未找到该ID的学生！");
    }

    // 4. 展示所有学生
    public void showAllStudents() {
        if (studentList.isEmpty()) {
            System.out.println("暂无学生信息！");
            return;
        }
        System.out.println("-------- 所有学生信息 --------");
        for (Student s : studentList) {
            System.out.println(s);
        }
    }

    // 5. 展示总分最高的学生
    public void showHighestScoreStudent() {
        if (studentList.isEmpty()) {
            System.out.println("暂无学生信息！");
            return;
        }
        Student topStudent = studentList.get(0);
        for (Student s : studentList) {
            if (s.getTotalScore() > topStudent.getTotalScore()) {
                topStudent = s;
            }
        }
        System.out.println("总分最高的学生：" + topStudent);
    }

    // 6. 计算班级各科平均分
    public void showClassAverageScore() {
        if (studentList.isEmpty()) {
            System.out.println("暂无学生信息！");
            return;
        }
        double totalChinese = 0, totalMath = 0, totalEnglish = 0;
        for (Student s : studentList) {
            totalChinese += s.getChinese();
            totalMath += s.getMath();
            totalEnglish += s.getEnglish();
        }
        int count = studentList.size();
        System.out.printf("班级平均分：语文=%.2f，数学=%.2f，英语=%.2f%n",
                totalChinese / count, totalMath / count, totalEnglish / count);
    }
}
