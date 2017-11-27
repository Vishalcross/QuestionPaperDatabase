/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oops;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
/**
 *
 * @author Vishal
 */
public class GUI {
     String u="",p="";
     boolean loggedIn = false;//store user state
     Auth auth;//reference to Auth class for invoking Database methods
    String sub;//stores current subject
     int count=0;
     int n=0;
     String selected;//stores selected question

    public GUI(){
        Font labelText = new Font("Noto Sans",0,18);//Font to be used later

        JFrame mainframe = new JFrame("Application");//mainframe holds all components
        mainframe.setVisible(true);
        mainframe.setSize(800,600);
        mainframe.setLayout(null);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        mainframe.setLocation(dim.width/2-mainframe.getSize().width/2, dim.height/2-mainframe.getSize().height/2);
        mainframe.setDefaultCloseOperation(EXIT_ON_CLOSE);//mainframe ends

        panel mainpanel = new panel();//panel to hold login page
        mainframe.add(mainpanel);
        mainpanel.setBounds(0,0,800,600);

        label welcome1 = new label("Welcome to the Quiz Generator");//welcome label
        mainpanel.add(welcome1);
        welcome1.setBounds(200,10,390,30);

        label welcome = new label("Welcome to the Quiz Generator");//welcome label
        mainpanel.add(welcome);
        welcome.setBounds(200,10,390,30);

        label signin = new label("Login to continue");//login header
        signin.setBounds(270,180,350,30);
        mainpanel.add(signin);

        JTextField username = new JTextField("Type Username",6);//username field
        mainpanel.add(username);
        username.setFont(labelText);
        username.setBounds(260,240,230,30);//username


        JPasswordField password = new JPasswordField("Type Password",6);
        mainpanel.add(password);//password field
        password.setFont(labelText);
        password.setBounds(260,285,230,30);

        //mainmenu panel
        panel mainmenu = new panel();
        mainframe.add(mainmenu);
        mainmenu.setBounds(0,0,800,600);
        mainmenu.setBackground(Color.decode("#1c1b1a"));
        mainmenu.add(welcome);
        mainmenu.setVisible(false);


        //main menu controls
        button changeUser = new button("Change Username");//changes Username
        button changePass = new button("Change Password");//changes password
        button generate = new button("Generate Quiz");//go to generate quiz page
        button edit = new button("Edit Database");//go to edit subject page

        mainmenu.add(changeUser);
        mainmenu.add(changePass);
        mainmenu.add(generate);
        mainmenu.add(edit);

        button login = new button("Login");//login button
        mainpanel.add(login);
        login.setBounds(330,340,100,40);

        //incorrect credential warning
        label warning = new label("Incorrect Credentials");
        warning.setVisible(false);
        warning.setBounds(250,400,350,30);
        mainpanel.add(warning);
        //login actions
        login.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String u = username.getText();
                String p = password.getText();
                auth = new Auth(u,p);
                loggedIn = auth.status();
                if(!loggedIn){
                    warning.setText("Incorrect Credentials");
                    warning.setVisible(true);
                }
                else{
                    mainpanel.setVisible(false);
                    mainmenu.setVisible(true);
                }

            }

        });

        generate.setBounds(150,200,150,40);
        edit.setBounds(450,200,150,40);
        changePass.setBounds(450,400,220,30);
        changeUser.setBounds(100,400,220,30);


        //actionListeners for password change
        changePass.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String newPass = JOptionPane.showInputDialog("Enter the new UserName");
                auth.setPass(newPass);


            }
        });

        //actionListeners for password change
        changeUser.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String newUsername = JOptionPane.showInputDialog("Enter the new UserName");
                auth.setUser(newUsername);


            }
        });

        //edit subject menu
        panel editMenu = new panel();
        editMenu.setVisible(false);
        editMenu.setBackground(Color.decode("#1c1b1a"));
        editMenu.setBounds(0,0,800,600);
        JComboBox subjectList = new JComboBox();

        //edit ActionListener
        edit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                mainmenu.setVisible(false);
                editMenu.setVisible(true);

                editMenu.add(subjectList);
                subjectList.setBounds(300,250,200,40);
                subjectList.setFont(labelText);
                subjectList.setVisible(true);
                mainframe.add(editMenu);
                auth.getTable();
                ArrayList<String> temp = auth.table;
                subjectList.removeAllItems();
                for(int i=0;i<temp.size();i++){
                    subjectList.addItem(temp.get(i));
                }
            }
        });

        //editMenu controls
        button back = new button("< Back");
        button next = new button("Next >");
        button addS = new button("Add Subject");
        button delS = new button("Delete Subject");
        editMenu.add(addS);
        editMenu.add(delS);
        editMenu.add(back);
        editMenu.add(next);
        back.setBounds(100,450,100,40);
        next.setBounds(600,450,100,40);
        addS.setBounds(100,100,200,40);
        delS.setBounds(500,100,200,40);

        //back to mainmenu
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                mainmenu.setVisible(true);
                editMenu.setVisible(false);
            }
        });

        //calls add subject of auth and adds the subject input given
        addS.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String newS = JOptionPane.showInputDialog("Enter name of new Subject");
                auth.newSub(newS);
                auth.getTable();
                ArrayList<String> temp = auth.table;
                subjectList.removeAllItems();
                for(int i=0;i<temp.size();i++){
                    subjectList.addItem(temp.get(i));
                }
            }
        });

        //calls delete subject of auth and deletes the subject input given
        delS.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String newS = JOptionPane.showInputDialog("Enter name of Subject");
                auth.delSub(newS);
                auth.getTable();
                ArrayList<String> temp = auth.table;
                subjectList.removeAllItems();
                for(int i=0;i<temp.size();i++){
                    subjectList.addItem(temp.get(i));
                }
            }
        });

        //addScreen is the subject question editing page
        panel addScreen = new panel();
        addScreen.setVisible(false);
        mainframe.add(addScreen);
        addScreen.setBounds(0,0,800,600);
        addScreen.setBackground(Color.decode("#1c1b1a"));

        //displays name of subject on top
        label subjectDisplay = new label("");
        addScreen.add(subjectDisplay);
        subjectDisplay.setBounds(350,10,220,30);

        //questions and answers list display
        label qlist = new label("Questions List (Select any to modify or delete)");
        addScreen.add(qlist);
        qlist.setBounds(100,270,600,30);
        label alist = new label("Answers list");
        addScreen.add(alist);
        alist.setBounds(280,360,350,30);

        //the controls of addScreen
        button modify = new button("Modify");//modify a selected question
        button addMCQ = new button("Add MCQ");//add a MCQ
        button addTF = new button("Add T/F");//add true/false
        button addF = new button("Add Fillups");//add FillUp
        button deleteQ = new button("Delete");//delete selected qeustion
        button backN = new button("< Back");//back to subject ist page
        addScreen.add(deleteQ);
        addScreen.add(addMCQ);
        addScreen.add(addTF);
        addScreen.add(addF);
        addScreen.add(modify);
        addScreen.add(backN);

        JComboBox setQ = new JComboBox();//holds the questions serially
        JComboBox setA = new JComboBox();//holds the answers serially
        addScreen.add(setQ);
        addScreen.add(setA);

        modify.setBounds(100,100,150,25);
        addMCQ.setBounds(300,100,150,25);
        addTF.setBounds(300,130,150,25);
        addF.setBounds(300,160,150,25);
        deleteQ.setBounds(500,100,100,25);
        setA.setBounds(250,400,300,30);
        setQ.setBounds(250,320,300,30);
        setA.setFont(labelText);
        setQ.setFont(labelText);
        backN.setBounds(100,450,100,40);

        //takes you to add/delete/modify screen from subject editing page
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                editMenu.setVisible(false);
                addScreen.setVisible(true);
                sub = subjectList.getSelectedItem().toString();
                subjectDisplay.setText(sub);
                auth.generateList(sub);
                setQ.removeAllItems();
                setA.removeAllItems();
                for(int i=0;i<auth.temp.size();i++){
                    setQ.addItem(auth.temp.get(i));
                    setA.addItem(auth.a.get(i));
                }
            }
        });

        //back to subjectList page
        backN.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                addScreen.setVisible(false);
                editMenu.setVisible(true);
            }

        });

        //pane to modify/add a question
        panel modifying = new panel();
        mainframe.add(modifying);
        modifying.setBounds(0,0,800,600);
        modifying.setVisible(false);
        button nextM = new button("Modify");
        modifying.add(nextM);
        nextM.setBounds(600,450,100,40);
        button backM = new button("< Back");
        modifying.add(backM);
        backM.setBounds(100,450,100,40);

        JTextField newQ = new JTextField("",6);//holds question
        modifying.add(newQ);
        newQ.setBounds(200,200,400,25);
        newQ.setFont(labelText);

        JTextField newA = new JTextField("Type answer here");//holds answer
        JTextField newa = new JTextField("Type option here");//holds options
        JTextField newb = new JTextField("Type option here");//holds options
        JTextField newc = new JTextField("Type option here");//holds options
        JTextField newd = new JTextField("Type option here");//holds options

        //qustion adders
        button mcqB = new button("Add MCQ");//mcq
        modifying.add(mcqB);
        mcqB.setBounds(600,450,100,40);
        mcqB.setVisible(false);

        button tfB = new button("Add True/False");//true false
        modifying.add(tfB);
        tfB.setBounds(520,450,170,40);
        tfB.setVisible(false);

        button fB = new button("Add FillUps");//fill in the blanks
        modifying.add(fB);
        fB.setBounds(550,450,150,40);
        fB.setVisible(false);


        //opens the modifying question pane
        modify.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                if(setQ.getItemCount() == 0) JOptionPane.showMessageDialog(null,"No Question Available");
                else{
                    selected = setQ.getSelectedItem().toString();
                     auth.findType(sub,selected);
                     String type = auth.type;
                     newQ.setText(selected);
                     if(type.compareTo("TF")==0 || type.compareTo("FILL") == 0){

                         modifying.add(newA);
                         newA.setText("Type Answer here");
                         newA.setBounds(200,240,400,25);
                         newA.setFont(labelText);
                     }
                     else{

                         modifying.add(newa);
                         newa.setText("Type Option here");
                         newa.setBounds(200,230,400,25);
                         newa.setFont(labelText);

                         modifying.add(newb);
                         newb.setText("Type Option here");
                         newb.setBounds(200,260,400,25);
                         newb.setFont(labelText);

                         modifying.add(newc);
                         newc.setText("Type Option here");
                         newc.setBounds(200,290,400,25);
                         newc.setFont(labelText);

                         modifying.add(newd);
                         newd.setText("Type Option here");
                         newd.setBounds(200,320,400,25);
                         newd.setFont(labelText);

                         modifying.add(newA);
                         newA.setText("Type answer here");
                         newA.setBounds(200,350,400,25);
                         newA.setFont(labelText);

                         newa.setVisible(true);
                     newb.setVisible(true);
                     newc.setVisible(true);
                     newd.setVisible(true);
                     }
                     addScreen.setVisible(false);
                     nextM.setVisible(true);
                    modifying.setVisible(true);
                }

            }
        });

        //back to addScreen pane
        backM.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                newa.setVisible(false);
                newb.setVisible(false);
                newc.setVisible(false);
                newd.setVisible(false);
                mcqB.setVisible(false);
                fB.setVisible(false);
                tfB.setVisible(false);
                addScreen.setVisible(true);
                modifying.setVisible(false);
            }

        });

        //modifies the question base on type

        nextM.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(auth.type.compareTo("TF")==0 || auth.type.compareTo("FILL") == 0){
                    auth.modifyQ(sub,newQ.getText(),newA.getText(),selected);
                }
                else{
                    auth.modifyMCQ(sub, newQ.getText(), newa.getText(), newb.getText(), newc.getText(), newd.getText(), newA.getText(), selected);
                }
                setQ.removeAllItems();
                setA.removeAllItems();
                for(int i=0;i<auth.temp.size();i++){
                    setQ.addItem(auth.temp.get(i));
                    setA.addItem(auth.a.get(i));
                }
                addScreen.add(setQ);
               addScreen.add(setA);
                newa.setVisible(false);
                newb.setVisible(false);
                newc.setVisible(false);
                newd.setVisible(false);
                addScreen.setVisible(true);
                modifying.setVisible(false);


            }

        });

        //add a question

        //adding MCQ
        addMCQ.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                    newa.setVisible(true);
                newb.setVisible(true);
                newc.setVisible(true);
                newd.setVisible(true);
                    newQ.setText("Type question here");

                    modifying.add(newa);
                    newa.setText("Type Option here");
                    newa.setBounds(200,230,400,25);
                    newa.setFont(labelText);

                    modifying.add(newb);
                    newb.setText("Type Option here");
                    newb.setBounds(200,260,400,25);
                    newb.setFont(labelText);

                    modifying.add(newc);
                    newc.setText("Type Option here");
                    newc.setBounds(200,290,400,25);
                    newc.setFont(labelText);

                    modifying.add(newd);
                    newd.setText("Type Option here");
                    newd.setBounds(200,320,400,25);
                    newd.setFont(labelText);

                    modifying.add(newA);
                    newA.setText("Type Answer here");
                    newA.setBounds(200,350,400,25);
                    newA.setFont(labelText);

                    nextM.setVisible(false);
                    mcqB.setVisible(true);
                addScreen.setVisible(false);
                modifying.setVisible(true);
            }

        });

        //creates the mcq based on data entered
        mcqB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                auth.addMCQ(sub, newQ.getText(), newa.getText(), newb.getText(), newc.getText(), newd.getText(), newA.getText());
                setQ.removeAllItems();
                setA.removeAllItems();
                for(int i=0;i<auth.temp.size();i++){
                    setQ.addItem(auth.temp.get(i));
                    setA.addItem(auth.a.get(i));
                }
                newa.setVisible(false);
                newb.setVisible(false);
                newc.setVisible(false);
                newd.setVisible(false);

                addScreen.add(setQ);
               addScreen.add(setA);
                mcqB.setVisible(false);
                addScreen.setVisible(true);
                modifying.setVisible(false);

            }

        });

        //add TF
        addTF.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                    newQ.setText("Type question here");
                    modifying.add(newA);
                    newA.setBounds(200,230,400,25);
                    newA.setText("Type Answer here");
                    newA.setFont(labelText);

                    nextM.setVisible(false);
                    tfB.setVisible(true);
                addScreen.setVisible(false);
                modifying.setVisible(true);
            }

        });
        //creates the T/F based on data entered
        tfB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                auth.addTF(sub, newQ.getText(), newA.getText());
                setQ.removeAllItems();
                setA.removeAllItems();
                for(int i=0;i<auth.temp.size();i++){
                    setQ.addItem(auth.temp.get(i));
                    setA.addItem(auth.a.get(i));
                }
                addScreen.add(setQ);
               addScreen.add(setA);
                tfB.setVisible(false);
                addScreen.setVisible(true);
                modifying.setVisible(false);

            }

        });

        //add Fillups
        addF.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                    newQ.setText("Type question here");
                    modifying.add(newA);
                    newA.setBounds(200,230,400,25);
                    newA.setText("Type Answer here");
                    newA.setFont(labelText);

                    nextM.setVisible(false);
                    fB.setVisible(true);
                addScreen.setVisible(false);
                modifying.setVisible(true);
            }

        });

        //creates fill in the blanks based on data entered
        fB.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {

                auth.addFill(sub, newQ.getText(), newA.getText());
                setQ.removeAllItems();
                setA.removeAllItems();
                for(int i=0;i<auth.temp.size();i++){
                    setQ.addItem(auth.temp.get(i));
                    setA.addItem(auth.a.get(i));
                }
                addScreen.add(setQ);
               addScreen.add(setA);
                fB.setVisible(false);
                addScreen.setVisible(true);
                modifying.setVisible(false);

            }

        });


        //delete question
        deleteQ.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                        if(setQ.getItemCount() == 0){
                            JOptionPane.showMessageDialog(null,"No Question Available");
                        }
                        else{
                            selected = setQ.getSelectedItem().toString();

                           auth.deleteQ(sub, selected);
                           setQ.removeAllItems();
                           setA.removeAllItems();
                           for(int i=0;i<auth.temp.size();i++){
                               setQ.addItem(auth.temp.get(i));
                               setA.addItem(auth.a.get(i));
                           }
                           addScreen.add(setQ);
                          addScreen.add(setA);
                        }

            }
        });


        //generate random Quiz
        panel gen = new panel();
        mainframe.add(gen);
        gen.setBounds(0,0,800,600);
        button go = new button("Go");//starts the quiz
        gen.add(go);
        go.setBounds(100,100,100,25);

        JTextField q = new JTextField("Number of questions",6);//data entry point of number of questions
        JComboBox s = new JComboBox();//dropdown to hold list of subjects

        gen.add(q);
        gen.add(s);
        q.setBounds(270,300,200,30);
        q.setFont(labelText);
        s.setBounds(270,250,200,30);
        s.setFont(labelText);
        gen.setVisible(false);
        button backMain = new button("Back");//takes back to main menu
        gen.add(backMain);
        backMain.setBounds(500,100,100,25);

        //takes to quiz generator page
        generate.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mainmenu.setVisible(false);
                gen.setVisible(true);
                auth.getTable();
                ArrayList<String> temp = auth.table;
                s.removeAllItems();
                for(int i=0;i<temp.size();i++){
                    s.addItem(temp.get(i));
                }
            }

        });

        //back to main menu
        backMain.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mainmenu.setVisible(true);
                gen.setVisible(false);
            }

        });

        //question page
        panel qPage = new panel();
        qPage.setBounds(0,0,800,600);
        qPage.setVisible(false);
        mainframe.add(qPage);

        //move back/forth between questions
        button b = new button("Back");
        qPage.add(b);
        b.setBounds(500,100,100,25);
        button f = new button("Next");
        f.setBounds(100,100,100,25);
        qPage.add(f);

        //displays the question
        JPanel questionHolder = new JPanel(new FlowLayout());
        qPage.add(questionHolder);
        questionHolder.setBackground(Color.decode("#1c1b1a"));
        questionHolder.setBounds(0,200,800,200);
        JLabel question = new JLabel();
        questionHolder.add(question);
        question.setFont(labelText);
        question.setForeground(Color.white);
        //go generates and starts the quiz
        go.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String subject = s.getSelectedItem().toString();
                int temp=0;
                try{
                    n = Integer.parseInt(q.getText());
                    temp = 1;
                }
                catch(NumberFormatException ex){
                    temp=0;
                    JOptionPane.showMessageDialog(null,"Incorrect Number of questions selected");
                }
                if(temp == 1){
                    if(n>auth.numberOfQ(subject)){
                       n = auth.numberOfQ(subject);
                    }

                   if(auth.numberOfQ(subject) == 0){
                       JOptionPane.showMessageDialog(null,"The subject doesn't have any questions");
                   }
                   else{
                       gen.setVisible(false);
                    qPage.setVisible(true);
                        auth.quiz(n,subject);
                        StringTokenizer st = new StringTokenizer(auth.temp.get(0),"|");
                        if(st.countTokens() > 1){
                            question.setText("<html> Q."+ st.nextToken() + "<br>" + st.nextToken() + " </html>");
                        } 
                        else question.setText(auth.temp.get(0));
                        count=1;
                   }
                }
            }

        });

        //last question or generate menu
        b.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(count>1){
                    count--;

                    StringTokenizer st = new StringTokenizer(auth.temp.get(count-1),"|");
                    if(st.countTokens() > 1) question.setText("<html>"+ st.nextToken() + " <br>" + st.nextToken() + " </html>");
                    else question.setText(auth.temp.get(count-1));
                }
                else{
                    count--;
                    gen.setVisible(true);
                    qPage.setVisible(false);
                }
            }

        });

        //for next question or main menu
        f.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Pressed");
                if(count==n){
                    System.out.println(count);
                    qPage.setVisible(false);
                    mainmenu.setVisible(true);
                    String[] items = new String[auth.a.size()+1];
                    items[0] = "Answers";
                    for(int i=1;i<items.length;i++){
                        items[i] = i +".  "+ auth.a.get(i-1);
                    }
                    JList aList = new JList(items);
                    aList.setFont(new Font("Noto Sans",0,18));
                    JPanel panel = new JPanel();
                    panel.add(aList);
                    JOptionPane.showMessageDialog(null,panel);
                    count=0;
                }
                else if(count<n){
                    System.out.println(count);
                    StringTokenizer st = new StringTokenizer(auth.temp.get(count),"|");
                    if(st.countTokens() > 1) question.setText("<html> "+ st.nextToken() + "<br>" + st.nextToken() + " </html>");
                    else question.setText(auth.temp.get(count));
                    count++;
                }
            }

        });











    }
}
//custom styled button
class button extends JButton{
    Font buttonText = new Font("Source Sans Pro",0,16);
    button(String name){
        super(name);
        super.setFont(buttonText);
        super.setBounds(330,320,100,30);
        super.setContentAreaFilled(false);
        super.setOpaque(true);
        super.setBackground(Color.decode("#423f28"));
        super.setForeground(Color.white);
    }
}
//custom styled panel
class panel extends JPanel{
    panel(){
        super(null);
        super.setBackground(Color.decode("#1c1b1a"));
    }
}
//custom styled labels
class label extends JLabel{
    label(String s){
        super(s);
        super.setForeground(Color.white);
        super.setFont(new Font("Montserrat",0,24));
    }
}
