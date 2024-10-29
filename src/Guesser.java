import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Guesser {

    // Stores the party scores.
    private final Map<String, Integer> partyScores = new HashMap<>();
    private final String[] PARTIES = {"Democrat", "Republican", "Libertarian", "Independent"};

    public Guesser() {
        // init each part to 0 weights.
        for (String party : PARTIES) {
            partyScores.put(party, 0);
        }
    }

    public void askQuestions() {
        Scanner scanner = new Scanner(System.in);

        // Get the list of questions.
        List<Question> questions = getQuestions();

        StringBuilder responses = new StringBuilder();

        for (int i = 0; i < questions.size(); i++) {
            // Get the current question and display it to the user.
            Question q = questions.get(i);
            System.out.println(q.getText());

            // Print all four answers.
            for (int j = 0; j < q.getOptions().length; j++) {
                System.out.println((j + 1) + ". " + q.getOptions()[j]);
            }

            // Makes sure the user can't choose an invalid answer.
            int choice;
            while (true) {
                System.out.print("Please select an option (1-" + q.getOptions().length + "): ");
                choice = scanner.nextInt() - 1; // Convert to zero-based index

                // Check if choice is valid
                if (choice >= 0 && choice < q.getOptions().length) {
                    break; // Valid input
                } else {
                    System.out.println("Invalid option. Please select a number between 1 and " + q.getOptions().length + ".");
                }
            }
            // Appends the answer to later be saved in a .txt file
            responses.append("Q").append(i + 1).append(": ").append(q.getOptions()[choice]).append("\n");

            // updates the party scores
            updatePartyScores(q, choice);
        }

        String finalGuess = guessParty();
        System.out.println("Final guess: " + finalGuess);

        saveResponsesToFile(finalGuess, responses.toString());

        System.out.println("Which political party do you actually affiliate with?");
        System.out.println("1. Democrat\n2. Republican\n3. Libertarian\n4. Independent");
        int actualPartyIndex = scanner.nextInt() - 1;

        System.out.println("Thank you for your response.");

    }

    public List<Question> getQuestions() {
        // Create a list of question
        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What should the government do to help the poor?", // Question
                new String[]{
                        // Answers
                        "Make it easier to apply for assistance",
                        "Allow parents to use education funds for charter schools",
                        "Create welfare to work programs",
                        "Nothing"
                },
                new int[][]{
                        // Weights
                        {10, 2, 0, 0},
                        {0, 10, 2, 0},
                        {5, 8, 5, 2},
                        {0, 0, 0, 10}
                }));

        questions.add(new Question(
                "How do you feel about universal healthcare?",
                new String[]{
                        "Strongly support",
                        "Support with restrictions",
                        "Oppose",
                        "Strongly oppose"
                },
                new int[][]{{10, 3, 0, 0}, {5, 7, 2, 0}, {0, 8, 7, 0}, {0, 2, 10, 10}}));

        questions.add(new Question(
                "What should the government's role in climate change be?",
                new String[]{
                        "Aggressively address it",
                        "Passively support green initiatives",
                        "Let the free market handle it",
                        "It's not a priority"

                },
                new int[][]{{10, 3, 0, 0}, {5, 7, 0, 0}, {0, 8, 10, 2}, {0, 0, 5, 10}}));

        questions.add(new Question(
                "What is your stance on gun control?",
                new String[]{
                        "Support stricter laws",
                        "Support current laws",
                        "Loosen gun restrictions",
                        "No opinion"
                },
                new int[][]{{10, 3, 0, 0}, {5, 8, 2, 0}, {0, 5, 10, 5}, {0, 0, 0, 5}}));

        questions.add(new Question(
                "What is your stance on taxes?",
                new String[]{
                        "Increase taxes for the wealthy",
                        "Reduce taxes for corporations",
                        "Eliminate most taxes",
                        "Keep taxes the same"
                },
                new int[][]{{10, 0, 0, 0}, {0, 10, 2, 0}, {0, 5, 10, 5}, {0, 0, 2, 10}}));

        questions.add(new Question(
                "What is your view on immigration?",
                new String[]{
                        "Provide a path to citizenship",
                        "Stricter border control",
                        "Limit government involvement",
                        "No opinion"
                },
                new int[][]{{10, 0, 0, 0}, {2, 10, 5, 0}, {0, 5, 10, 0}, {0, 0, 0, 5}}));

        questions.add(new Question(
                "What is your stance on LGBTQ+ rights?",
                new String[]{
                        "Support full rights",
                        "Support civil unions",
                        "No government involvement",
                        "Oppose any recognition"
                },
                new int[][]{{10, 2, 0, 0}, {5, 8, 0, 0}, {0, 5, 10, 0}, {0, 0, 2, 10}}));

        questions.add(new Question(
                "What is your view on student loan forgiveness?",
                new String[]{
                        "Support full forgiveness",
                        "Support partial forgiveness",
                        "Oppose",
                        "No opinion"
                },
                new int[][]{{10, 5, 0, 0}, {5, 7, 0, 0}, {0, 10, 5, 2}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "How should the government handle the economy?",
                new String[]{
                        "Increase government spending",
                        "Cut taxes",
                        "Limit government intervention",
                        "Maintain current policies"
                },
                new int[][]{{10, 2, 0, 0}, {0, 10, 2, 0}, {0, 5, 10, 5}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "What is your view on criminal justice reform?",
                new String[]{
                        "Support defunding the police",
                        "Support increased police funding",
                        "Reduce government involvement",
                        "No opinion"
                },
                new int[][]{{10, 2, 0, 0}, {0, 10, 2, 0}, {0, 5, 10, 5}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "What is your view on foreign policy?",
                new String[]{
                        "Increase diplomatic engagement",
                        "Increase military spending",
                        "Reduce foreign interventions",
                        "Keep current policies"
                },
                new int[][]{{10, 3, 0, 0}, {5, 10, 0, 0}, {0, 2, 10, 2}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "What is your view on government surveillance?",
                new String[]{
                        "Reduce surveillance",
                        "Maintain current levels",
                        "Abolish government surveillance",
                        "Increase surveillance for safety"
                },
                new int[][]{{10, 5, 0, 0}, {5, 10, 0, 0}, {0, 2, 10, 0}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "How should the government handle drug policies?",
                new String[]{
                        "Legalize all drugs",
                        "Legalize marijuana only",
                        "Maintain current policies",
                        "Oppose legalization"
                },
                new int[][]{{10, 0, 10, 0}, {5, 5, 0, 0}, {0, 8, 0, 2}, {0, 0, 0, 10}}));

        questions.add(new Question(
                "What is your stance on trade policies?",
                new String[]{
                        "Support free trade",
                        "Support tariffs",
                        "Reduce government involvement",
                        "No opinion"
                },
                new int[][]{{10, 5, 10, 0}, {0, 10, 2, 0}, {0, 0, 10, 0}, {0, 0, 0, 5}}));

        questions.add(new Question(
                "What is your view on environmental regulations?",
                new String[]{
                        "Support stronger regulations",
                        "Support current regulations",
                        "Oppose regulations",

                        "No opinion"
                },
                new int[][]{{10, 5, 0, 0}, {5, 10, 0, 0}, {0, 2, 10, 0}, {0, 0, 0, 5}}));

        questions.add(new Question(
                "How do you feel about government subsidies for businesses?",
                new String[]{
                        "Support increased subsidies",
                        "Support targeted subsidies",
                        "Oppose all subsidies",
                        "Maintain current policies"
                },
                new int[][]{{10, 5, 0, 0}, {5, 8, 0, 0}, {0, 2, 10, 0}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "What is your view on public education funding?",
                new String[]{
                        "Increase funding for public schools",
                        "Support school choice programs",
                        "Reduce government role in education",
                        "Maintain current funding"
                },
                new int[][]{{10, 0, 0, 0}, {0, 10, 2, 0}, {0, 5, 10, 5}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "How should the government address poverty?",
                new String[]{
                        "Expand social programs",
                        "Encourage private sector solutions",
                        "Limit government role",
                        "No opinion"
                },
                new int[][]{{10, 2, 0, 0}, {5, 8, 2, 0}, {0, 5, 10, 0}, {0, 0, 2, 5}}));

        questions.add(new Question(
                "What is your view on minimum wage?",
                new String[]{
                        "Raise minimum wage",
                        "Keep current wage",
                        "Eliminate minimum wage",
                        "No opinion"
                },
                new int[][]{{10, 0, 0, 0}, {5, 10, 0, 0}, {0, 5, 10, 5}, {0, 0, 2, 5}}));


        return questions;
    }

    public void updatePartyScores(Question question, int answerIndex) {
        // Updates the party score based on user's answer
        int[] answerWeights = question.getWeights()[answerIndex];

        for (int i = 0; i < PARTIES.length; i++) {
            String party = PARTIES[i];
            partyScores.put(party, partyScores.get(party) + answerWeights[i]);
        }
    }

    public String guessParty() {
        String bestGuess = "";
        int highestScore = Integer.MIN_VALUE; // keeps track of the highest score when doing the for loop

        for (Map.Entry<String, Integer> entry : partyScores.entrySet()) {
            // Check if the current party's score is higher than the current highest score
            if (entry.getValue() > highestScore) {
                highestScore = entry.getValue();
                bestGuess = entry.getKey();
            }
        }

        return bestGuess;
    }

    // save responses to a file corresponding to the guessed party
    public void saveResponsesToFile(String party, String responses) {
        String fileName = party + ".txt";

        try (FileWriter writer = new FileWriter(fileName, true)) { // append to the file
            writer.write("Survey responses:\n");
            writer.write(responses);
            writer.write("------\n");
            System.out.println("Responses saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
