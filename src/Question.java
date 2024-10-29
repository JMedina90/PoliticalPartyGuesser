public class Question {
    private String text;
    private String[] options;
    private int[][] weights; // Array of weights for each answer option per party

    public Question(String text, String[] options, int[][] weights) {
        this.text = text;
        this.options = options;
        this.weights = weights;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public int[][] getWeights() {
        return weights;
    }
}