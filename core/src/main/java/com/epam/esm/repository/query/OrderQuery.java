package com.epam.esm.repository.query;

public final class OrderQuery {

    public static final String FIND_COUNT_ALL_RECORDS = """
            SELECT COUNT(*) FROM orders WHERE user_id = :id""";
}
