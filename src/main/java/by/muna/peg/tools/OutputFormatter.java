package by.muna.peg.tools;

import java.util.List;

public class OutputFormatter {
    @SuppressWarnings("unchecked")
    public static String format(Object o) {
        if (o instanceof List) {
            List<Object> list = (List<Object>) o;

            StringBuilder sb = new StringBuilder();

            sb.append('[');

            boolean isFirst = true;
            for (Object e : list) {
                if (isFirst) isFirst = false;
                else sb.append(", ");

                sb.append(OutputFormatter.format(e));
            }

            sb.append(']');

            return sb.toString();
        } else if (o instanceof Character) {
            return '\'' + o.toString() + '\'';
        } else if (o instanceof String) {
            return '"' + o.toString() + '"';
        } else {
            return o.toString();
        }
    }
}
