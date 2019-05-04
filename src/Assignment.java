import java.util.ArrayList;

public class Assignment {

    String name;
    int totalPoints;
    AssignmentType type;
    ArrayList<Question> questions;

    public Assignment(String name,AssignmentType type){
        this.name = name;
        this.type = type;
        questions = new ArrayList<>();
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public void setTotalPoints(){
        for(Question question:questions){
            totalPoints += question.getPoints();
        }
    }

    public ArrayList<Question> getQuestions(){return questions;}

    public void setPoints(int points){
        totalPoints = points;
    }

    @Override
    public String toString(){
        //Assignment values separated by @! and question values separated by @&
        StringBuffer buffer = new StringBuffer();
        buffer.append(name + "@!" + totalPoints + "@!" + type.toString());
        for(Question question:questions){
            buffer.append("@!" + question.toString());
        }
        buffer.append("@!-");
        return buffer.toString();
    }

    public enum AssignmentType{
        Homework,
        Quiz,
        Test
    }
}
