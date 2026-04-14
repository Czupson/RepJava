package Pd01;

public class GradeBook {
    static final String[] STUDENTS = {"Ala Kowalska", "Bartek Nowak", "Celina Zielińska", "Damian Wójcik"
    };
    static final String[] SUBJECTS = {
            "Matematyka", "Polski", "Angielski", "Historia", "Fizyka"
    };
    static final int[][] GRADES = {
            { 5, 4, 6, 3, 5 },
            { 3, 3, 4, 2, 0 },
            { 6, 5, 5, 6, 6 },
            { 2, 1, 3, 2, 3 }
    };

    static final String CSV_DATA = """
            name,Matematyka,Polski,Angielski,Historia,Fizyka
            Ala Kowalska,5,4,6,3,5
            Bartek Nowak,3,3,4,2,0
            Celina Zielińska,6,5,5,6,6
            Damian Wójcik,2,1,3,2,3
            Ewelina Lis,4,4,5,4,5""";

    static double average(int[] grades) {
        int sum = 0;
        int count = 0;

        for (int grade : grades) {
            if (grade != 0) {
                sum += grade;
                count++;
            }
        }
        return count == 0 ? -1.0 : (double) sum / count;
    }

    static double averageForStudent(int[][] grades, int studentIndex) {
        return average(grades[studentIndex]);
    }

    static double averageForSubject(int[][] grades, int subjectIndex) {
        int sum = 0;
        int count = 0;

        for (int[] row : grades) {
            int grade = row[subjectIndex];

            if (grade != 0) {
                sum += grade;
                count++;
            }
        }

        return count == 0 ? -1.0 : (double) sum / count;
    }

    static int maxRecursive(int[][] grades) {
        return maxRecursive(grades, 0, 0);
    }

    private static int maxRecursive(int[][] grades, int row, int col) {
        if (row == grades.length) {
            return Integer.MIN_VALUE;
        }
        if (col == grades[row].length) {
            return maxRecursive(grades, row + 1, 0);
        }

        int current = grades[row][col];
        int next = maxRecursive(grades, row, col + 1);

        return Math.max(current, next);
    }
    static String classify(double average) {
        int bucket =
                average < 0 ? -1 :
                        average < 2.0 ? 0 :
                                average < 3.5 ? 1 :
                                        average < 4.5 ? 2 :
                                                average < 5.5 ? 3 : 4;
        return switch (bucket){
            case -1 -> "brak ocen";
            case 0 -> "zagrożony";
            case 1 -> "przeciętny";
            case 2 -> "dobry";
            case 3 -> "bardzo dobry";
            default -> "celujący";
        };
    }

    static void printBasicReport(String[] students, int[][] grades) {
        for (int i = 0; i < students.length; i++) {
            double avg = averageForStudent(grades, i);

            System.out.printf("%s | %.2f | %s%n",
                    students[i],
                    avg,
                    classify(avg));
        }
        System.out.println("Maksymalna ocena: " + maxRecursive(grades));
    }

    static String[][] parseCSV(String csv) {
        int rows = 1;

        for (int i = 0; i < csv.length(); i++) {
            if (csv.charAt(i) == '\n') rows++;
        }

        String[][] result = new String[rows][];
        String[] temp = new String[100];

        int row = 0;
        int col = 0;
        int fieldStart = 0;

        for (int i = 0; i <= csv.length(); i++) {
            boolean end = i == csv.length();
            char c = end ? '\n' : csv.charAt(i);

            if (c == ',' || c == '\n') {
                temp[col++] = csv.substring(fieldStart, i);
                fieldStart = i + 1;

                if (c == '\n') {
                    result[row] = new String[col];

                    for (int j = 0; j < col; j++) {
                        result[row][j] = temp[j];
                    }

                    row++;
                    col = 0;
                }
            }
        }
        return result;
    }

    static int[][] extractGrades(String[][] parsed) {
        int[][] grades = new int[parsed.length - 1][];

        for (int i = 1; i < parsed.length; i++) {
            grades[i - 1] = new int[parsed[i].length - 1];

            for (int j = 1; j < parsed[i].length; j++) {
                grades[i - 1][j - 1] = Integer.parseInt(parsed[i][j]);
            }
        }
        return grades;
    }

    static String[] extractNames(String[][] parsed) {
        String[] names = new String[parsed.length - 1];

        for (int i = 1; i < parsed.length; i++) {
            names[i - 1] = parsed[i][0];
        }
        return names;
    }

    static int[] rankStudentsByAverage(String[] names, int[][] grades) {
        int n = names.length;

        int[] idx = new int[n];
        double[] keys = new double[n];

        for (int i = 0; i < n; i++) {
            idx[i] = i;
            keys[i] = average(grades[i]);
        }

        quicksortIndices(idx, keys, 0, n - 1);
        return idx;
    }

    private static void quicksortIndices(int[] idx, double[] keys, int lo, int hi) {
        if (lo >= hi) return;

        int p = partitionIndices(idx, keys, lo, hi);
        quicksortIndices(idx, keys, lo, p - 1);
        quicksortIndices(idx, keys, p + 1, hi);
    }

    private static int partitionIndices(int[] idx, double[] keys, int lo, int hi) {
        double pivot = keys[idx[hi]];
        int i = lo;

        for (int j = lo; j < hi; j++) {
            if (keys[idx[j]] > pivot) {
                swap(idx, i, j);
                i++;
            }
        }

        swap(idx, i, hi);
        return i;
    }

    static void printGradeHistogram(int[][] grades) {
        int[] counts = new int[7];
        int total = 0;

        for (int[] row : grades) {
            for (int grade : row) {
                if (grade != 0) {
                    counts[grade]++;
                    total++;
                }
            }
        }

        int max = 0;
        for (int i = 1; i <= 6; i++) {
            if (counts[i] > max) max = counts[i];
        }

        System.out.println("Histogram ocen (łącznie " + total + "):");

        for (int i = 1; i <= 6; i++){
            int bars = max > 40 ? counts[i] * 40 / max : counts[i];

            System.out.print(i + ": ");
            for (int j = 0; j < bars; j++) {
                System.out.print("*");
            }
            System.out.println(" (" + counts[i] + ")");
        }
    }

    static double median(int[] grades){
        int count = 0;
        for (int grade : grades) {
            if (grade != 0) count++;
        }
        if (count == 0)
            return -1.0;

        int[] values = new int[count];
        int idx = 0;

        for (int grade : grades) {
            if (grade != 0) values[idx++] = grade;
        }

        quicksortInt(values, 0, values.length - 1);

        if (values.length % 2 == 1){
            return values[values.length / 2];
        }

        return (values[values.length / 2 - 1] + values[values.length / 2]) / 2.0;
    }

    static double stddev(int[] grades) {
        double avg = average(grades);
        if (avg < 0) return -1.0;

        int count = 0;
        double sumSq = 0;

        for (int grade : grades) {
            if (grade != 0){
                double diff = grade - avg;
                sumSq += diff * diff;
                count++;
            }
        }

        if (count < 2) return 0.0;

        return Math.sqrt(sumSq / (count - 1));
    }

    static double[] movingAverage(int[] grades, int window) {
        double[] result = new double[grades.length];

        for (int i = 0; i < grades.length; i++) {
            int sum = 0;
            int count = 0;

            for (int j = i; j >= 0 && count < window; j--) {
                if (grades[j] != 0) {
                    sum += grades[j];
                    count++;
                }
            }
            result[i] = count < window ? -1.0 : (double) sum / count;
        }
        return result;
    }

    static boolean canBePromoted(int[] grades) {
        int belowThree = 0;
        int sum = 0;
        int count = 0;

        for (int grade : grades) {
            if (grade == 0) continue;

            if (grade == 1) return false;

            if (grade < 3) belowThree++;

            sum += grade;
            count++;
        }

        if (belowThree > 2) return false;
        if (count == 0) return false;

        return (double) sum / count >= 2.5;
    }

    static void printRankingTable(String[] names, int[][] grades) {
        int [] ranking = rankStudentsByAverage(names, grades);

        int nameWidth = 4;
        for(String name : names) {
            if (name.length() > nameWidth){
                nameWidth = name.length();
            }
        }

        System.out.printf(
                "%2s | %-" + nameWidth + "s | %6s | %-12s%n",
                "Lp",
                "Imię",
                "Średnia",
                "Klasyfikacja"
        );

        System.out.println("----------------------------------------------");

        for (int i = 0; i < ranking.length; i++) {
            int idx = ranking[i];
            double avg = average(grades[idx]);

            System.out.printf(
                    "%2d | %-" + nameWidth + "s | %6.2f | %-12s%n",
                    i + 1,
                    names[idx],
                    avg,
                    classify(avg));
        }
    }

    private static void quicksortInt(int[] arr, int lo, int hi) {
        if (lo >= hi) return;

        int p = partitionInt(arr, lo, hi);

        quicksortInt(arr, lo, p - 1);
        quicksortInt(arr, p + 1, hi);
    }

    private static int partitionInt(int[] arr, int lo, int hi) {
        int pivot = arr[hi];
        int i = lo;

        for (int j = lo; j < hi; j++) {
            if (arr[j] < pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, hi);
        return i;
    }

    private static void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    public static void main(String[] args) {
        String[][] parsed = parseCSV(CSV_DATA);
        String[] names = extractNames(parsed);
        int[][] grades = extractGrades(parsed);

        printBasicReport(names, grades);
        printGradeHistogram(grades);
        printRankingTable(names, grades);
    }
}
