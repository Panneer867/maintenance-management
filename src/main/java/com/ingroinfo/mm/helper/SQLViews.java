package com.ingroinfo.mm.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SQLViews {

	@Autowired
	private JdbcTemplate jdbcTemplate;


	private static final String MONTHWISE_TOTALSTOCKS = "CREATE OR REPLACE VIEW DASHBOARD_MONTHWISE_TOTALSTOCKS AS "
			+ "SELECT TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,TO_CHAR( DATE_CREATED, 'YYYY' ) AS YEAR,"
			+ "SUM( QUANTITY ) AS TOTAL_QUANTITY FROM (SELECT DATE_CREATED,QUANTITY FROM "
			+ "MM_INWARD_APPROVED_SPARES WHERE DATE_CREATED >= TRUNC( SYSDATE, 'YEAR' ) "
			+ "AND DATE_CREATED < ADD_MONTHS( TRUNC( SYSDATE, 'YEAR' ), 12 ) UNION ALL SELECT DATE_CREATED,"
			+ "QUANTITY FROM MM_INWARD_APPROVED_MATERIALS WHERE DATE_CREATED >= TRUNC( SYSDATE, 'YEAR' ) "
			+ "AND DATE_CREATED < ADD_MONTHS( TRUNC( SYSDATE, 'YEAR' ), 12 ) UNION ALL SELECT DATE_CREATED,"
			+ "QUANTITY FROM MM_INWARD_APPROVED_TOOLS WHERE DATE_CREATED >= TRUNC( SYSDATE, 'YEAR' ) "
			+ "AND DATE_CREATED < ADD_MONTHS( TRUNC( SYSDATE, 'YEAR' ), 12 )) ALL_TABLES GROUP BY "
			+ "TO_CHAR( DATE_CREATED, 'MON' ),TO_CHAR( DATE_CREATED, 'YYYY' ) ORDER BY "
			+ "TO_DATE( YEAR || MONTH_NAME || '01', 'YYYYMONDD' ) ASC";

	private static final String MONTHWISE_STOCKS = "CREATE OR REPLACE VIEW DASHBOARD_MONTHWISE_STOCKS AS "
			+ "SELECT 'SPARES' AS STOCK_TYPE,TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,"
			+ "EXTRACT( YEAR FROM DATE_CREATED ) AS YEAR,SUM( QUANTITY ) AS TOTAL_QUANTITY FROM "
			+ "MM_INWARD_APPROVED_SPARES WHERE DATE_CREATED >= SYSDATE - INTERVAL '1' YEAR GROUP BY "
			+ "EXTRACT( YEAR FROM DATE_CREATED ),TO_CHAR( DATE_CREATED, 'MON' ) UNION SELECT "
			+ "'MATERIALS' AS STOCK_TYPE,TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,"
			+ "EXTRACT( YEAR FROM DATE_CREATED ) AS YEAR,SUM( QUANTITY ) AS TOTAL_QUANTITY FROM "
			+ "MM_INWARD_APPROVED_MATERIALS WHERE DATE_CREATED >= SYSDATE - INTERVAL '1' YEAR GROUP BY "
			+ "EXTRACT( YEAR FROM DATE_CREATED ),TO_CHAR( DATE_CREATED, 'MON' ) UNION SELECT "
			+ "'TOOLS' AS STOCK_TYPE,TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,"
			+ "EXTRACT( YEAR FROM DATE_CREATED ) AS YEAR,SUM( QUANTITY ) AS TOTAL_QUANTITY FROM "
			+ "MM_INWARD_APPROVED_TOOLS WHERE DATE_CREATED >= SYSDATE - INTERVAL '1' YEAR GROUP BY "
			+ "EXTRACT( YEAR FROM DATE_CREATED ),TO_CHAR( DATE_CREATED, 'MON' ) ORDER BY YEAR ASC,"
			+ "MONTH_NAME ASC,STOCK_TYPE ASC ";

	private static final String OUTWARD_STOCKS = "CREATE OR REPLACE VIEW DASHBOARD_OUTWARD_STOCKS AS SELECT "
			+ "UPPER( TO_CHAR( DATE_CREATED, 'MON' ) ) AS MONTH_NAME, "
			+ "SUM( CASE WHEN STOCK_TYPE = 'ML' THEN FINAL_QUANTITY ELSE 0 END ) AS MATERIALS_QUANTITY, "
			+ "SUM( CASE WHEN STOCK_TYPE = 'SP' THEN FINAL_QUANTITY ELSE 0 END ) AS SPARES_QUANTITY, "
			+ "SUM( CASE WHEN STOCK_TYPE = 'TE' THEN FINAL_QUANTITY ELSE 0 END ) AS TOOLS_QUANTITY  FROM "
			+ "MM_APPROVED_WORKORDER_ITEMS WHERE "
			+ "EXTRACT( YEAR FROM DATE_CREATED ) = EXTRACT( YEAR FROM SYSDATE )  GROUP BY "
			+ "UPPER( TO_CHAR( DATE_CREATED, 'MON' ))  ORDER BY TO_DATE( MONTH_NAME, 'MON' ) ";

	private static final String STOCKS_RETURN = "CREATE OR REPLACE VIEW DASHBOARD_STOCKS_RETURN AS SELECT "
			+ "CASE WHEN STOCK_TYPE = 'ML' THEN 'MATERIALS' WHEN STOCK_TYPE = 'TE' THEN 'TOOLS' "
			+ "WHEN STOCK_TYPE = 'SP' THEN 'SPARES' ELSE STOCK_TYPE END AS STOCK_TYPE, "
			+ "SUM(RETURN_QUANTITY) AS TOTAL_QUANTITY FROM MM_APPROVED_STOCKS_RETURN GROUP BY STOCK_TYPE";

	@PostConstruct
	public void createView() {
		jdbcTemplate.execute(MONTHWISE_STOCKS);
		jdbcTemplate.execute(MONTHWISE_TOTALSTOCKS);
		jdbcTemplate.execute(OUTWARD_STOCKS);
		jdbcTemplate.execute(STOCKS_RETURN);
	}
}
