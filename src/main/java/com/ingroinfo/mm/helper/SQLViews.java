package com.ingroinfo.mm.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SQLViews {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String WORKORDER_REQUESTS = "CREATE OR REPLACE VIEW WORKORDER_REQUESTS AS "
			+ "SELECT DISTINCT COMPL_DTLS, COMPL_NO, CONTACT_NO,DEPARTMENT_NAME,DIVISION,SUB_DIVISION,START_DATE,END_DATE,"
			+ "INDENT_NO,USERNAME,WORK_ORDER_NO,WORK_PRIORITY,WORK_SITE FROM MM_WORKORDER_ITEMS_REQUEST";

	private static final String MONTHWISE_TOTALSTOCKS = "CREATE OR REPLACE VIEW DASHBOARD_MONTHWISE_TOTALSTOCKS AS "
			+ "SELECT TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,TO_CHAR( DATE_CREATED, 'YYYY' ) AS YEAR,"
			+ "SUM( ADDED_QUANTITY ) AS TOTAL_QUANTITY FROM (SELECT DATE_CREATED,ADDED_QUANTITY FROM "
			+ "MM_INWARD_APPROVED_SPARES WHERE DATE_CREATED >= TRUNC( SYSDATE, 'YEAR' ) "
			+ "AND DATE_CREATED < ADD_MONTHS( TRUNC( SYSDATE, 'YEAR' ), 12 ) UNION ALL SELECT DATE_CREATED,"
			+ "ADDED_QUANTITY FROM MM_INWARD_APPROVED_MATERIALS WHERE DATE_CREATED >= TRUNC( SYSDATE, 'YEAR' ) "
			+ "AND DATE_CREATED < ADD_MONTHS( TRUNC( SYSDATE, 'YEAR' ), 12 ) UNION ALL SELECT DATE_CREATED,"
			+ "ADDED_QUANTITY FROM MM_INWARD_APPROVED_TOOLS WHERE DATE_CREATED >= TRUNC( SYSDATE, 'YEAR' ) "
			+ "AND DATE_CREATED < ADD_MONTHS( TRUNC( SYSDATE, 'YEAR' ), 12 )) ALL_TABLES GROUP BY "
			+ "TO_CHAR( DATE_CREATED, 'MON' ),TO_CHAR( DATE_CREATED, 'YYYY' ) ORDER BY "
			+ "TO_DATE( YEAR || MONTH_NAME || '01', 'YYYYMONDD' ) ASC";

	private static final String MONTHWISE_STOCKS = "CREATE OR REPLACE VIEW DASHBOARD_MONTHWISE_STOCKS AS "
			+ "SELECT 'SPARES' AS STOCK_TYPE,TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,"
			+ "EXTRACT( YEAR FROM DATE_CREATED ) AS YEAR,SUM( AVAILABLE_QTY ) AS TOTAL_QUANTITY FROM "
			+ "MM_INWARD_APPROVED_SPARES WHERE DATE_CREATED >= SYSDATE - INTERVAL '1' YEAR GROUP BY "
			+ "EXTRACT( YEAR FROM DATE_CREATED ),TO_CHAR( DATE_CREATED, 'MON' ) UNION SELECT "
			+ "'MATERIALS' AS STOCK_TYPE,TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,"
			+ "EXTRACT( YEAR FROM DATE_CREATED ) AS YEAR,SUM( AVAILABLE_QTY ) AS TOTAL_QUANTITY FROM "
			+ "MM_INWARD_APPROVED_MATERIALS WHERE DATE_CREATED >= SYSDATE - INTERVAL '1' YEAR GROUP BY "
			+ "EXTRACT( YEAR FROM DATE_CREATED ),TO_CHAR( DATE_CREATED, 'MON' ) UNION SELECT "
			+ "'TOOLS' AS STOCK_TYPE,TO_CHAR( DATE_CREATED, 'MON' ) AS MONTH_NAME,"
			+ "EXTRACT( YEAR FROM DATE_CREATED ) AS YEAR,SUM( AVAILABLE_QTY ) AS TOTAL_QUANTITY FROM "
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

	private static final String STOCK_MATERIALS = "CREATE OR REPLACE VIEW DASHBOARD_STOCK_MATERIALS AS SELECT "
			+ "CATEGORY, SUM(AVAILABLE_QTY) AS TOTAL_QUANTITY FROM MM_INWARD_APPROVED_MATERIALS GROUP BY CATEGORY";
	
	private static final String EMPLOYEE_COUNT = "CREATE OR REPLACE VIEW DASHBOARD_DEPTWISE_EMPLOYEE AS SELECT "
			+ "DEPARTMENT, TO_CHAR(DATE_CREATED, 'MON') AS MONTH_NAME, EXTRACT(YEAR FROM DATE_CREATED) AS YEAR, "
			+ "COUNT(*) AS EMP_COUNT FROM MM_EMPLOYEE_MASTER WHERE DATE_CREATED >= SYSDATE - INTERVAL '1' YEAR "
			+ "GROUP BY DEPARTMENT, TO_CHAR(DATE_CREATED, 'MON'), EXTRACT(YEAR FROM DATE_CREATED) "
			+ "ORDER BY YEAR ASC, MONTH_NAME ASC, DEPARTMENT ASC";


	private static final String EMPLOYEE_LEAVE = "CREATE OR REPLACE VIEW DASHBORD_DEPTWISE_EMP_LEAVE AS SELECT "
	        + "DEPARTMENT ,TO_CHAR(CREATE_DATE, 'MON') AS MONTH_NAME, EXTRACT(YEAR FROM CREATE_DATE) AS YEAR,"
			+ "COUNT(*) AS EMP_COUNT FROM MM_EMPLOYEE_LEAVE WHERE HR_APPROVAL='YES'"
	        + "AND CREATE_DATE >= SYSDATE - INTERVAL '1' YEAR GROUP BY DEPARTMENT, TO_CHAR(CREATE_DATE, 'MON'),"
			+ "EXTRACT(YEAR FROM CREATE_DATE) ORDER BY YEAR ASC, MONTH_NAME ASC, DEPARTMENT ASC";
	
	private static final String INDENT_STATUS = "CREATE OR REPLACE VIEW VERIFY_INDENT_STATUS AS "
	        + "SELECT DISTINCT "
	        + "witem.INDENT_NO, "
	        + "witem.COMPL_NO, "
	        + "witem.DEPARTMENT_NAME, "
	        + "witem.APPROVED_STS as item_approved, "
	        + "wlab.APPROVED_STS as labor_approved, "
	        + "wveh.APPROVED_STS as vehicle_approved "
	        + "FROM "
	        + "MM_TEMP_WORKORDER_ITEM_REQUEST witem "
	        + "JOIN MM_TEMP_WORKORDER_LABOUR_REQUEST wlab ON witem.INDENT_NO = wlab.INDENT_NO AND witem.COMPL_NO = wlab.COMPL_NO AND witem.DEPARTMENT_NAME = wlab.DEPARTMENT_NAME "
	        + "JOIN MM_TEMP_WORKORDER_VEHICLE_REQUEST wveh ON witem.INDENT_NO = wveh.INDENT_NO AND witem.COMPL_NO = wveh.COMPL_NO AND witem.DEPARTMENT_NAME = wveh.DEPARTMENT_NAME";

	@PostConstruct
	public void createView() {
		jdbcTemplate.execute(WORKORDER_REQUESTS);
		jdbcTemplate.execute(MONTHWISE_STOCKS);
		jdbcTemplate.execute(MONTHWISE_TOTALSTOCKS);
		jdbcTemplate.execute(OUTWARD_STOCKS);
		jdbcTemplate.execute(STOCKS_RETURN);
		jdbcTemplate.execute(STOCK_MATERIALS);
		jdbcTemplate.execute(EMPLOYEE_COUNT);
		jdbcTemplate.execute(EMPLOYEE_LEAVE);
		jdbcTemplate.execute(INDENT_STATUS);
	}
}
