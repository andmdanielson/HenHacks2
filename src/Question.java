import java.util.HashMap;

public class Question {

    int num,points;
    String question;
    QuestionType type;
    HashMap<String,Boolean> answers = new HashMap<>();

    public Question(int num, String question, QuestionType type,int points){
        this.num = num;
        this.question = question;
        this.type = type;
        this.points = points;
    }

    public void addAnswer(String answer, boolean value){
        answers.put(answer,value);
    }

    public int getPoints(){return points;}
    public int getNum(){return num;}

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("" + num + "@&" + question + "@&" + type.toString() + "@&"  +points);
        for(String answer:answers.keySet()){
            buffer.append("@&" + answer + "@&" + answers.get(answer));
        }
        return buffer.toString();
    }

    public enum QuestionType{
        MultipleChoice,
        TrueOrFalse,
        FillInTheBlank,
        ShortResponse
    }
}
