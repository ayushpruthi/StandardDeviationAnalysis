package com.standarddeviation.excelutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SuiteController {

	private static XSSFWorkbook getWorkBookInstance(String sheetName) throws IOException {
		FileInputStream fi = null;
		XSSFWorkbook workbook;
		try {
			fi = new FileInputStream(new File("input" + File.separator + sheetName));
			workbook = new XSSFWorkbook(fi);

		} finally {
			try {
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return workbook;

	}

	public static List<ExcelData> getExcelData(String sheetName, String tabName, String runFor) throws IOException {
		XSSFWorkbook workbook = getWorkBookInstance(sheetName);
		XSSFSheet sheet = workbook.getSheet(tabName);
		DataFormatter df = new DataFormatter();
		List<ExcelData> data = new ArrayList<>();
		FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		Iterator<Row> itr = sheet.rowIterator();
		while (itr.hasNext()) {
			Row row = itr.next();
			String security = df.formatCellValue(row.getCell(0));
			String url = df.formatCellValue(row.getCell(1));
			if (security != null && !security.isEmpty() && !url.equalsIgnoreCase("URL")) {
				ExcelData excelData = new ExcelData();
				excelData.setSecurity(security);
				excelData.setUrl(url);
				excelData.setCallPut(getCellData(row, 3, "Text", evaluator, df));
				if (runFor.equalsIgnoreCase("Previous")) {
					excelData.setStrikeGap(getCellData(row, 2, "Text", evaluator, df));
					excelData.setDaysToExpiry(getCellData(row, 8, "Number", evaluator, df));
				} else {
					excelData.setStikeNear2SD(getCellData(row, 16, "Text", evaluator, df));
					excelData.setSrtikeNear3SD(getCellData(row, 17, "Text", evaluator, df));
				}
				data.add(excelData);
			}

		}
		workbook.close();
		return data;
	}

	public static void dumpExcelResults(List<ExcelData> excelData, String runFor, String sheetName, String tabName)
			throws IOException {
		Iterator<ExcelData> dataItr = excelData.iterator();
		XSSFWorkbook workbook = getWorkBookInstance(sheetName);
		XSSFSheet sheet = workbook.getSheet(tabName);
		DataFormatter df = new DataFormatter();

		Iterator<Row> itr = sheet.rowIterator();
		while (dataItr.hasNext()) {
			Row row = itr.next();
			String security = df.formatCellValue(row.getCell(0));
			String url = df.formatCellValue(row.getCell(1));
			if (security != null && !security.isEmpty() && !url.equalsIgnoreCase("URL")) {
				ExcelData data = dataItr.next();
				if (runFor.equalsIgnoreCase("Previous")) {
					// System.out.println("Setting result for security: " + data.getSecurity()
					// + ".The price for the security is " + data.getPreviousClose());
					setCellData(row, data.getPreviousClose(), 5);
					setCellData(row, data.getAtmStrike(), 9);
					setCellData(row, data.getPremiumATMStrike(), 10);
					setCellData(row, data.getIV(), 11);
					setCellData(row, data.getSD(), 12);
					setCellData(row, data.getOneSD(), 13);
					setCellData(row, data.getTwoSD(), 14);
					setCellData(row, data.getThreeSD(), 15);
					setCellData(row, data.getStikeNear2SD(), 16);
					setCellData(row, data.getSrtikeNear3SD(), 17);
				} else {
					if (data.getNon3SDStrike() == null) {
						data.setNon3SDStrike("");
					}
					if (data.getPremium2SD() == null) {
						data.setPremium2SD("");
					}
					setCellData(row, data.getSpot(), 4);
					setCellData(row, data.getPremium2SD(), 18);
					setCellData(row, data.getPremium3SD(), 19);
					setCellData(row, data.getNon3SDStrike(), 20);
				}
			}
		}
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(new File("input" + File.separator + sheetName));
			workbook.write(fo);
		} finally {
			workbook.close();
			fo.close();
		}
	}

	private static void setCellData(Row row, String data, int cell) {
		Cell newCell = null;
		if (row.getCell(cell) == null) {
			newCell = row.createCell(cell);
		} else {
			newCell = row.getCell(cell);
		}
		try {
			newCell.setCellValue(Float.parseFloat(data.trim()));
		} catch (NumberFormatException e) {
			newCell.setCellValue(data);
		}
	}

	private static String getCellData(Row row, int cell, String type, FormulaEvaluator evaluator, DataFormatter df) {
		if (row.getCell(cell) == null) {
			if (type.equalsIgnoreCase("Number")) {
				return new Double("-1.00").toString();
			} else {
				return "NA";
			}
		} else {
			if (type.equalsIgnoreCase("Number")) {
				return new Double(evaluator.evaluate(row.getCell(cell)).getNumberValue()).toString();
			} else {
				return df.formatCellValue(row.getCell(cell));
			}
		}
	}
}
