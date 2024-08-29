/*  STUDENT COURSE REGISTRATION SYSTEM

Course Database: Store course information, including course code, title,description, capacity, and schedule.
Student Database: Store student information, including student ID, name, and registered courses.
Course Listing: Display available courses with details and available slots.
Student Registration: Allow students to register for courses from the available options.
Course Removal: Enable students to drop courses they have registered for.
*/

import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentCourseRegistrationSystem {

    // SQL Queries as constants
    private static final String INSERT_COURSE_SQL = "INSERT INTO course(code, title, description, capacity, schedule) VALUES (?, ?, ?, ?, ?)";
    private static final String INSERT_STUDENT_SQL = "INSERT INTO student(id, name) VALUES (?,?)";
    private static final String REGISTER_COURSE_SQL = "INSERT INTO student_course(student_id,course_code) VALUES (?,?)";
    private static final String SELECT_STUDENT_SQL = "SELECT * FROM student";
    private static final String SELECT_COURSE_SQL = "SELECT * FROM course";
    private static final String DELETE_STUDENT_COURSE_SQL = "DELETE FROM student_course where student_id = ? AND course_code = ?";
    private static final String SELECT_STUDENT_COURSE_SQL = "SELECT c.code,c.title,ci.description,c.capacity,c.schedule FROM course c JOIN student_course sc ON c.code = sc.course_code WHERE sc.student_id = ?";

    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in);
                Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/student_course",
                        "postgres", "2023")) {
            Class.forName("org.postgresql.Driver");

            boolean progRunning = true;
            while (progRunning) {
                menuDisplay();

                System.out.print("Enter your choice : ");
                int choice = s.nextInt();
                s.nextLine();

                switch (choice) {
                    case 1:
                        // for storing course info
                        storeCourseInfo(con, s);
                        break;

                    case 2:
                        // for storing student info
                        storeStudentInfo(con, s);
                        break;

                    case 3:
                        // for displaying available courses with remaining slots
                        displayAvailableCourses(con);
                        break;

                    case 4:
                        // for displaying all students info
                        displayAllStudents(con);
                        break;

                    case 5:
                        // for registering student for a course
                        registerCourse(con, s);
                        break;

                    case 6:
                        // for dropping a registered course by student
                        dropCourse(con, s);
                        break;

                    case 7:
                        // to exit the program
                        progRunning = false;
                        System.out.println("Exiting the system.");
                        break;
                    default:
                        System.out.println("Invalid option Entered. Please Enter a number between 1-7");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            // e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Postgres Driver not found.");
            // e.printStackTrace();
        } catch (InputMismatchException e) {
            System.out.println("Enter a valid number input(1-7).");
        }
    }

    // for displaying the options menu
    private static void menuDisplay() {
        System.out.println("1. Add new course information(admin only)");
        System.out.println("2. Add student information");
        System.out.println("3. Display available course with available slots");
        System.out.println("4. Display all students Information");
        System.out.println("5. Register student for a course");
        System.out.println("6. Drop a courses");
        System.out.println("7. Exit");
    }

    // to store new course info
    private static void storeCourseInfo(Connection con, Scanner s) {
        try (PreparedStatement pstmt = con.prepareStatement(INSERT_COURSE_SQL)) {

            System.out.println("\tCourse Information ->");

            System.out.print("\nEnter new course code : ");
            int code = s.nextInt();
            s.nextLine();

            System.out.print("\nEnter new course name : ");
            String courseName = s.nextLine();

            System.out.print("\nEnter course description : ");
            String description = s.nextLine();

            System.out.print("\nEnter course capacity : ");
            int capacity = s.nextInt();
            s.nextLine();

            System.out.print("\nEnter course schedule (YYYY-MM-DD): ");
            String schedule = s.nextLine();

            pstmt.setInt(1, code);
            pstmt.setString(2, courseName);
            pstmt.setString(3, description);
            pstmt.setInt(4, capacity);
            pstmt.setDate(5, java.sql.Date.valueOf(schedule));

            int insertedRows = pstmt.executeUpdate();
            if (insertedRows > 0) {
                System.out.println("Course information stored successfully.\n");

            } else {
                System.out.println("Failed to store course Information\n");
            }

        } catch (SQLException e) {
            System.out.println("Error storing course information.Please check inputs and try again.\n");
            // e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input format : " + e.getMessage());
        }
    }

    // to store student info
    private static void storeStudentInfo(Connection con, Scanner s) {
        try (PreparedStatement pstmt = con.prepareStatement(INSERT_STUDENT_SQL);
                /*Statement stmt = con.createStatement()*/) {

                System.out.println("\tStudent Information ->");
                System.out.print("Enter your student id : ");
                int id = s.nextInt();
                s.nextLine();

                System.out.print("Enter your name : ");
                String name = s.nextLine();

                pstmt.setInt(1, id);
                pstmt.setString(2, name);


            // ResultSet rs = stmt.executeQuery(SELECT_STUDENT_SQL);
            int insertedRows = pstmt.executeUpdate();
            if (insertedRows > 0) {
                System.out.println("Student information stored successfully.\n");
            } else {
                System.out.println("Failed to store Student Information\n");
            }

            // System.out.println("student -> ");

            // while (rs.next()) {
            //     int id2 = rs.getInt("id");
            //     String name2 = rs.getString("name");
            //     System.out.println("ID : " + id2 + "\nname : " + name2 + "\n");
            // }

        } catch (SQLException e) {
            System.out.println("Error storing student information : "+e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input format : "+e.getMessage());
        }
    }

    // to display info of all students
    private static void displayAllStudents(Connection con) {
        try (Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(SELECT_STUDENT_SQL)) {

            System.out.println("\tAll Students:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                System.out.println("ID: " + id + ", Name: " + name + "\n");
            }

        } catch (SQLException e) {
            System.out.println("Error displaying students : "+e.getMessage());
            // e.printStackTrace();
        }
    }

    // for displaying all available courses
    private static void displayAvailableCourses(Connection con) {
        try (Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_COURSE_SQL)) {

            System.out.println("\tCourses:");
            while (rs.next()) {
                int code = rs.getInt("code");
                String title = rs.getString("title");
                String description = rs.getString("description");
                int capacity = rs.getInt("capacity");
                String schedule = rs.getString("schedule");

                System.out.println("Code: " + code + ",\nTitle: " + title + ",\nDescription: " + description
                        + ",\nCapacity: " + capacity + ",\nSchedule: " + schedule + ".\n");
            }

        } catch (SQLException e) {
            System.out.println("Error displaying available courses : "+e.getMessage());
            // e.printStackTrace();
        }
    }

// for registering a student for a course
    private static void registerCourse(Connection con, Scanner s) {
        try (PreparedStatement pstmt = con.prepareStatement(REGISTER_COURSE_SQL)) {
            System.out.println("\tStudent Course Registration ->");
            System.out.println("Enter your student ID : ");
            int studentID = s.nextInt();
            s.nextLine();

            System.out.println("Enter the course code to register : ");
            int courseCode = s.nextInt();
            s.nextLine();

            pstmt.setInt(1, studentID);
            pstmt.setInt(2, courseCode);

            int insertedRows = pstmt.executeUpdate();
            if (insertedRows > 0) {
                System.out.println("Student registered for course successfully.\n");
            } else {
                System.out.println("Failed to register for course");
            }

        } catch (SQLException e) {
            System.out.println("Error registering for the course : "+e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input(student ID OR course code) entered."+e.getMessage());
        }
    }

    // for dropping the course
    private static void dropCourse(Connection con, Scanner s) {
        try (PreparedStatement selectStmt = con.prepareStatement(SELECT_STUDENT_COURSE_SQL);
            PreparedStatement deleteStmt = con.prepareStatement(DELETE_STUDENT_COURSE_SQL)) {

            System.out.print("Enter your student ID: ");
            int studentID = s.nextInt();
            s.nextLine();
            
            System.out.print("Enter the course code to drop: ");
            int courseCode = s.nextInt();
            s.nextLine();

            // Check if the student is registered for the course
            selectStmt.setInt(1, studentID);
            selectStmt.setInt(2, courseCode);

            ResultSet rs = selectStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No registration found for this course with the provided student ID.");
                return;
            }

            // Display course details
            System.out.println("Course Code: " + rs.getInt("code"));
            System.out.println("Course Title: " + rs.getString("title"));
            System.out.println("Course Description: " + rs.getString("description"));
            System.out.println("Course Capacity: " + rs.getInt("capacity"));
            System.out.println("Course Schedule: " + rs.getDate("schedule"));

            System.out.print("Are you sure you want to drop this course? (yes/no): ");
            String confirm = s.nextLine();

            if (confirm.equalsIgnoreCase("yes")) {
                // Drop course
                deleteStmt.setInt(1, studentID);
                deleteStmt.setInt(2, courseCode);

                int rowsAffected = deleteStmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Course successfully dropped.");
                } else {
                    System.out.println("Failed to drop the course. Please try again.");
                }
            } else {
                System.out.println("Course drop canceled.");
            }

        } catch (SQLException e) {
            System.out.println("Error dropping course. Please check the inputs and try again.");
            e.printStackTrace();
        }
    }
}