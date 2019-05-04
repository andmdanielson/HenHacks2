import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class ClassList {

    JFrame studentList = new JFrame("Class List");
    JLabel listLabel = new JLabel("List of Students");

    public ClassList(String accountFile){
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        studentList.setLayout(new BorderLayout());
        studentList.getRootPane().setBorder(pad);
        studentList.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JList list;
        DefaultListModel<String> listStudent = new DefaultListModel<String>();

        try{
            FileReader reader = new FileReader(accountFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.readLine();
            String number = bufferedReader.readLine();
            bufferedReader.close();
            String c_file=number+".txt";

            FileReader reader_1 = new FileReader(c_file);
            BufferedReader bufferedReader_1= new BufferedReader(reader_1);
            bufferedReader_1.readLine();
            String username;
            while((username=bufferedReader_1.readLine())!=null){
                FileReader studentReader = new FileReader(username);
                BufferedReader buffRead = new BufferedReader(studentReader);
                String line = buffRead.readLine();
                String[] words = line.split(":",5);
                String name = words[0]+" "+words[1];
                buffRead.close();

                String addString = name + "                                                    " + (calculateScore(username)*100);
                listStudent.addElement(addString);
            }
            bufferedReader_1.close();

        } catch (FileNotFoundException ex){
        } catch (IOException ex){}

        list = new JList(listStudent);

        JScrollPane students = new JScrollPane(list);

        studentList.add(listLabel,BorderLayout.NORTH);
        studentList.add(students,BorderLayout.CENTER);
        studentList.pack();
        studentList.setVisible(true);
        studentList.setResizable(true);
    }

    public double calculateScore(String fileName){
        double total = 0,value = 0;
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);
            bufferedReader.readLine();
            bufferedReader.readLine();
            String line;
            while((line = bufferedReader.readLine()) != null){
                String[] brokenLine = line.split("@!");
                if(!brokenLine[brokenLine.length-1].equals("-")) {
                    String[] furtherBroken = brokenLine[brokenLine.length - 1].split("/");
                    value += Double.valueOf(furtherBroken[0]);
                    total += Double.valueOf(furtherBroken[1]);
                }
            }
        }
        catch(FileNotFoundException ex){}
        catch (IOException ex){}
        if(total == 0){
            return 0;
        }
        else {
            return value / total;
        }
    }
}
