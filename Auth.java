/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oops;
import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author Vishal
 */
public class Auth {//the class that handles the GUI requests
    private boolean isLogged=false;//state of user authentication
    private String username;//holds username temporarily
    private String password;//holds password temporarily
     Database o;// reference to the databse object
    ArrayList<String> temp;//holds questions for various uses
    ArrayList<String> a;//holds answers for various uses
    ArrayList<String> table;//holds the list of subjects
    String type;//type of Question
    public Auth(String user, String pass){//constructor to initialise Database and check authentication
        FileInputStream f = null;
        try {
            f = new FileInputStream("E:\\Java Projects\\OOPS\\src\\oops\\auth.txt");
            DataInputStream d = new DataInputStream(f);
            BufferedReader bf = new BufferedReader(new InputStreamReader(d));
            try {
                 username = bf.readLine();
                 password = bf.readLine();
                isLogged = username.compareTo(user) == 0 && password.compareTo(pass) == 0;
            if(isLogged){
                        System.out.println("Logged IN");
                        o = new Database();

                    }
                    else{
                        System.out.println("Unauthorised Access");
                    }
            } catch (IOException ex) {
                System.err.println("Line Not Found");
            }

        } catch (FileNotFoundException ex) {
            System.err.println("File Not Found");
        }


    }
    public boolean status(){//returns user status
        return isLogged;
    }
    public void setPass(String pass){//password setter
        if(isLogged){
            try {
                PrintWriter pw = new PrintWriter("E:\\Java Projects\\OOPS\\src\\oops\\auth.txt");
                pw.println(username);
                password = pass;
                pw.println(password);
                System.out.println("Changed Passoword Successully");
                pw.close();
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            }
        }
                else System.out.println("Not Possible");
    }
    public void setUser(String user){//username setter
        if(isLogged){
            try {
                PrintWriter pw = new PrintWriter("E:\\Java Projects\\OOPS\\src\\oops\\auth.txt");
                username = user;
                pw.println(username);
                pw.println(password);
                System.out.println("Changed Username Successully");
                pw.close();
            } catch (FileNotFoundException ex) {
                System.err.println(ex);
            }
        }
        else System.out.println("Not Possible");
    }

    public void getTable(){//fills the ArrayList containing the subjects
         table = o.getTables();
    }

    public void newSub(String subject){//calls the new Subject in Database
        o.createSubject(subject);
    }

    public void delSub(String subject){//calls the delete Subject in Database
        o.deleteSubject(subject);
    }

    public void generateList(String subject){//gets the questions of a subject and stores in temp and a
        System.out.println(subject);
        System.out.println(subject+" "+o.numberOfQ(subject));
        o.generateList(subject);
        temp = o.getListQ();
        a = o.getListA();
    }

    public int numberOfQ(String subject){//returns number of questions in a subject
        int numberQ = o.numberOfQ(subject);
        return numberQ;
    }

    public void quiz(int number, String subject){//generates a random subject based quiz via Database
        o.generateQuiz(number, subject);
        temp = o.getQ();
        a = o.getA();
    }

    public void findType(String subject, String question){//finds type of question
         type = o.findType(subject, question);
    }

    public void modifyQ(String subject, String question, String answer, String n){//calls modify Question of Database to modify TrueFalse or Fill in the Blanks
        if(type.compareTo("FILL") == 0){
            o.changeFill(subject, question, answer, n);
        }
        else if(type.compareTo("TF")==0){
            o.changeTF(subject, question, answer, n);
        }
        temp.clear();
        a.clear();
        generateList(subject);
    }
    public void modifyMCQ(String subject, String question, String as, String b, String c, String d, String answer, String n){//for modifying MCQs
        o.changeMCQ(subject, question, as, b, c, d, answer, n);
        temp.clear();
        a.clear();
        generateList(subject);
    }

    /*public void addQ(String subject, String question, String answer){
        if(type.compareTo("FILL") == 0){
            o.addFill(subject, question, answer);
        }
        else if(type.compareTo("TF")==0){
            o.addTF(subject, question, answer);
        }
        temp.clear();
        a.clear();
        generateList(subject);
    }
    */
    public void addMCQ(String subject, String question, String as, String b, String c, String d, String answer){//adds a MCQ
        o.addMCQ(subject, question, as, b, c, d, answer);
        temp.clear();
        a.clear();
        generateList(subject);
    }

    public void addFill(String subject, String question, String answer){//adds FillUps
        o.addFill(subject, question, answer);
        temp.clear();
        a.clear();
        generateList(subject);
    }
    public void addTF(String subject, String question, String answer){//adds TrueFalse
        o.addTF(subject, question, answer);
        temp.clear();
        a.clear();
        generateList(subject);
    }

    public void deleteQ(String subject, String question){//delete a question
        o.deleteQuestion(subject, question);
        temp.clear();
        a.clear();
        generateList(subject);
    }
}
