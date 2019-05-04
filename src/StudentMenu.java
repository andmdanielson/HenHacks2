import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class StudentMenu {

    int num = 1;
    int sum = 0;
    String usernameFile,word;
    Assignment assignment = null;

    public StudentMenu(String usernameFile){
        this.usernameFile = usernameFile;
        JFrame window = new JFrame("Take Assignment");
        JLabel title = new JLabel("Select an Assignment");

        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        window.setLayout(new BorderLayout());
        window.getRootPane().setBorder(pad);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JComboBox<String> assignments = new JComboBox<String>();
        try{
            FileReader reader = new FileReader(usernameFile);
            BufferedReader buffer = new BufferedReader(reader);
            buffer.readLine();
            buffer.readLine();
            String line;

            while((line = buffer.readLine())!=null){
                String[] phrases = line.split("@!",4);
                assignments.addItem(phrases[0]+" - "+phrases[1]+" Point "+phrases[2]);
            }
            buffer.close();


        }catch(FileNotFoundException ex){
        }catch(IOException ex){}

        JButton advance = new JButton("Begin Assignment");

        advance.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phrase = (String) assignments.getSelectedItem();
                String[] brokenLine = phrase.split(" ",2);
                word = brokenLine[0];
                try {
                    FileReader reader = new FileReader(usernameFile);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        String[] moreBrokenLine = line.split("@!");
                        if(moreBrokenLine[0].equals(word)){
                            assignment = loadAssignment(line);
                            break;
                        }
                    }
                    bufferedReader.close();
                }
                catch(FileNotFoundException ex){}
                catch(IOException ex){}
                window.dispose();
                startAssignment(assignment);
            }
        });

        window.add(title,BorderLayout.NORTH);
        window.add(assignments,BorderLayout.CENTER);
        window.add(advance,BorderLayout.SOUTH);
        window.pack();
        window.setVisible(true);
        window.setResizable(true);
    }

    public Assignment loadAssignment(String line){
        String[] brokenLine = line.split("@!");
        Assignment.AssignmentType type;
        if(brokenLine[2].equals("Quiz")){
            type = Assignment.AssignmentType.Quiz;
        }
        else if(brokenLine[2].equals("Test")){
            type = Assignment.AssignmentType.Test;
        }
        else{
            type = Assignment.AssignmentType.Homework;
        }
        Assignment assignment = new Assignment(brokenLine[0],type);
        assignment.setPoints(Integer.parseInt(brokenLine[1]));
        for(int i = 3; i < brokenLine.length-1;i++){
            assignment.addQuestion(loadQuestion(brokenLine[i]));
        }
        return assignment;
    }

    public Question loadQuestion(String line){
        String[] brokenLine = line.split("@&");
        Question.QuestionType type;
        if(brokenLine[2].equals("MultipleChoice")){
            type = Question.QuestionType.MultipleChoice;
        }
        else{
            type = Question.QuestionType.TrueOrFalse;
        }

        Question question = new Question(Integer.parseInt(brokenLine[0]),brokenLine[1],type,Integer.parseInt(brokenLine[3]));
        for(int i = 4; i < brokenLine.length;i+=2){
            question.answers.put(brokenLine[i],Boolean.parseBoolean(brokenLine[i+1]));
        }
        return question;
    }

    public void startAssignment(Assignment assignment){
        ArrayList<Question> questions = assignment.getQuestions();
        for(Question question:questions){
            if(question.getNum() == num){
                if(questions.size()!=1){
                    num++;
                    askQuestion(question,questions,true);
                }
                else{
                    askQuestion(question,questions,false);
                }
                break;
            }
        }
    }

    public void askQuestion(Question question, ArrayList<Question> questions,boolean more){
        JFrame qWindow = new JFrame ("Question "+ question.getNum());
        JLabel qLabel = new JLabel (question.question);
        JButton submit = new JButton ("Submit");
        Border pad = BorderFactory.createEmptyBorder(20,20,20,20);
        qWindow.setLayout(new BorderLayout());
        qWindow.getRootPane().setBorder(pad);
        qWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container choices = new Container();
        if (question.type==Question.QuestionType.TrueOrFalse){
            JRadioButton t = new JRadioButton("True");
            JRadioButton f = new JRadioButton("False");
            ButtonGroup options = new ButtonGroup();
            options.add(t);
            options.add(f);

            choices.setLayout(new GridLayout(1,2,10,0));
            choices.add(t);
            choices.add(f);
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean result;
                    if (t.isSelected()){
                        result=question.answers.get("True");
                    } else {
                        result = question.answers.get("False");
                    }
                    if(result){
                        sum+=question.points;
                    }
                    qWindow.dispose();

                    if(more) {
                        for (Question q : questions) {
                            if (q.getNum() == num) {
                                if (num == questions.size()) {
                                    askQuestion(q, questions, false);
                                } else {
                                    num++;
                                    askQuestion(q, questions, true);
                                }
                                break;
                            }
                        }
                    }
                    else{
                        setScore();
                    }
                }
            });
        } else{
            Set<String> answerList = question.answers.keySet();
            String[] answer=new String[4];
            int idx=0;
            for(String phrase:answerList){
                answer[idx]=phrase;
                idx++;
            }
            JRadioButton a = new JRadioButton(answer[0]);
            JRadioButton b = new JRadioButton(answer[1]);
            JRadioButton c = new JRadioButton(answer[2]);
            JRadioButton d = new JRadioButton(answer[3]);
            ButtonGroup options = new ButtonGroup();
            options.add(a);
            options.add(b);
            options.add(c);
            options.add(d);

            choices.setLayout(new GridLayout(4,1,0,10));
            choices.add(a);
            choices.add(b);
            choices.add(c);
            choices.add(d);
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean result;
                    if (a.isSelected()){
                        result = question.answers.get(answer[0]);
                    }else if(b.isSelected()){
                        result = question.answers.get(answer[1]);
                    }else if(b.isSelected()){
                        result = question.answers.get(answer[2]);
                    }else{
                        result = question.answers.get(answer[3]);
                    }
                    if(result){
                        sum+=question.points;
                    }
                    qWindow.dispose();

                    if(more) {
                        for (Question q : questions) {
                            if (q.getNum() == num) {
                                if (num == questions.size()) {
                                    askQuestion(q, questions, false);
                                } else {
                                    num++;
                                    askQuestion(q, questions, true);
                                }
                                break;
                            }
                        }
                    }
                    else{
                        setScore();
                    }
                }
            });
        }
        qWindow.add(qLabel,BorderLayout.NORTH);
        qWindow.add(choices,BorderLayout.CENTER);
        qWindow.add(submit,BorderLayout.SOUTH);
        qWindow.pack();
        qWindow.setVisible(true);

    }

    public void setScore(){
        try {
            FileReader reader = new FileReader(usernameFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] brokenLine = line.split("@!");
                if(brokenLine[0].equals(word)){
                    buffer.append(assignment.name + "@!" + assignment.totalPoints + "@!" + assignment.type.toString());
                    for(Question question:assignment.questions){
                        buffer.append("@!" + question.toString());
                    }
                    buffer.append("@!" + sum + "/" + assignment.totalPoints);
                }
                else{
                    buffer.append(line);
                    buffer.append("\n");
                }
            }
            bufferedReader.close();

            FileWriter writer = new FileWriter(usernameFile);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(buffer.toString());
            bufferedWriter.close();
        }
        catch(FileNotFoundException ex){}
        catch(IOException ex){}
    }
}
