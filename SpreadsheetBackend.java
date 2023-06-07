import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * SpreadsheetBackend Class initialises the hashMap in a constructor
 */
public class SpreadsheetBackend {
    private final Map<String, Object> cells;

    public SpreadsheetBackend() {
        this.cells = new HashMap<>();
    }

    /**
     * Main class involved test operations
     */
    public static void main(String[] args) {
        SpreadsheetBackend spreadsheet = new SpreadsheetBackend();

        spreadsheet.setCellValue("A1", 13);
        spreadsheet.setCellValue("A2", 14);
        System.out.println("A1 = " + spreadsheet.getCellValue("A1"));

        spreadsheet.setCellValue("A3", "=A1+A2");
        System.out.println("A3 = " + spreadsheet.getCellValue("A3"));

        spreadsheet.setCellValue("A4", "=A1+A2+A3");
        System.out.println("A4 = " + spreadsheet.getCellValue("A4"));

    }

    /**
     * setCellValue sets formula/value to Map
     */
    public void setCellValue(String cellId, Object value) {
        cells.put(cellId, value);
    }

    /**
     * getCellValue retrives value from map, it computes the real value if its a formula
     */
    public int getCellValue(String cellId) {
        Object value = cells.get(cellId);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            String formula = (String) value;
            if (formula.startsWith("=")) {
                return evaluateFormula(formula.substring(1));
            }
        }
        throw new IllegalArgumentException("Cell value is null: " + cellId); //Throws IllegalArgumentException if value is null
    }

    /**
     * evaluateFormula method is called from getValue method for computing the fromula. it also other method for computing *,-,/ operations
     */
    private int evaluateFormula(String formula) {
        String[] cellRefs = formula.split("\\+");
        int sum = 0;
        for (String cellRef : cellRefs) {
            String trimmedRef = cellRef.trim();
            if (trimmedRef.contains("*")) {
                sum += evaluateFormulaMultiply(trimmedRef);
            }
            if (trimmedRef.contains("/")) {
                sum += evaluateFormulaDivision(trimmedRef);
            }
            if (trimmedRef.contains("-")) {
                sum += evaluateFormulaSubtract(trimmedRef);
            } else if (Pattern.matches("[^/*-]*", trimmedRef)) {
                int cellValue = getCellValue(trimmedRef);
                sum += cellValue;
            }

        }
        return sum;
    }

    /**
     * evaluateFormulaMultiply computed formula involving mutiplication operation
     */
    private int evaluateFormulaMultiply(String formula) {
        String[] cellRefs = formula.split("\\*");
        int sum = 1;
        for (String cellRef : cellRefs) {
            String trimmedRef = cellRef.trim();
            int cellValue = getCellValue(trimmedRef);
            sum *= cellValue;
        }
        return sum;
    }

    /**
     * evaluateFormulaMultiply computed formula involving subtraction operation
     */
    private int evaluateFormulaSubtract(String formula) {
        String[] cellRefs = formula.split("\\-");
        int sum = 1;
        for (String cellRef : cellRefs) {
            String trimmedRef = cellRef.trim();
            int cellValue = getCellValue(trimmedRef);
            sum -= cellValue;
        }
        return sum;
    }

    /**
     * evaluateFormulaMultiply computed formula involving division operation. Since value are int, float value are discared, So 0.5==0
     */
    private int evaluateFormulaDivision(String formula) {
        String[] cellRefs = formula.split("\\/");
        int sum = 0;
        for (String cellRef : cellRefs) {
            String trimmedRef = cellRef.trim();
            int cellValue = getCellValue(trimmedRef);
            if (sum == 0) {
                sum = cellValue;
            } else {
                sum /= cellValue;
            }
        }
        return sum;
    }
}
