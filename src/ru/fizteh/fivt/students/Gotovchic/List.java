package ru.fizteh.fivt.students.Gotovchic;

import java.util.Map;
import java.util.Set;

public class List extends Command {
    void listFunction(Map<String, String> storage) {
        Integer size = 0;
        Set k = storage.keySet();
        for (Object iter : k) {
            if (size < storage.size() - 1) {
                System.out.print(iter + ", ");
            } else {
                System.out.print(iter);
            }
            ++size;
        }
        System.out.println();
    }
}

