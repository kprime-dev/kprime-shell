package it.nipe.garage;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/*
* Scenaries:
* 1. A quanti quiz devo ancora rispondere ?
* TODO 2. Quali sono quelle a cui non risponde da più tempo?
* TODO 3. Quali sono le ultime risposte bagliate a questa domanda?
 */
public class ApplicationTest {

    @Test
    public void a_quanti_quiz_devo_ancora_rispondere() {
    /*
        // Given
        MockAppWithOneQuiz mockAppWithOneQuiz = new  MockAppWithOneQuiz();
        Application app = mockAppWithOneQuiz.invoke();
        Quiz quiz = mockAppWithOneQuiz.getQuiz();
        assertThat(app.getRemainingQuizNumber(),is(1l));
        // Do
        quiz.addAnswer("try-answer");
        // Then
        assertThat(app.getRemainingQuizNumber(),is(0l));
    */
    }

    /*
    private class MockAppWithOneQuiz {
        private Application app;
        private Quiz quiz;

        public Application getApp() {
            return app;
        }

        public Quiz getQuiz() {
            return quiz;
        }

        public Application invoke() {
            app = new Application();
            quiz = app.addNewQuiz("author","topic","question","correct-answer");
            return app;
        }
    }
    */
}