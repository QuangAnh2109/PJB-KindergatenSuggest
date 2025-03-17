package fa.appcode.common.utils;

public class QueryConstant {
    private QueryConstant() {
    }

        public static final String USERS_BY_USERNAME = """
                SELECT email, password, CASE WHEN status_id = 1 THEN true ELSE false END
                FROM account_info
                WHERE email=?
            """;
//    public static final String USERS_BY_USERNAME = """
//                SELECT email, password,
//                       CASE
//                           WHEN delete_flg = 1 THEN false
//                           WHEN status_id = 0 THEN false
//                           ELSE true
//                       END
//                FROM account_info
//                WHERE email=?
//            """;

    public static final String AUTHORITIES_BY_USERNAME = """
                SELECT a.email, m.type_value
                FROM account_info a
                JOIN master_data m ON a.role_id = m.type_key
                WHERE type_name='ROLE' AND a.email=?
            """;
}
