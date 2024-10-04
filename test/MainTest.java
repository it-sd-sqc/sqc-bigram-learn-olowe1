import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

import edu.cvtc.bigram.*;

@SuppressWarnings({"SpellCheckingInspection"})
class MainTest {
  @Test
  void createConnection() {
    assertDoesNotThrow(
        () -> {
          Connection db = Main.createConnection();
          assertNotNull(db);
          assertFalse(db.isClosed());
          db.close();
          assertTrue(db.isClosed());
        }, "Failed to create and close connection."
    );
  }

  @Test
  void reset() {
    Main.reset();
    assertFalse(Files.exists(Path.of(Main.DATABASE_PATH)));
  }

  @Test
  void mainArgs() {
    assertAll(
        () -> {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          System.setOut(new PrintStream(out));
          Main.main(new String[]{"--version"});
          String output = out.toString();
          assertTrue(output.startsWith("Version "));
        },
        () -> {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          System.setOut(new PrintStream(out));
          Main.main(new String[]{"--help"});
          String output = out.toString();
          assertTrue(output.startsWith("Add bigrams"));
        },
        () -> assertDoesNotThrow(() -> {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          System.setErr(new PrintStream(out));
          Main.main(new String[]{"--reset"});
          String output = out.toString();
          assertTrue(output.startsWith("Expected"));
        }),
        () -> assertDoesNotThrow(() -> Main.main(new String[]{"./sample-texts/non-existant-file.txt"})),
        () -> assertDoesNotThrow(() -> Main.main(new String[]{"./sample-texts/empty.txt"}))
    );
  }

  // TODO: Create your test(s) below. /////////////////////////////////////////
  @Test
    public void testBigramsWithExcessiveSpaces() {
        String input = "    hello    world   !   "; // Simulating input with excessive spaces
        String expectedOutput = "hello world"; // Expected output should ignore extra spaces

        // Assuming getBigrams is a static method in Main class
        String result = Main.getBigrams(input).trim(); // Invoke the method to extract bigrams
        
        // Validate the result
        assertEquals(expectedOutput, result); // Validate that extracted bigrams match expectations
    }

  @Test
    public void testBigramsWithPunctuation() {
        String input = "Hello, world!";
        String expectedOutput = "Hello world"; // Depending on punctuation rules
        
        String result = Main.getBigrams(input).trim(); 
        
        assertEquals(expectedOutput, result); // Validate the expected outcome
    }
}