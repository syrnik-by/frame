package ru.autotestframework.document_steps.json_service;

import com.flipkart.zjsonpatch.InvalidJsonPatchException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

enum Operation {
    ADD("add"),
    REMOVE("remove"),
    REPLACE("replace"),
    MOVE("move"),
    COPY("copy"),
    TEST("test");

    private static final Map<String, Operation> OPS = createImmutableMap();

    private static Map<String, Operation> createImmutableMap() {
        Map<String, Operation> map = new HashMap<String, Operation>();
        map.put(ADD.rfcName, ADD);
        map.put(REMOVE.rfcName, REMOVE);
        map.put(REPLACE.rfcName, REPLACE);
        map.put(MOVE.rfcName, MOVE);
        map.put(COPY.rfcName, COPY);
        map.put(TEST.rfcName, TEST);
        return Collections.unmodifiableMap(map);
    }

    private String rfcName;

    Operation(String rfcName) {
        this.rfcName = rfcName;
    }

    public static Operation fromRfcName(String rfcName) throws InvalidJsonPatchException {
        if (rfcName == null) throw new InvalidJsonPatchException("rfcName cannot be null");
        Operation op = OPS.get(rfcName.toLowerCase());
        if (op == null) throw new InvalidJsonPatchException("unknown / unsupported operation " + rfcName);
        return op;
    }

    public String rfcName() {
        return this.rfcName;
    }
}
