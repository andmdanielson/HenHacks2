import javax.print.DocFlavor;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Login {

    JFrame loginWindow = new JFrame("Login");
    JLabel loginLabel = new JLabel("Welcome! Please Login or Select 'New Account'");
    JFrame newAccountWindow = new JFrame("New Account");
    JLabel newAccountLabel = new JLabel("Create New Account");

    public Login(){
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        loginWindow.setLayout(new BorderLayout());
        loginWindow.getRootPane().setBorder(pad);
        loginWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container loginContainer = new Container();
        Container buttonContainer = new Container();
        loginContainer.setLayout(new GridLayout(2,2,0,10));
        buttonContainer.setLayout(new GridLayout(1,2,5,0));

        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        JButton login = new JButton("Login");
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkLogin(usernameField.getText(),new String(passwordField.getPassword()));
            }
        });
        JButton newAccount = new JButton("New Account");
        newAccount.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewAccount();
            }
        });

        loginContainer.add(usernameLabel);
        loginContainer.add(usernameField);
        loginContainer.add(passwordLabel);
        loginContainer.add(passwordField);

        buttonContainer.add(login);
        buttonContainer.add(newAccount);

        loginWindow.add(loginLabel,BorderLayout.NORTH);
        loginWindow.add(loginContainer,BorderLayout.CENTER);
        loginWindow.add(buttonContainer,BorderLayout.SOUTH);
        loginWindow.pack();
        loginWindow.setVisible(true);
    }

    public void checkLogin(String username, String password){
        try{
            FileReader reader = new FileReader("Accounts.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            ArrayList<String[]> accounts = new ArrayList<>();
            String line;
            String[] brokenLine;
            while((line = bufferedReader.readLine()) != null){
                brokenLine = line.split(":",3);
                accounts.add(brokenLine);
            }
            String accountFile = null;
            for(String[] person:accounts){
                if(person[0].equals(username)){
                    if(person[1].equals(password)){
                        accountFile = person[2];
                    }
                }
            }
            if(accountFile != null){
                loginWindow.dispose();
                openAccount(accountFile);
            }
            else{
                loginLabel.setText("Username or Password is Incorrect");
            }
        }
        catch(FileNotFoundException ex){
            loginLabel.setText("No Accounts in Database");
        }
        catch(IOException ex){
            loginLabel.setText("No Accounts in Database");
        }
    }

    public void openAccount(String accountFile){
        try {
            FileReader reader = new FileReader(accountFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            String[] brokenLine = line.split(":",5);
            if(brokenLine[4].equals("Teacher")){
                new TeacherMenu(accountFile);
            }
            else{
                new StudentMenu(accountFile);
            }
        }
        catch(FileNotFoundException ex){}
        catch(IOException ex){}
    }

    public void createNewAccount(){
        loginWindow.dispose();

        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        newAccountWindow.setLayout(new BorderLayout());
        newAccountWindow.getRootPane().setBorder(pad);
        newAccountWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        Container fields = new Container();
        Container buttons = new Container();

        fields.setLayout(new GridLayout(6,2,5,10));
        buttons.setLayout(new GridLayout(1,1));

        JLabel firstName=new JLabel("First Name:");
        JLabel lastName=new JLabel("Last Name:");
        JLabel username=new JLabel("Username:");
        JLabel password=new JLabel("Password:");
        JLabel rePass=new JLabel("Retype Password");
        JLabel select=new JLabel("Student or Teacher?");

        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        JPasswordField rePassField = new JPasswordField();
        rePassField.setEchoChar('*');
        JComboBox<String> type = new JComboBox<String>(new String[]{"Student","Teacher"});
        JButton create = new JButton("Create Account");

        create.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String password=new String(passwordField.getPassword());
                String verPass=new String(rePassField.getPassword());
                boolean works = verifyAccount(usernameField.getText(), password,verPass);
                String response = (String)type.getSelectedItem();
                boolean student=false;
                if (response.equals("Student")){
                    student=true;
                }

                if (works==true){
                    newAccountWindow.dispose();
                    addAccount(firstNameField.getText(),lastNameField.getText(),usernameField.getText(),password,student);
                }
            }
        });


        fields.add(firstName);
        fields.add(firstNameField);
        fields.add(lastName);
        fields.add(lastNameField);
        fields.add(username);
        fields.add(usernameField);
        fields.add(password);
        fields.add(passwordField);
        fields.add(rePass);
        fields.add(rePassField);
        fields.add(select);
        fields.add(type);
        buttons.add(create);

        newAccountWindow.add(newAccountLabel,BorderLayout.NORTH);
        newAccountWindow.add(fields,BorderLayout.CENTER);
        newAccountWindow.add(buttons,BorderLayout.SOUTH);
        newAccountWindow.pack();
        newAccountWindow.setVisible(true);


    }

    public boolean verifyAccount(String username, String password, String verPass){
        boolean works = false;
        try {
        FileReader reader = new FileReader("Accounts.txt");
        BufferedReader bufferedReader = new BufferedReader(reader);
        ArrayList<String[]> accounts = new ArrayList<>();
        String line;
        String[] brokenLine;
        while((line = bufferedReader.readLine()) != null){
            brokenLine = line.split(":",3);
            accounts.add(brokenLine);
        }
        for(String[] person:accounts) {
            if (person[0].equals(username)) {
                throw new RuntimeException("Invalid Username");
            }
        }
            if (password.equals(verPass)==false){
                throw new RuntimeException("Passwords Do Not Match");
            }
            works=true;
            }catch(FileNotFoundException ex){
            }catch (IOException ex){
        }catch (RuntimeException ex){
            newAccountLabel.setText(ex.getMessage());
        }
        return works;
    }

    public void addAccount(String first, String last, String username, String password,boolean student) {
        loginWindow.dispose();
        try {
            FileWriter writer = new FileWriter("Accounts.txt",true);
            if(writer == null){
                writer = new FileWriter("Accounts.txt");
            }
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            String accountFile = username +".txt";
            String writeString = username + ":" + password + ":" + accountFile;
            bufferedWriter.write(writeString);
            bufferedWriter.newLine();
            bufferedWriter.close();

            writer = new FileWriter(accountFile);
            bufferedWriter = new BufferedWriter(writer);
            String type;
            if(student){
                type = "Student";
            }
            else{
                type = "Teacher";
            }
            writeString = first + ":" + last + ":" + username + ":" + password + ":" + type;
            bufferedWriter.write(writeString);
            bufferedWriter.newLine();

            FileReader reader = new FileReader("ClassCodes.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            ArrayList<String> codes = new ArrayList<>();
            String line;
            while((line = bufferedReader.readLine()) != null){
                codes.add(line);
            }
            bufferedReader.close();

            if(student){
                bufferedWriter.close();
                JFrame teacherCodeWindow = new JFrame("Class Code");
                Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
                teacherCodeWindow.setLayout(new BorderLayout());
                teacherCodeWindow.getRootPane().setBorder(pad);
                teacherCodeWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                teacherCodeWindow.setLayout(new GridLayout(3,1,0,10));

                JLabel codeLabel = new JLabel("Enter your teacher's class code");
                JTextField codeField = new JTextField();
                codeField.setColumns(6);
                JButton codeButton = new JButton("Submit");


                codeButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for(String code: codes){
                            System.out.println(code);
                            System.out.println(codeField.getText());
                            if(code.equals(codeField.getText())){
                                teacherCodeWindow.dispose();
                                addToClass(code,accountFile);
                                try {
                                    FileWriter writer = new FileWriter(accountFile,true);
                                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                                    bufferedWriter.write(code);
                                    bufferedWriter.newLine();
                                    bufferedWriter.close();
                                }
                                catch(FileNotFoundException ex){}
                                catch(IOException ex){}
                            }
                            else{
                                codeLabel.setText("No such class exists");
                            }
                        }
                    }
                });

                teacherCodeWindow.add(codeLabel);
                teacherCodeWindow.add(codeField);
                teacherCodeWindow.add(codeButton);
                teacherCodeWindow.pack();
                teacherCodeWindow.setVisible(true);
            }
            else{
                Random random = new Random();
                boolean codeTest = true;
                Integer thisCode = 0;
                while(codeTest) {
                    codeTest = false;
                    thisCode = random.nextInt(899999) + 100000;
                    for (String code : codes) {
                        if (code.equals(thisCode.toString())) {
                            codeTest = true;
                            break;
                        }
                    }
                }
                bufferedWriter.write(thisCode.toString());
                bufferedWriter.newLine();
                bufferedWriter.close();
                writer = new FileWriter("ClassCodes.txt",true);
                bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(thisCode.toString());
                bufferedWriter.newLine();
                bufferedWriter.close();
                writer = new FileWriter(thisCode.toString() + ".txt");
                bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(accountFile);
                bufferedWriter.close();
            }
        } catch (IOException ex) {
        }
    }

    public void addToClass(String code,String accountFile){
        try {
            FileReader reader = new FileReader(code + ".txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer inputBuffer = new StringBuffer();
            String line;
            while((line = bufferedReader.readLine()) != null){
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }
            inputBuffer.append(accountFile);
            inputBuffer.append('\n');
            bufferedReader.close();

            FileWriter writer = new FileWriter(code+".txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(inputBuffer.toString());
            bufferedWriter.close();
        }
        catch(FileNotFoundException ex){}
        catch(IOException ex){}
    }
}
