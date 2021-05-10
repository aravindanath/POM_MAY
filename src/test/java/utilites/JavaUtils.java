package utilites;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.testng.Reporter;
import org.testng.SkipException;

import java.io.*;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;


public class JavaUtils {

	public Set<String> returnAllNames(String sheetName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		Set<String> all = new HashSet<String>();

		FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetName);
		Iterator<Row> it = sheet.rowIterator();

		Row headers = it.next();
		while (it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(2).toString();
			all.add(cellValue);
		}
		return all;
	}

	public Set<String> returnAllBulkValues(String sheetName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		Set<String> all = new HashSet<String>();

		FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetName);
		Iterator<Row> it = sheet.rowIterator();

		Row headers = it.next();
		while (it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(0).toString();
			all.add(cellValue);
		}
		return all;
	}

	public String getPropertyOf(String key) {

		String propValue = null;
		try {
			FileInputStream input = new FileInputStream("./config.properties");
			Properties prop = new Properties();
			prop.load(input);
			propValue = prop.getProperty(key);

		} catch (IOException e) {
			throw new NullPointerException("Unable to get the property of : " + key);
		}
		return propValue;
	}

	public static String getPropValue(String key) throws IOException {

		FileReader reader = new FileReader("./config.properties");
		Properties prop = new Properties();
		prop.load(reader);
		String propValue = prop.getProperty(key);
		System.out.println("Property Value for " + key + " is " + propValue);
		return propValue;

	}

	public void writeTestResultsToExcel(String outputFile, String SheetName, List<String> data, boolean overwriteCells)
			throws InvalidFormatException, IOException {
		FileInputStream fis = new FileInputStream(outputFile);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet sheet = wb.getSheet(SheetName);
		Iterator<String> itr = data.iterator();
		if (overwriteCells == true) {
			int idx = 0;
			XSSFRow r = sheet.createRow(1);
			while (itr.hasNext()) {
				Cell c = r.createCell(idx);
				c.setCellType(c.CELL_TYPE_STRING);
				c.setCellValue(itr.next());
				idx += 1;
			}
		} else {
			int rowNo = sheet.getLastRowNum() + 1;
			Row r = sheet.createRow(rowNo);
			int idx = 0;
			while (itr.hasNext()) {
				Cell c = r.createCell(idx);
				c.setCellType(c.CELL_TYPE_STRING);
				c.setCellValue(itr.next());
				idx += 1;
			}
		}

		FileOutputStream fos = new FileOutputStream(outputFile);
		wb.write(fos);
		fos.close();
	}

	public void writeResultToExcel(String testCaseName, String result) throws InvalidFormatException, IOException {
		try {
			FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet("TestExecution");
			Iterator<Row> it = sheet.rowIterator();
			Row headers = it.next();
			while (it.hasNext()) {
				Row record = it.next();
				String cellValue = record.getCell(0).toString();
				if (cellValue.equalsIgnoreCase(testCaseName)) {

					// update cell if active
					for (int i = 0; i < record.getLastCellNum(); i++) {
						if (headers.getCell(i).equals("EXECUTIONRESULT")) {
							try {
								record.getCell(i).setCellValue(result);
							} catch (Exception e1) {
								// create cell and update if inactive
								record.createCell(3).setCellValue(result);
							}
							break;
						}
					}

					break;
				}
			}
			FileOutputStream fos = new FileOutputStream("./test-data/TestData.xlsx");
			wb.write(fos);
			fos.close();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	public String getPropValues(String[] key) throws IOException {

		FileReader reader = new FileReader("./GramaVidiyal/config.properties");
		Properties prop = new Properties();
		prop.load(reader);

		String propValue = prop.getProperty(key[0]);
		return propValue;

	}

	public void writeExecutionStatusToExcelscreenShot(String SheetName, String[] ExecutionDetails)
			throws InvalidFormatException, IOException {

		FileInputStream fileIn = new FileInputStream(new File("./reports/TestReport.xlsx"));
		Workbook workbook = WorkbookFactory.create(fileIn);
		CreationHelper createHelper = workbook.getCreationHelper();
		CellStyle hlinkstyle = workbook.createCellStyle();
	//	XSSFHyperlink link = (XSSFHyperlink) createHelper.createHyperlink(Hyperlink.LINK_FILE);
		Sheet worksheet = workbook.getSheet(SheetName);

		Iterator<Row> it = worksheet.rowIterator();

		Row headers = it.next();
		int rowToUpdate = worksheet.getLastRowNum() + 1;
		loop: while (it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(3).toString().trim();
			if (cellValue.equalsIgnoreCase(ExecutionDetails[3])) {
				rowToUpdate = record.getRowNum();
				break loop;
			}
		}

		Row record = worksheet.createRow(rowToUpdate);
		for (int i = 0; i < ExecutionDetails.length; i++) {
			Cell cellNum = record.createCell(i);
			cellNum.setCellValue(ExecutionDetails[i]);
			if (i != 6) {
				cellNum.setCellValue(ExecutionDetails[i]);
			} else if (i == 7) {
				cellNum.setCellValue("Screenshot");
//				link.setAddress("file:///D://reports//FailedScreenShots//screenshots//" + ExecutionDetails[i]);
//				cellNum.setHyperlink(link);
				cellNum.setCellStyle(hlinkstyle);

				/*
				 * if (i==4) { CellStyle style =
				 * record.getCell(4).getCellStyle(); if
				 * (ExecutionDetails[i].equalsIgnoreCase("PASS")) { ((CellStyle)
				 * style).setFillBackgroundColor(IndexedColors.GREEN.getIndex())
				 * ; } else if (ExecutionDetails[i].equalsIgnoreCase("FAIL")) {
				 * ((CellStyle)
				 * style).setFillBackgroundColor(IndexedColors.RED.getIndex());
				 * } }
				 */
			}

			FileOutputStream fileOut = new FileOutputStream(new File("./Reports/TestReport.xlsx"));
			workbook.write(fileOut);
			fileOut.close();
		}
	}

	public void writeExecutionStatusToExcel(String SheetName, String[] ExecutionDetails)
			throws InvalidFormatException, IOException {

		FileInputStream fileIn = new FileInputStream(new File("./reports/TestReport.xlsx"));
		Workbook workbook = WorkbookFactory.create(fileIn);
		CellStyle my_style = workbook.createCellStyle();
		Font style = workbook.createFont();

		Sheet worksheet = workbook.getSheet(SheetName);

		// style.setColor(Font.COLOR_RED);
		// my_style.setFont(style);
		Iterator<Row> it = worksheet.rowIterator();

		Row headers = it.next();
		int rowToUpdate = worksheet.getLastRowNum() + 1;
		// loop: while (it.hasNext()) {
		//
		// Row record = it.next();
		// String cellValue = record.getCell(3).toString().trim();
		// if (cellValue.equalsIgnoreCase(ExecutionDetails[3])) {
		// rowToUpdate = record.getRowNum();
		// break loop;
		// }
		// }

		Row record = worksheet.createRow(rowToUpdate);
		for (int i = 0; i < ExecutionDetails.length; i++) {
			Cell cellNum = record.createCell(i);
			cellNum.setCellValue(ExecutionDetails[i]);
			cellNum.setCellStyle(my_style);

		}

		FileOutputStream fileOut = new FileOutputStream(new File("./Reports/TestReport.xlsx"));
		workbook.write(fileOut);
		fileOut.close();
	}

	public String checkExecutionStatus(String sheetName, String testCaseId) {

		HashMap<String, String> testRow = readExcelData(sheetName, testCaseId);
		String testDesc = testRow.get("Test Description");

		/*
		 * Checks the execution status of the current testCaseID which is set in
		 * the Excel - TestData sheet if marked 'Yes' testCase would execute ,
		 * else testCase would skip
		 */
		if (testRow.get("Execution Status").toLowerCase().equalsIgnoreCase("no")) {
			throw new SkipException(
					"Skipping the test flow with ID " + testCaseId + " as it marked 'NO' in the Execution Excel Sheet");
		}

		Reporter.log("\nExecuting the " + testRow.get("Test Description") + " : " + testCaseId, true);
		return testCaseId;
	}

	public void checkExecutionStatus(String testScenario) {

		HashMap<String, String> testRow = readExcelData("TESTEXECUTION", testScenario);
		String testDesc = testRow.get("TESTDESCRIPTION");
		String testExecStatus = testRow.get("RUNMODE");
		/*
		 * Checks the execution status of the current testCaseID which is set in
		 * the Excel - TestData sheet if marked 'Yes' testCase would execute ,
		 * else testCase would skip
		 */
		if (testExecStatus.toUpperCase().equalsIgnoreCase("N")) {
			throw new SkipException("Skipping the test flow " + testScenario + " as it marked 'N' for Execution..!");
		}

		Reporter.log("\nExecuting the workflow for " + testDesc + " : " + testScenario, true);
	}

	public boolean checkForMethodExecution(String testFlow, String methodName) {

		if (testFlow.toUpperCase().equals("SKIP")) {
			throw new SkipException("Skipping " + methodName + " as mentioned in the Execution Sheet");
		} else {
			// Reporter.log("Exectuing the method "+methodName);
			return true;
		}
	}

	public static HashMap<String, String> readExcelData(String sheetname, String uniqueValue) {
		try {
			String key, value;
			FileInputStream file = new FileInputStream("./test-data/TestDataFile.xls");
			HashMap<String, String> dataMap = new HashMap<String, String>();
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetname);
			Iterator<Row> it = sheet.rowIterator();

			Row headers = it.next();
			while (it.hasNext()) {

				Row record = it.next();
				String cellValue = record.getCell(0).toString().trim();
				if (cellValue.equalsIgnoreCase(uniqueValue)) {

					for (int i = 0; i < record.getLastCellNum(); i++) {

						if (record.getCell(i).getCellType() == record.getCell(i).CELL_TYPE_NUMERIC) {
							if (HSSFDateUtil.isCellDateFormatted(record.getCell(i))) {

								// DateFormat dateFormat = new
								// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
								value = dateFormat.format(record.getCell(i).getDateCellValue()).trim();

							} else {
								record.getCell(i).setCellType(Cell.CELL_TYPE_STRING);

								value = record.getCell(i).toString().trim();
							}
							key = headers.getCell(i).toString().trim();

						} else {

							key = headers.getCell(i).toString().trim();
							value = record.getCell(i).toString().trim();
						}
						dataMap.put(key, value);

					}
					break;
				}
			}
			return dataMap;
		} catch (NullPointerException e) {
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (InvalidFormatException e) {
			throw new NullPointerException("Failed due to InvalidFormatException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}
	}

	public HashMap<String, String> readExcelDataWithDifferentWb(String workbook, String sheetname, String uniqueValue) {
		try {
			String key, value;
			FileInputStream file = new FileInputStream("./test-data/" + workbook + ".xlsx");
			HashMap<String, String> dataMap = new HashMap<String, String>();
			Workbook wb = WorkbookFactory.create(file);
			Sheet sheet = wb.getSheet(sheetname);
			Iterator<Row> it = sheet.rowIterator();

			Row headers = it.next();
			while (it.hasNext()) {

				Row record = it.next();
				String cellValue = record.getCell(0).toString().trim();
				if (cellValue.equalsIgnoreCase(uniqueValue)) {

					for (int i = 0; i < record.getLastCellNum(); i++) {

						if (record.getCell(i).getCellType() == record.getCell(i).CELL_TYPE_NUMERIC) {
							if (HSSFDateUtil.isCellDateFormatted(record.getCell(i))) {

								// DateFormat dateFormat = new
								// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
								value = dateFormat.format(record.getCell(i).getDateCellValue()).trim();

							} else {
								record.getCell(i).setCellType(Cell.CELL_TYPE_STRING);

								value = record.getCell(i).toString().trim();
							}
							key = headers.getCell(i).toString().trim();

						} else {

							key = headers.getCell(i).toString().trim();
							value = record.getCell(i).toString().trim();
						}
						dataMap.put(key, value);

					}
					break;
				}
			}
			return dataMap;
		} catch (NullPointerException e) {
			throw new NullPointerException("Failed due to NullPointerException" + e);
		} catch (EncryptedDocumentException e) {
			throw new EncryptedDocumentException("Failed due to EncryptedDocumentException" + e);
		} catch (InvalidFormatException e) {
			throw new NullPointerException("Failed due to InvalidFormatException" + e);
		} catch (IOException e) {
			throw new NullPointerException("Failed due to IOException" + e);
		}
	}

	public static void writeValueToExcel(String sheetname, String uniqueValue, String columnName, String value)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		FileInputStream file = new FileInputStream("./test-data/testData.xlsx");
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetname);
		Iterator<Row> it = sheet.rowIterator();

		Row headers = it.next();
		while (it.hasNext()) {

			Row record = it.next();
			String cellValue = record.getCell(0).toString().trim();
			if (cellValue.equalsIgnoreCase(uniqueValue)) {
				for (int i = 0; i < headers.getLastCellNum(); i++) {
					if (headers.getCell(i).getStringCellValue().equals(columnName)) {
						try {
							record.getCell(i).setCellValue(value);
						} catch (Exception e) {
							record.createCell(i).setCellValue(columnName);
						}
						break;
					}
				}
				break;
			}
		}
		FileOutputStream fos = new FileOutputStream("./test-data/testData.xlsx");
		wb.write(fos);
		wb.close();
		fos.close();
	}

	public static String getExecutionResultStatus(int statusCode) {

		String testStatus = null;
		if (statusCode == 1) {
			testStatus = "PASS";
		} else if (statusCode == 2) {
			testStatus = "FAIL";
		} else if (statusCode == 3) {
			testStatus = "SKIP";
		}

		return testStatus;
	}

	public List<String> returnAllWorkFlows(String sheetName)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		List<String> allWorkflows = new ArrayList<String>();

		FileInputStream file = new FileInputStream("./test-data/TestData.xlsx");
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetName);
		Iterator<Row> it = sheet.rowIterator();
		it.next();
		while (it.hasNext()) {

			Row record = it.next();
			// Runmode
			if (record.getCell(2).toString().equalsIgnoreCase("Y")) {
				String cellValue = record.getCell(0).toString();
				allWorkflows.add(cellValue);
			}
		}
		return allWorkflows;
	}

	/*
	 * Returns the ArrayList to Two-Dimensional Object array for dataProvider
	 * Iteration
	 */
	public Object[][] returnAllUniqueValues(String sheetName) {

		List<String> listValues = null;
		try {
			listValues = returnAllWorkFlows(sheetName);
		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!listValues.equals(null)) {
			Object[][] allValues = new Object[listValues.size()][1];
			for (int i = 0; i < listValues.size(); i++) {
				allValues[i][0] = listValues.get(i);
			}
			return allValues;
		}

		Reporter.log("ERROR..! Unable to fetch workflows from the Excel.", true);
		return null;
	}

	public String checkExecutionStatusForSkip(String testCaseId) {

		if (testCaseId.equalsIgnoreCase("SKIP")) {
			throw new SkipException("Skipping the test flow with ID " + testCaseId
					+ " as it marked 'SKIP' in the Execution Excel Sheet");

		}
		Reporter.log("\nExecuting the " + testCaseId, true);
		return testCaseId;

	}

	public String setTestCaseId(String sheetName, String id, String uniqueValue)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		HashMap<String, String> value = readExcelData(sheetName, id);
		return value.get(uniqueValue);
	}

	public HashMap<String, int[]> consolidatedReport()
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		String sheetname = getPropValue("reportSheetName");
		FileInputStream file = new FileInputStream(getPropValue("testReport"));
		HashMap<String, int[]> executionResult = new HashMap<String, int[]>();
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheet(sheetname);
		Iterator<Row> it = sheet.rowIterator();
		Row headers = it.next();
		while (it.hasNext()) {
			Row record = it.next();
			String api = record.getCell(1).toString();
			String result = record.getCell(4).toString();
			if (null != executionResult.get(api)) {
				if (result.equalsIgnoreCase("PASS")) {
					++executionResult.get(api)[0];
				} else if (result.equalsIgnoreCase("FAIL")) {
					++executionResult.get(api)[1];
				}
				++executionResult.get(api)[2];
			} else {
				if (result.equalsIgnoreCase("PASS")) {
					executionResult.put(api, new int[] { 1, 0, 1 });
				} else if (result.equalsIgnoreCase("FAIL")) {
					executionResult.put(api, new int[] { 0, 1, 1 });
				}

			}

		}

		/*
		 * for(Map.Entry<String, int[]> value : executionResult.entrySet()){
		 * String key = value.getKey(); int [] arr = value.getValue();
		 * System.out.println(key+"  " +Arrays.toString(arr)); }
		 */

		return executionResult;
	}

	public String report() throws EncryptedDocumentException, InvalidFormatException, IOException {
		StringBuilder form = new StringBuilder();
		HashMap<String, int[]> result = consolidatedReport();
		form.append(
				"<html>" + "<table style='border-spacing: 0px; padding:5px; font-family: monospace; font-size: 1em;'>"
						+ "<tr style='background-color:#ff7f00;font-weight: bold;font-family: monospace;font-size: 1.1em;'> "
						+ "<td style='border:1px solid;padding:5px'>DATE OF EXECUTION</td>"
						+ "<td style='border:1px solid;padding:5px'>TEST STEP</td>"
						+ "<td style='border:1px solid;padding:5px'>BUILD</td>"
						+ "<td style='border:1px solid;padding:5px'>TOTAL TEST EXECUTED WORK FLOWS</td>"
						+ "<td style='border:1px solid;padding:5px'>TOTAL PASSED</td>"
						+ "<td style='border:1px solid;padding:5px'>TOTAL FAILED</td>" + "</tr>");
		for (Map.Entry<String, int[]> data : result.entrySet()) {
			form.append("<tr style='font-family: monospace;font-size: 1em'>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + getTodaysDate("dd-MM-yyyy")
					+ "</td>" + "<td style='border:1px solid;padding:5px'>" + data.getKey() + "</td>"
					// + "<td style='border:1px solid;text-align:
					// center;padding:5px'>" + getPropValue("buildNumber")
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + getBuildVersion() + "</td>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + data.getValue()[2] + "</td>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + data.getValue()[0] + "</td>"
					+ "<td style='border:1px solid;text-align: center;padding:5px'>" + data.getValue()[1] + "</td>"
					+ "</tr>");
		}
		form.append("</table></html>");
		return form.toString();
	}

	public String getTodaysDate(String format) {

		Format formatter = new SimpleDateFormat(format);
		String todaysDate = formatter.format(new Date());
		return todaysDate;
	}

	// write test result to execution file

	public static void writeExecutionStatusToExcel(String[] APIExecutionDetails)
			throws InvalidFormatException, IOException {

		try {
			int rowToUpdate = 0;
			File file = new File(getPropValue("testReport"));
			if (!(file.exists())) {
				file.createNewFile();
				Workbook workbook = new HSSFWorkbook();
				Sheet worksheet = workbook.createSheet(getPropValue("reportSheetName"));
				Row headers = worksheet.createRow(0);

				headers.createCell(0).setCellValue("BUILD NUMBER");
				headers.createCell(1).setCellValue("TEST STEP");
				headers.createCell(2).setCellValue("TCID");
				headers.createCell(3).setCellValue("TEST DESCRIPTION");
				headers.createCell(4).setCellValue("RESULT");
				headers.createCell(5).setCellValue("REASON OF FAILURE");
				FileOutputStream fileOut = new FileOutputStream(file);
				workbook.write(fileOut);
				workbook.close();
				fileOut.close();
			}
			FileInputStream fileIn = new FileInputStream(file);
			Workbook workbook = WorkbookFactory.create(fileIn);
			Sheet worksheet = workbook.getSheet(getPropValue("reportSheetName"));
			rowToUpdate = worksheet.getLastRowNum() + 1;
			int i;
			Row record = worksheet.createRow(rowToUpdate);
			Cell cell = null;
			for (i = 0; i < APIExecutionDetails.length; i++) {
				cell = record.createCell(i);
				cell.setCellValue(APIExecutionDetails[i]);
			}

			FileOutputStream fileOut = new FileOutputStream(new File(getPropValue("testReport")));
			workbook.write(fileOut);
			workbook.close();
			fileOut.close();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}

	public void setBuildVersion(String applicantLeadID) throws IOException {
		Reporter.log("writing the  Build Versionn details to a .ini file", true);

		Ini ini = new Ini(new File("./test-data/test-data.ini"));
		ini.put("BuildVersion", "BUILD", applicantLeadID);
		ini.store();
	}

	public static String getBuildVersion() throws InvalidFileFormatException, IOException {
		Reporter.log("reading the build version details from a .ini file", true);
		Ini ini = new Ini(new File("./test-data/test-data.ini"));
		String BUILD = ini.get("BuildVersion", "BUILD");
		System.err.println("BUILD Version-->" + BUILD);
		return BUILD;
	}

}
