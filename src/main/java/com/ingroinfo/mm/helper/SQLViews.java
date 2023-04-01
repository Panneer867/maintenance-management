package com.ingroinfo.mm.helper;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SQLViews {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String STOCKORDER_REQUESTS = "CREATE OR REPLACE VIEW STOCKORDER_REQUESTS AS "
			+ "SELECT DISTINCT COMPL_DTLS, COMPL_NO, CONTACT_NO,DEPARTMENT_NAME,DIVISION,SUB_DIVISION,START_DATE,END_DATE,"
			+ "INDENT_NO,USERNAME,STOCK_ORDER_NO,WORK_PRIORITY,WORK_SITE FROM MM_STOCKORDER_ITEMS_REQUEST";

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
			+ "MM_APPROVED_STOCKORDER_ITEMS WHERE "
			+ "EXTRACT( YEAR FROM DATE_CREATED ) = EXTRACT( YEAR FROM SYSDATE )  GROUP BY "
			+ "UPPER( TO_CHAR( DATE_CREATED, 'MON' ))  ORDER BY TO_DATE( MONTH_NAME, 'MON' ) ";

	private static final String STOCKS_RETURN = "CREATE OR REPLACE VIEW DASHBOARD_STOCKS_RETURN AS SELECT "
			+ "CASE WHEN STOCK_TYPE = 'ML' THEN 'MATERIALS' WHEN STOCK_TYPE = 'TE' THEN 'TOOLS' "
			+ "WHEN STOCK_TYPE = 'SP' THEN 'SPARES' ELSE STOCK_TYPE END AS STOCK_TYPE, "
			+ "SUM(RETURN_QUANTITY) AS TOTAL_QUANTITY FROM MM_APPROVED_STOCKS_RETURN GROUP BY STOCK_TYPE";

	private static final String STOCK_MATERIALS = "CREATE OR REPLACE VIEW DASHBOARD_STOCK_MATERIALS AS SELECT "
			+ "CATEGORY, SUM(AVAILABLE_QTY) AS TOTAL_QUANTITY FROM MM_INWARD_APPROVED_MATERIALS GROUP BY CATEGORY";

	private static final String STOCK_SPARES = "CREATE OR REPLACE VIEW DASHBOARD_STOCK_SPARES AS SELECT "
			+ "CATEGORY, SUM(AVAILABLE_QTY) AS TOTAL_QUANTITY FROM MM_INWARD_APPROVED_SPARES GROUP BY CATEGORY";

	private static final String STOCK_TOOLS = "CREATE OR REPLACE VIEW DASHBOARD_STOCK_TOOLS AS SELECT "
			+ "CATEGORY, SUM(AVAILABLE_QTY) AS TOTAL_QUANTITY FROM MM_INWARD_APPROVED_TOOLS GROUP BY CATEGORY";

	private static final String STOCK_OUTWARDS = "CREATE OR REPLACE VIEW DASHBOARD_STOCK_OUTWARDS AS SELECT "
			+ "CATEGORY, SUM(FINAL_QUANTITY) AS TOTAL_QUANTITY FROM MM_APPROVED_STOCKORDER_ITEMS GROUP BY CATEGORY";

	private static final String STOCK_RETURNS = "CREATE OR REPLACE VIEW DASHBOARD_STOCK_RETURNS AS SELECT "
			+ "CATEGORY, SUM(RETURN_QUANTITY) AS TOTAL_QUANTITY FROM MM_APPROVED_STOCKS_RETURN GROUP BY CATEGORY";

	private static final String EMPLOYEE_COUNT = "CREATE OR REPLACE VIEW DASHBOARD_DEPTWISE_EMPLOYEE AS SELECT "
			+ "DEPARTMENT, EXTRACT(YEAR FROM DATE_CREATED) AS YEAR," + "COUNT(*) AS EMP_COUNT FROM MM_EMPLOYEE_MASTER "
			+ "WHERE EXTRACT(YEAR FROM DATE_CREATED) = EXTRACT(YEAR FROM CURRENT_DATE) GROUP BY DEPARTMENT, "
			+ "EXTRACT(YEAR FROM DATE_CREATED) ORDER BY YEAR ASC, DEPARTMENT ASC";

	private static final String EMPLOYEE_LEAVE = "CREATE OR REPLACE VIEW DASHBORD_DEPTWISE_EMP_LEAVE AS SELECT "
			+ "DEPARTMENT ,TO_CHAR(CREATE_DATE, 'MON') AS MONTH_NAME, EXTRACT(YEAR FROM CREATE_DATE) AS YEAR,"
			+ "COUNT(*) AS EMP_COUNT FROM MM_EMPLOYEE_LEAVE WHERE HR_APPROVAL='YES'"
			+ "AND CREATE_DATE >= SYSDATE - INTERVAL '1' YEAR GROUP BY DEPARTMENT, TO_CHAR(CREATE_DATE, 'MON'),"
			+ "EXTRACT(YEAR FROM CREATE_DATE) ORDER BY YEAR ASC, MONTH_NAME ASC, DEPARTMENT ASC";

	private static final String INDENT_STATUS = "CREATE OR REPLACE VIEW VERIFY_INDENT_STATUS AS " + "SELECT DISTINCT "
			+ "COALESCE(a.COMPL_NO, b.COMPL_NO, c.COMPL_NO) AS COMPL_NO, "
			+ "COALESCE(a.INDENT_NO, b.INDENT_NO, c.INDENT_NO) AS INDENT_NO, "
			+ "COALESCE(a.DEPARTMENT_NAME, b.DEPARTMENT_NAME, c.DEPARTMENT_NAME) AS DEPARTMENT_NAME " + "FROM "
			+ "MM_INDENT_APPROVED_ITEMS a "
			+ "FULL OUTER JOIN MM_INDENT_APPROVED_VEHICLES b ON a.COMPL_NO = b.COMPL_NO AND a.INDENT_NO = b.INDENT_NO "
			+ "FULL OUTER JOIN MM_INDENT_APPROVED_LABOURS c ON a.COMPL_NO = c.COMPL_NO AND a.INDENT_NO = c.INDENT_NO "
			+ "WHERE " + "COALESCE(a.APPROVED_STS, 'Y') = 'Y' AND " + "COALESCE(b.APPROVED_STS, 'Y') = 'Y' AND "
			+ "COALESCE(c.APPROVED_STS, 'Y') = 'Y'";

	private static final String ASSETS_COUNT = "CREATE OR REPLACE VIEW DASHBORD_ASSETS AS SELECT "
			+ "DEPARTMENT AS DEPARTMENTS, SUM(QUANTITY) AS TOTAL_QUANTITY  FROM MM_ASSETS "
			+ "GROUP BY DEPARTMENT ORDER BY TOTAL_QUANTITY";
	
	private static final String GENERATE_WORKORDERS_COUNT = "CREATE OR REPLACE VIEW DASHBORD_GENERATE_WORKORDERS AS SELECT "
			+ "COMPL_NO, COMPL_NO.DEPARTMENT_NAME AS DEPARTMENTS, COUNT(*) AS COUNT FROM (SELECT DISTINCT COMPL_NO, "
			+ "DEPARTMENT_NAME FROM MM_WAP_WORKORDER_ITEMS UNION SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM "
			+ "MM_WAP_WORKORDER_LABOURS UNION SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM MM_WAP_WORKORDER_VEHICLES) "
			+ "COMPL_NO GROUP BY COMPL_NO, COMPL_NO.DEPARTMENT_NAME";

	private static final String APPROVED_WORKORDERS_COUNT = "CREATE OR REPLACE VIEW DASHBORD_APPROVED_WORKORDERS AS SELECT "
			+"COMPL_NO, COMPL_NO.DEPARTMENT_NAME AS DEPARTMENTS, COUNT(*) AS COUNT FROM (SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME "
			+ "FROM MM_WORKORDER_APPROVED_ITEMS UNION SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM MM_WORKORDER_APPROVED_LABOURS UNION "
			+ "SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM MM_WORKORDER_APPROVED_VEHICLES) COMPL_NO GROUP BY COMPL_NO, COMPL_NO.DEPARTMENT_NAME";
	
	private static final String HOLD_WORKORDERS_COUNT = "CREATE OR REPLACE VIEW DASHBORD_HOLD_WORKORDERS AS SELECT "
			+"COMPL_NO, COMPL_NO.DEPARTMENT_NAME AS DEPARTMENTS, COUNT(*) AS COUNT FROM (SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME "
			+ "FROM MM_HOLD_WORKORDER_ITEMS UNION SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM MM_HOLD_WORKORDER_LABOURS UNION "
			+ "SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM MM_HOLD_WORKORDER_VEHICLES) COMPL_NO GROUP BY COMPL_NO, COMPL_NO.DEPARTMENT_NAME";
	
	private static final String CANCEL_WORKORDERS_COUNT = "CREATE OR REPLACE VIEW DASHBORD_CANCEL_WORKORDERS AS SELECT "
			+"COMPL_NO, COMPL_NO.DEPARTMENT_NAME AS DEPARTMENTS, COUNT(*) AS COUNT FROM (SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME "
			+ "FROM MM_CANCEL_WORKORDER_ITEMS UNION SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM MM_CANCEL_WORKORDER_LABOURS UNION "
			+ "SELECT DISTINCT COMPL_NO, DEPARTMENT_NAME FROM MM_CANCEL_WORKORDER_VEHICLES) COMPL_NO GROUP BY COMPL_NO, COMPL_NO.DEPARTMENT_NAME";
	
	
	
	@PostConstruct
	public void createView() {
		
		jdbcTemplate.execute(GENERATE_WORKORDERS_COUNT);
		jdbcTemplate.execute(APPROVED_WORKORDERS_COUNT);
		jdbcTemplate.execute(CANCEL_WORKORDERS_COUNT);
		jdbcTemplate.execute(HOLD_WORKORDERS_COUNT);
		jdbcTemplate.execute(MONTHWISE_TOTALSTOCKS);
		jdbcTemplate.execute(STOCKORDER_REQUESTS);
		jdbcTemplate.execute(MONTHWISE_STOCKS);		
		jdbcTemplate.execute(STOCK_MATERIALS);
		jdbcTemplate.execute(OUTWARD_STOCKS);
		jdbcTemplate.execute(STOCK_OUTWARDS);
		jdbcTemplate.execute(EMPLOYEE_COUNT);
		jdbcTemplate.execute(EMPLOYEE_LEAVE);
		jdbcTemplate.execute(STOCK_RETURNS);
		jdbcTemplate.execute(STOCKS_RETURN);
		jdbcTemplate.execute(INDENT_STATUS);
		jdbcTemplate.execute(ASSETS_COUNT);
		jdbcTemplate.execute(STOCK_SPARES);		
		jdbcTemplate.execute(STOCK_TOOLS);
		
	}
}
