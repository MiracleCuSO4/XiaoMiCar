package com.xiaomi.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SafeFormulaCheck {
    // 允许的变量名
    private static final Set<String> allowedVariables = new HashSet<>(Arrays.asList("Mx", "Mi", "Ix", "Ii"));
    // 允许的操作符
    private static final String allowedOperators = "+-*/()";

    /**
     * 检查公式是否安全,注意这样检查可能并不充分
     * @param formula 公式字符串
     * @return 如果公式安全则返回true，否则返回false
     */
    public static boolean isSafeFormula(String formula) {
        // 去除空白字符
        formula = formula.replaceAll("\\s", "");

        // 提取出所有 ${} 包围的变量
        List<String> variables = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\}");
        Matcher matcher = pattern.matcher(formula);
        while (matcher.find()) {
            String variable = matcher.group(1);
            // 检查变量是否在允许的变量名列表中
            if (!allowedVariables.contains(variable)) {
                return false;
            }
            variables.add(variable);
        }

        // 替换所有合法变量为一个数字，以便检查其余部分是否安全，可以让后面的代码检查是否有嵌套${}
        String sanitizedFormula = formula;
        for (String variable : variables) {
            sanitizedFormula = sanitizedFormula.replace("${" + variable + "}", "1");
        }

        // 检查公式中是否包含不允许的字符
        for (char c : sanitizedFormula.toCharArray()) {
            if (!Character.isDigit(c) && c != '.' && !allowedOperators.contains(String.valueOf(c))) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println(SafeFormulaCheck.isSafeFormula("10.2 * ( ${Mx} - ${Mi} ) / ${Mx}")); // true
        System.out.println(SafeFormulaCheck.isSafeFormula("${Mx}-${Mi}")); // true
        System.out.println(SafeFormulaCheck.isSafeFormula("10 * ( ${sys} - ${Mi} ) / ${Mx}")); // false
        System.out.println(SafeFormulaCheck.isSafeFormula("10 * ( ${ ${Mx} - ${Mx} } - ${Mi} ) / ${Mx}")); // false
        System.out.println(SafeFormulaCheck.isSafeFormula("10 * ( ${Mx} - ${Mi} ) / ${Mx} * ${{Mx}}")); // false
    }
}
