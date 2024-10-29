import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Guesser {

    // Stores the party scores.
    private final Map<String, Integer> partyScores = new HashMap<>();
    private final String[] PARTIES = {"Democrat", "Republican", "Libertarian", "Independent"};
    private final String fileName = "data.txt"; // File to store response weights

    public Guesser() {
        // init each part to 0 weights.
        for (String party : PARTIES) {
            partyScores.put(party, 0);
        }
    }

    public void askQuestions() {
        Scanner scanner = new Scanner(System.in);

        List<Question> questions = getQuestions();
        List<Integer> userResponses = new ArrayList<>(); // To store raw answer numbers

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

            userResponses.add(choice + 1);

            // updates the party scores
            updatePartyScores(q, choice);
        }


        System.out.println("Which political party do you actually affiliate with?");
        System.out.println("1. Democrat\n2. Republican\n3. Libertarian\n4. Independent");

        int actualPartyIndex = scanner.nextInt() - 1;
        String actualParty = PARTIES[actualPartyIndex];

        // guess the party based on this session
        String finalGuess = guessParty(partyScores);
        System.out.println("Your party affiliation is: " + finalGuess);

        // uses previous saved weights to guess the user's political party.
        String storedPartyGuess = guessPartyFromStoredData();
        System.out.println("The guessed party from stored data is: " + storedPartyGuess);

        // save the responses and weights to the file
        saveResponsesToFile(userResponses);

        // compare if the weighted data is the save as the one in this survey.
        if (actualParty.equals(storedPartyGuess)) {
            System.out.println("The stored data prediction matches your declared party.");
        } else {
            System.out.println("The stored data prediction does not match your declared party.");
        }

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

    // method to retrieve stored data
    public String guessPartyFromStoredData() {
        Map<String, Integer> storedScores = new HashMap<>();
        for (String party : PARTIES) {
            storedScores.put(party, 0);
        }

        try {
            // read the file line by line
            File file = new File(fileName);
            if (!file.exists() || file.length() == 0) {
                System.out.println("No previous data recorded.");
                return null; // No data to process
            }

            List<String> lines = Files.readAllLines(Paths.get(fileName));

            for (String line : lines) {
                // parse the line of weights
                String[] weights = line.split(",");
                if (weights.length != PARTIES.length) {
                    System.err.println("Skipping malformed line: " + line);
                    continue; // skip lines that don't have exactly 4 values
                }
                for (int i = 0; i < weights.length; i++) {
                    int weight = Integer.parseInt(weights[i]);
                    storedScores.put(PARTIES[i], storedScores.get(PARTIES[i]) + weight);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        return guessParty(storedScores);
    }

    public String guessParty(Map<String, Integer> scores) {
        String bestGuess = "";
        int highestScore = Integer.MIN_VALUE; // keeps track of the highest score when doing the for loop

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            // Check if the current party's score is higher than the current highest score
            if (entry.getValue() > highestScore) {
                highestScore = entry.getValue();
                bestGuess = entry.getKey();
            }
        }

        return bestGuess;
    }

    // save responses to a file corresponding to the guessed party
    public void saveResponsesToFile(List<Integer> choices) {
        File file = new File(fileName);


        try (FileWriter writer = new FileWriter(fileName, true)) {

            // create file if it doesn't exists.
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("File 'data.txt' created.");
            }

            StringBuilder weightLine = new StringBuilder();
            for (int i = 0; i < PARTIES.length; i++) {
                weightLine.append(partyScores.get(PARTIES[i]));
                if (i < PARTIES.length - 1) {
                    weightLine.append(",");
                }
            }
            writer.write(weightLine.toString() + "\n");
            System.out.println("Responses saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}
