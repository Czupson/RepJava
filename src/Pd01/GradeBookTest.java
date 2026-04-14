package Pd01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GradeBookTest {
    @Test
    void classifyShouldReturnCorrectLabels() {
        assertEquals("brak ocen", GradeBook.classify(-1));
        assertEquals("zagrożony", GradeBook.classify(1.5));
        assertEquals("przeciętny", GradeBook.classify(2.5));
        assertEquals("dobry", GradeBook.classify(4.0));
        assertEquals("bardzo dobry", GradeBook.classify(5.0));
        assertEquals("celujący", GradeBook.classify(5.8));
    }

    @Test
    void averageShouldCalculateNormally() {
        assertEquals(4.0, GradeBook.average(new int[]{4, 4, 4}), 0.001);
    }

    @Test
    void averageShouldIgnoreZeros() {
        assertEquals(5.0, GradeBook.average(new int[]{5, 0, 5}), 0.001);
    }

    @Test
    void averageShouldReturnMinusOneForOnlyZeros() {
        assertEquals(-1.0, GradeBook.average(new int[]{0, 0, 0}), 0.001);
    }

    @Test
    void averageForStudentShouldWork() {
        int[][] grades = {
                {5, 4, 3},
                {2, 2, 2}
        };

        assertEquals(4.0, GradeBook.averageForStudent(grades, 0), 0.001);
    }

    @Test
    void averageForSubjectShouldWork() {
        int[][] grades = {
                {5, 4},
                {3, 2},
                {1, 0}
        };

        assertEquals(3.0, GradeBook.averageForSubject(grades, 0), 0.001);
    }

    @Test
    void maxRecursiveShouldFindMaximum() {
        int[][] grades = {
                {1, 2},
                {6, 3}
        };

        assertEquals(6, GradeBook.maxRecursive(grades));
    }

    @Test
    void parseCsvShouldParseCorrectly() {
        String csv = """
            name,Math
            Ala,5
            Bartek,4""";

        String[][] parsed = GradeBook.parseCSV(csv);

        assertEquals("name", parsed[0][0]);
        assertEquals("Math", parsed[0][1]);
        assertEquals("Ala", parsed[1][0]);
        assertEquals("5", parsed[1][1]);
        assertEquals("Bartek", parsed[2][0]);
        assertEquals("4", parsed[2][1]);
    }

    @Test
    void extractNamesShouldWork() {
        String[][] parsed = {
                {"name", "Math"},
                {"Ala", "5"},
                {"Bartek", "4"}
        };

        assertArrayEquals(
                new String[]{"Ala", "Bartek"},
                GradeBook.extractNames(parsed)
        );
    }

    @Test
    void extractGradesShouldWork() {
        String[][] parsed = {
                {"name", "Math", "Polski"},
                {"Ala", "5", "4"},
                {"Bartek", "3", "2"}
        };

        int[][] expected = {
                {5, 4},
                {3, 2}
        };

        assertArrayEquals(expected, GradeBook.extractGrades(parsed));
    }

    @Test
    void rankStudentsByAverageShouldSortDescending() {
        String[] names = {"Ala", "Bartek", "Celina"};

        int[][] grades = {
                {5, 5},
                {3, 3},
                {6, 6}
        };

        assertArrayEquals(
                new int[]{2, 0, 1},
                GradeBook.rankStudentsByAverage(names, grades)
        );
    }

    @Test
    void medianShouldWorkForOddCount() {
        assertEquals(4.0, GradeBook.median(new int[]{5, 4, 3}), 0.001);
    }

    @Test
    void medianShouldWorkForEvenCount() {
        assertEquals(4.5, GradeBook.median(new int[]{4, 5, 3, 6}), 0.001);
    }

    @Test
    void medianShouldIgnoreZeros() {
        assertEquals(5.0, GradeBook.median(new int[]{0, 5, 5}), 0.001);
    }

    @Test
    void stddevShouldCalculateCorrectly() {
        assertEquals(1.0, GradeBook.stddev(new int[]{4, 5, 6}), 0.001);
    }

    @Test
    void movingAverageShouldWork() {
        assertArrayEquals(
                new double[]{-1.0, -1.0, 2.0, 3.0},
                GradeBook.movingAverage(new int[]{1, 2, 3, 4}, 3),
                0.001
        );
    }

    @Test
    void canBePromotedShouldReturnTrueForValidStudent() {
        assertTrue(GradeBook.canBePromoted(new int[]{3, 3, 4, 5}));
    }

    @Test
    void canBePromotedShouldReturnFalseIfHasOne() {
        assertFalse(GradeBook.canBePromoted(new int[]{1, 5, 5}));
    }

    @Test
    void canBePromotedShouldReturnFalseIfTooManyGradesBelowThree() {
        assertFalse(GradeBook.canBePromoted(new int[]{2, 2, 2, 5}));
    }

    @Test
    void canBePromotedShouldReturnFalseIfAverageTooLow() {
        assertFalse(GradeBook.canBePromoted(new int[]{2, 2, 3}));
    }
}
