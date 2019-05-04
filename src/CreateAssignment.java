import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class CreateAssignment extends Thread {
    JLabel createLabel = new JLabel("Enter Assignment Information");
    JFrame createWindow = new JFrame("Create Assignment");
    Assignment assign;
    boolean questionCheck = true;
    int num = 1;
    String fileName;

    public CreateAssignment(String fileName){
        this.fileName = fileName;
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        createWindow.setLayout(new BorderLayout());
        createWindow.getRootPane().setBorder(pad);
        createWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container fields = new Container();
        fields.setLayout(new GridLayout(2,2,5,10));
        Container finish = new Container();
        finish.setLayout(new GridLayout(1,1));

        JLabel assignmentName = new JLabel("Assignment Name");
        JLabel assignmentType = new JLabel("Assignment Type");
        JTextField enterAssignment = new JTextField();
        JComboBox<String> chooseType = new JComboBox<String>(new String[]{"Homework","Quiz","Test"});
        JButton end = new JButton("Create");

        fields.add(assignmentName);
        fields.add(enterAssignment);
        fields.add(assignmentType);
        fields.add(chooseType);
        finish.add(end);

        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addAssignment(enterAssignment.getText(),(String)chooseType.getSelectedItem());
            }
        });

        createWindow.add(createLabel,BorderLayout.NORTH);
        createWindow.add(fields,BorderLayout.CENTER);
        createWindow.add(finish,BorderLayout.SOUTH);
        createWindow.pack();
        createWindow.setVisible(true);
    }

    public void addAssignment(String name, String type){
        switch(type) {
            case "Homework":
                createWindow.dispose();
                assign = new Assignment(name, Assignment.AssignmentType.Homework);
                break;
            case "Quiz":
                createWindow.dispose();
                assign = new Assignment(name, Assignment.AssignmentType.Quiz);
                break;
            case "Test":
                createWindow.dispose();
                assign = new Assignment(name, Assignment.AssignmentType.Test);
                break;
        }
        addQuestions();
    }

    public void addQuestions(){
        JFrame questionInfoWindow = new JFrame("New Question");
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        questionInfoWindow.setLayout(new BorderLayout());
        questionInfoWindow.getRootPane().setBorder(pad);
        questionInfoWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        questionInfoWindow.setLayout(new BorderLayout());

        Container fieldContainer = new Container();
        Container buttons = new Container();
        fieldContainer.setLayout(new GridLayout(3,2));
        buttons.setLayout(new GridLayout(1,2,5,10));

        JLabel numberLabel = new JLabel("Question " + num);
        JTextField questionField = new JTextField();
        JTextField pointsField = new JTextField();
        JComboBox<String> questionType = new JComboBox<>(new String[]{"Multiple Choice", "Fill in the Blank","True or False","Short Response"});
        JLabel questionLabel = new JLabel("Question");
        JLabel typeLabel = new JLabel("Question Type");
        JLabel pointsLabel = new JLabel("Points");
        JButton add = new JButton("Add More");
        JButton addFinal = new JButton("Add Last");
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int point = Integer.parseInt(pointsField.getText());
                    questionInfoWindow.dispose();
                    String type = (String)questionType.getSelectedItem();
                    switch(type){
                        case "Multiple Choice":
                            multipleChoice(num,questionField.getText(),Question.QuestionType.MultipleChoice,point,true);
                            break;
                        case "True or False":
                            trueOrFalse(num,questionField.getText(),Question.QuestionType.TrueOrFalse,point,true);
                            break;
                    }
                    num++;
                }
                catch(NumberFormatException ex){
                    numberLabel.setText("Points must be a number");
                }
                catch(NullPointerException ex){
                    System.out.println("Null Pointer Exception");
                }
            }
        });
        addFinal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int point = Integer.parseInt(pointsField.getText());
                    questionInfoWindow.dispose();
                    String type = (String)questionType.getSelectedItem();
                    switch(type){
                        case "Multiple Choice":
                            multipleChoice(num,questionField.getText(),Question.QuestionType.MultipleChoice,point,false);
                            break;
                        case "True or False":
                            trueOrFalse(num,questionField.getText(),Question.QuestionType.MultipleChoice,point,false);
                            break;
                    }
                    num++;
                }
                catch(NumberFormatException ex){
                    numberLabel.setText("Points must be a number");
                }
                catch(NullPointerException ex){
                    System.out.println("Null Pointer Exception");
                }
            }
        });

        fieldContainer.add(questionLabel);
        fieldContainer.add(questionField);
        fieldContainer.add(typeLabel);
        fieldContainer.add(questionType);
        fieldContainer.add(pointsLabel);
        fieldContainer.add(pointsField);

        buttons.add(add);
        buttons.add(addFinal);

        questionInfoWindow.add(numberLabel,BorderLayout.NORTH);
        questionInfoWindow.add(fieldContainer,BorderLayout.CENTER);
        questionInfoWindow.add(buttons,BorderLayout.SOUTH);
        questionInfoWindow.pack();
        questionInfoWindow.setVisible(true);
    }

    public void multipleChoice(int num, String question, Question.QuestionType type, int points, boolean again){
        Question thisQuestion = new Question(num, question, type, points);

        JFrame window = new JFrame("Multiple Choice Question");
        JLabel title = new JLabel("Answer to Question");
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        window.setLayout(new BorderLayout());
        window.getRootPane().setBorder(pad);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container letters = new Container();
        letters.setLayout(new GridLayout(4,1,0,10));
        Container answers = new Container();
        answers.setLayout(new GridLayout(4,1,0,10));

        JTextField answerA = new JTextField();
        JTextField answerB = new JTextField();
        JTextField answerC = new JTextField();
        JTextField answerD = new JTextField();

        JRadioButton a = new JRadioButton("A");
        JRadioButton b = new JRadioButton("B");
        JRadioButton c = new JRadioButton("C");
        JRadioButton d = new JRadioButton("D");

        ButtonGroup group = new ButtonGroup();
        group.add(a);
        group.add(b);
        group.add(c);
        group.add(d);

        JButton submit = new JButton("Submit");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (a.isSelected()){
                    thisQuestion.addAnswer(answerA.getText(),true);
                    thisQuestion.addAnswer(answerB.getText(),false);
                    thisQuestion.addAnswer(answerC.getText(),false);
                    thisQuestion.addAnswer(answerD.getText(),false);

                } else if(b.isSelected()){
                    thisQuestion.addAnswer(answerA.getText(),false);
                    thisQuestion.addAnswer(answerB.getText(),true);
                    thisQuestion.addAnswer(answerC.getText(),false);
                    thisQuestion.addAnswer(answerD.getText(),false);

                } else if(c.isSelected()){
                    thisQuestion.addAnswer(answerA.getText(),false);
                    thisQuestion.addAnswer(answerB.getText(),false);
                    thisQuestion.addAnswer(answerC.getText(),true);
                    thisQuestion.addAnswer(answerD.getText(),false);

                } else{
                    thisQuestion.addAnswer(answerA.getText(),false);
                    thisQuestion.addAnswer(answerB.getText(),false);
                    thisQuestion.addAnswer(answerC.getText(),false);
                    thisQuestion.addAnswer(answerD.getText(),true);

                }
                window.dispose();
                assign.addQuestion(thisQuestion);
                if(again){
                    addQuestions();
                } else{
                    writeAssignment();
                }
            }
        });

        answers.add(answerA);
        answers.add(answerB);
        answers.add(answerC);
        answers.add(answerD);

        letters.add(a);
        letters.add(b);
        letters.add(c);
        letters.add(d);

        window.add(title,BorderLayout.NORTH);
        window.add(letters,BorderLayout.WEST);
        window.add(answers,BorderLayout.CENTER);
        window.add(submit, BorderLayout.SOUTH);

        window.pack();
        window.setVisible(true);
    }

    public void trueOrFalse(int num, String question, Question.QuestionType type, int points, boolean again) {
        Question thisQuestion = new Question(num, question, type, points);

        JFrame window = new JFrame("True or False Question");
        JLabel title = new JLabel("Answer to Question");
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        window.setLayout(new BorderLayout());
        window.getRootPane().setBorder(pad);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Container options = new Container();
        options.setLayout(new GridLayout(1,2,0,10));

        JRadioButton t = new JRadioButton("True");
        JRadioButton f = new JRadioButton("False");
        ButtonGroup group = new ButtonGroup();
        group.add(t);
        group.add(f);
        JButton submit = new JButton("Submit");
        options.add(t);
        options.add(f);

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (t.isSelected()){
                    thisQuestion.addAnswer("True", true);
                    thisQuestion.addAnswer("False", false);

                } else{
                    thisQuestion.addAnswer("True",false);
                    thisQuestion.addAnswer("False",true);

                }
                window.dispose();
                assign.addQuestion(thisQuestion);
                if(again){
                    addQuestions();
                } else{
                    System.out.println("Made it");
                    writeAssignment();
                }
            }

        });

        window.add(title,BorderLayout.NORTH);
        window.add(options,BorderLayout.CENTER);
        window.add(submit,BorderLayout.SOUTH);
        window.pack();
        window.setVisible(true);
    }

    public void writeAssignment(){
        assign.setTotalPoints();
        try {
            FileReader reader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String file;
            while((file = bufferedReader.readLine()) != null) {
                FileReader newReader = new FileReader(file);
                BufferedReader newBufferedReader = new BufferedReader(newReader);
                StringBuffer stringBuffer = new StringBuffer();

                String line;
                while ((line = newBufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                    stringBuffer.append('\n');
                }
                stringBuffer.append(assign.toString());
                stringBuffer.append("\n");
                newBufferedReader.close();

                FileWriter newWriter = new FileWriter(file);
                BufferedWriter newBufferedWriter = new BufferedWriter(newWriter);
                newBufferedWriter.write(stringBuffer.toString());
                newBufferedWriter.close();
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex){}
        catch(IOException ex){}
    }
}
