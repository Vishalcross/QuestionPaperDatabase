package oops;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
public class Database {//databse handler
    Connection connection = null;//database connection
    Statement s = null;//databse Statement
    private ArrayList<String> quiz = new ArrayList();//holds the generated quiz questions
    private ArrayList<String> answers = new ArrayList();//holds the generated quiz's answers
    private ArrayList<String> listQ = new ArrayList();//holds the questions of subject
    private ArrayList<String> listA = new ArrayList();//holds answers of a subject

    String line;
    //private boolean loggedIn = false;
    public Database(){//opens connection
            try {
                Class.forName("org.sqlite.JDBC");
                Connection c = DriverManager.getConnection("jdbc:sqlite:data.db");//ata.db is name of database
                s = c.createStatement();
                System.out.println("Database opened successfully");
            } catch (ClassNotFoundException | SQLException ex) {
                JOptionPane.showMessageDialog(null,"Database Connection Failed");
                System.err.println(ex);
            }
    }


    public void createSubject(String subject){//creates subject

        try {
            String query = "CREATE TABLE "+subject+" (TYPE TEXT, QUESTION TEXT, A TEXT, B TEXT, C TEXT, D TEXT, ANSWER TEXT);";
            s.executeUpdate(query);
            System.out.println("Successfully created a subject "+subject);
            s.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Subject Already Exists");
            System.out.println(ex);
        }
    }


    public void deleteSubject(String subject){//deletes subject
        try {
            String query = "drop table "+subject+";";
            s.executeUpdate(query);
            System.out.println("Deleted Subject!");
        } catch (SQLException ex) {
            System.out.println("Subject not found");
            JOptionPane.showMessageDialog(null,"Subject not found");
        }
    }


    public void addMCQ(String subject, String question, String a, String b, String c, String d, String answer){//adds an MCQ
        try {
            String query = "INSERT INTO "+subject+" (TYPE,QUESTION,A,B,C,D,ANSWER) VALUES ('MCQ','"+question+"','"+a+"','"+b+"','"+c+"','"+d+"','"+answer+"' );";
            s.executeUpdate(query);
            System.out.println("Added "+question+" to "+subject);
            s.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    public void addFill(String subject, String question, String answer){//adds a fill in the blanks question
        try {
            String query = "INSERT INTO " + subject + " (TYPE,QUESTION,ANSWER) VALUES ('FILL', '"+question+"' , '"+answer+"' );";
            s.executeUpdate(query);
            s.close();
            System.out.println("Added"+question+" to "+subject);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }


    public void addTF(String subject, String question, String answer){// adds a true or false question
        try {
            String query = "INSERT INTO " + subject + " (TYPE,QUESTION,ANSWER) VALUES ('TF', '"+question+"' , '"+answer+"' );";
            s.executeUpdate(query);
            s.close();
            System.out.println("Added"+question+" to "+subject);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void deleteQuestion(String subject, String question){//deletes question
        try{
            String query = "DELETE FROM "+subject+" WHERE QUESTION = '"+question+"';";
            System.out.println("Deleted the question successfully");
            s.executeUpdate(query);
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Question not selected");
            System.out.println("Subject/Question number incorrect!");
        }

    }

    public String findType(String subject, String question){//finds the question type and returns it
        String ans="";
        try{
            String query = "SELECT TYPE FROM "+subject+" WHERE QUESTION = '"+question+"';";
            ResultSet r = s.executeQuery(query);
            ans = r.getString("TYPE");
            System.out.println(ans);
        } catch(SQLException e){
            System.out.println(e);
        }
        return ans;
    }

    public void changeMCQ(String subject, String question, String a, String b, String c, String d, String answer, String n){//changes an MCQ
        try {
            String query = "UPDATE "+subject+" SET QUESTION = '"+question+"',A = '"+a+"',B = '"+b+"',C = '"+c+"',D = '"+d+"',ANSWER = '"+answer+"' WHERE QUESTION ='"+n+"';";
            s.executeUpdate(query);
            System.out.println("Modified "+question+" to "+subject);
            s.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Question not selected");
            System.out.println(ex);
        }
    }


    public void changeFill(String subject, String question, String answer, String n){//changes a Fillup
        try {

            String query = "UPDATE "+subject+" SET QUESTION = '"+question+"',ANSWER = '"+answer+"' WHERE QUESTION ='"+n+"';";

            s.executeUpdate(query);
            System.out.println("Modified "+question+" to "+subject+" ");
            s.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Question not selected");
            System.out.println(ex);
        }
    }

    public void changeTF(String subject, String question, String answer, String n){//changes a True or False
        try {
            String query = "UPDATE "+subject+" SET QUESTION = '"+question+"',ANSWER = '"+answer+"' WHERE QUESTION ='"+n+"';";
            s.executeUpdate(query);
            System.out.println("Modified "+question+" to "+subject);
            s.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Question not selected");
            System.out.println(ex);
        }
    }

    public int numberOfQ(String subject){//finds and returns the number of questions in a subject
        int count=0;
        try {
            ResultSet r = s.executeQuery("SELECT COUNT(*) AS total FROM "+subject);
            count = r.getInt("total");
        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "Subject not found to present number of questions");
            System.out.println(ex);
        }
        return count;
    }

    public void generateQuiz(int number, String subject){//generates the quiz and stores the question and answers in separate .txt files
        quiz.clear();
        answers.clear();
        try {
            int count;
            ResultSet r = s.executeQuery("SELECT COUNT(*) AS total FROM "+subject);
            count = r.getInt("total");
                r = s.executeQuery("SELECT * FROM "+subject+";");
                while(r.next()){
                    String q = r.getString("TYPE");
                    if(q.compareTo("MCQ") == 0){
                        q = r.getString("QUESTION") + "|" + "a. " + r.getString("A")+  " b. " + r.getString("B")+ " c. " +r.getString("C")+ " d. " +r.getString("D");
                        quiz.add(q);
                        q = r.getString("ANSWER");
                        answers.add(q);
                    }
                    else if(q.compareTo("TF") == 0 || q.compareTo("FILL") == 0){
                        q = r.getString("QUESTION");
                        quiz.add(q);
                        q = r.getString("ANSWER");
                        answers.add(q);
                    }
                }
                int n = quiz.size();
                for(int i=0;i<n - number;i++){
                    int j = (int)Math.ceil((quiz.size() - 1)* Math.random());
                    quiz.remove(j);
                    answers.remove(j);
               }
               try {
                            PrintWriter pw = new PrintWriter(new FileOutputStream("E:\\Java Projects\\OOPS\\src\\oops\\quiz.txt"));
                            PrintWriter pw1 = new PrintWriter(new FileOutputStream("E:\\Java Projects\\OOPS\\src\\oops\\answers.txt"));
                            for(int i=0;i<quiz.size();i++){
                                String q = (String)quiz.get(i);
                                StringTokenizer st = new StringTokenizer(q,"|");
                                pw.println("Q"+(i+1)+". "+st.nextToken());
                                while(st.hasMoreElements())
                                {

                                    pw.println(" "+st.nextToken());
                                }


                                q = (String) answers.get(i);
                                pw1.println((i+1) + " "+q);
                            }
                            pw.close();
                            pw1.close();
                        } catch (FileNotFoundException ex) {
                            System.out.println("File not found");
                        }

        } catch (SQLException ex) {
             JOptionPane.showMessageDialog(null, "No questions available or Subject Incorrect");
            System.out.println(ex);
        }
    }
    public void generateList(String subject){//generates the question and answers list of a subject
        listQ.clear();
        listA.clear();
        try {
            ResultSet r;

                r = s.executeQuery("SELECT * FROM "+subject+";");
                while(r.next()){
                    String q = r.getString("TYPE");
                    if(q.compareTo("MCQ") == 0){
                        q = r.getString("QUESTION");
                        listQ.add(q);
                        q = r.getString("ANSWER");
                        listA.add(q);
                    }
                    else if(q.compareTo("TF") == 0 || q.compareTo("FILL") == 0){
                        q = r.getString("QUESTION");
                        listQ.add(q);
                        q = r.getString("ANSWER");
                        listA.add(q);
                    }
                }


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No questions available or Subject Incorrect to display list of questions");
            System.out.println(ex);
        }
    }

    public ArrayList getTables(){//returns the subjects contained in the database
        ArrayList<String> table = new ArrayList();
        String query = "SELECT name AS t FROM sqlite_master";
        try {
            ResultSet r = s.executeQuery(query);
            while(r.next()){
                String x = r.getString("t");
                System.out.println(x);
                table.add(x);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No Subject Found");
            System.err.println(ex);
        }
        return table;
    }

    public ArrayList getListA(){//returns the list of Answers from generateList
        return listA;
    }
    public ArrayList getListQ(){//returns the list of Questions from generateList
        return listQ;
    }
    public ArrayList getQ(){//returns the list of questions from generateQuiz
        return quiz;
    }
    public ArrayList getA(){//returns the list of answer from generateQuiz
        return answers;
    }
}
