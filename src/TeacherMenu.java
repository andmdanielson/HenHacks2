import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class TeacherMenu {

    JLabel codeLabel;
    JFrame menuWindow = new JFrame("Menu");
    public TeacherMenu(String accountFile){
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        menuWindow.setLayout(new BorderLayout());
        menuWindow.getRootPane().setBorder(pad);
        menuWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuWindow.setLayout(new BorderLayout());

        Container buttons = new Container();
        buttons.setLayout(new GridLayout(2,2,5,10));

        JButton viewStudents = new JButton("View Students");
        JButton addAssignment = new JButton("Add Assignment");
        JButton editAssignment = new JButton("Edit Assignment");
        JButton logout = new JButton("Logout");

        viewStudents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuWindow.dispose();
                viewStudents(accountFile);
            }
        });
        addAssignment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuWindow.dispose();
                addAssignment(accountFile);
            }
        });
        editAssignment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuWindow.dispose();
                editAssignment(accountFile);
            }
        });
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuWindow.dispose();
                System.exit(0);
            }
        });

        buttons.add(addAssignment);
        buttons.add(viewStudents);
        buttons.add(editAssignment);
        buttons.add(logout);

        try{
            FileReader reader = new FileReader(accountFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.readLine();
            String code = "Your class code is: " + bufferedReader.readLine();
            codeLabel = new JLabel(code);
        }
        catch(FileNotFoundException ex){}
        catch(IOException ex){}

        menuWindow.add(codeLabel,BorderLayout.NORTH);
        menuWindow.add(buttons,BorderLayout.CENTER);
        menuWindow.pack();
        menuWindow.setVisible(true);
    }

    public void viewStudents(String accountFile){
        new ClassList(accountFile);
    }

    public void addAssignment(String usernameFile){
        String fileNumber="";
        try{
            FileReader reader = new FileReader(usernameFile);
            BufferedReader buffer = new BufferedReader(reader);
            buffer.readLine();
            String number = buffer.readLine();
            buffer.close();
            fileNumber = number+".txt";


        } catch(FileNotFoundException ex){
        } catch(IOException ex){}


        new CreateAssignment(fileNumber);
    }

    public void editAssignment(String accountFile){

    }
}
