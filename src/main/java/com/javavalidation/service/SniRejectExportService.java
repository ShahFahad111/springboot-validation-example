package com.javavalidation.service;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.javavalidation.entity.MessageTransitionData;
import com.javavalidation.repository.MessageTransitionRepo;
import com.javavalidation.util.DateUtil;

import au.com.bytecode.opencsv.CSVWriter;
import net.lingala.zip4j.ZipFile;

@Service
public class SniRejectExportService {

	private static final String fileHeader = "STATUS_OF_MESSAGE;RECEIVER_BIC;SENDER_BIC;MSGTYPE;ACCT_NUM;CCY;STATEMENT_NUM;SEQ_NUM;IS_FINAL_SEQ_NUM;LENGTH_MSG;RECEIVED_DATE;PERIMETER";

	@Value("${report.path}")
	private String reportFolderPath;
	
	@Value("${report.backup}")
	private String backupFolder;
	
	@Value("${report.backup-local-file:true}")
	private boolean backupSentLocalFile;

	private static final String reportFileName = "SNI_Reject_Report";

	@Autowired
	private MessageTransitionRepo repo;

	public void exportSniReject() {
		boolean isError = false;
		try {
		List<MessageTransitionData> list = fetchMessageTransitionData();
		//System.out.println(list);
		for (MessageTransitionData l : list) {
			//System.out.println(l);
		}
		
		
		String string = generateCsvFile_1(list);
		
		createZipFile(string);
		
		System.out.println("File exported with absolute path " + string);
		}catch(Exception e) {
			isError = true;
			e.printStackTrace();
		}finally {
			FileUtils.listFiles(new File(reportFolderPath), new String[] {"csv"}, true).forEach(f -> FileUtils.deleteQuietly(f));
			
			if((false == isError) && backupSentLocalFile) {
				FileUtils.listFiles(new File(reportFolderPath), new String[] {"zip"}, true).forEach(f -> {
					try {
						if(StringUtils.isBlank(backupFolder)) {
							backupFolder = System.getProperty("java.io.tmpdir");
						}
						FileUtils.moveFileToDirectory(f, new File(backupFolder), false);
					}catch(IOException e) {
						e.printStackTrace();
					}
				});
			}else {
				FileUtils.listFiles(new File(reportFolderPath), new String[] {"zip"}, true).forEach(f -> FileUtils.deleteQuietly(f));
			}
		}
	}

	private void createZipFile(String string)  {
		//Input : CSV File name with Path :  	E:\\Java\\report\SNI_Reject_Report_2023-01-23.csv
		
		// csvFileName : SNI_Reject_Report_2023-01-23.csv
		String csvFileName = new File(string).getName();
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return csvFileName.equals(name);
			}
			
		};
		
		File localDir = new File(reportFolderPath);
		
		File[] listFiles = localDir.listFiles(filter);
		
		if(null == listFiles || listFiles.length == 0) {
			System.out.println("No File to zip");
			return;
		}
		
		// zipFileName SNI_Reject_Report_2023-01-23.csv.zip
		String zipFileName = new StringBuilder(csvFileName).append(".zip").toString();
		try {
			Files.deleteIfExists(new File(reportFolderPath + File.separator + zipFileName).toPath());
			ZipFile zipFile = new ZipFile(new File(reportFolderPath + File.separator + zipFileName));
			zipFile.addFile(listFiles[0]);	
			zipFile.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	
		/*Files.deleteIfExists(new File(reportFolderPath + File.separator + zipFileName).toPath());
		
		try(ZipFile zipFile = new ZipFile(new File(reportFolderPath + File.separator + zipFileName))){
			zipFile.addFile(listFiles[0]);
		}*/
		
	}

	private String generateCsvFile_1(List<MessageTransitionData> dataToBeExported) {
		String fileName = new StringBuilder().append(reportFileName).append("_")
				.append(DateUtil.getCurrentDayDate("yyyy-MM-dd")).toString();
		return createCsvFile(dataToBeExported, fileName);
	}

	private String createCsvFile(List<MessageTransitionData> dataToBeExported, String fileName) {
		Map<String, Map<String, List<MessageTransitionData>>> listToMap = convertListToMap(dataToBeExported);
		return createCsvFile(listToMap, reportFolderPath, fileName);
	}

	private Map<String, Map<String, List<MessageTransitionData>>> convertListToMap(List<MessageTransitionData> list) {
		return list.stream().collect(Collectors.groupingBy(MessageTransitionData::getAccount,
				Collectors.groupingBy(MessageTransitionData::getStatementNumber)));
	}
	
	private String createCsvFile(Map<String, Map<String, List<MessageTransitionData>>> listToMap,
			String reportFolderPath, String fileName) {
		List<List<String>> data = convertMessageTransitionToString(listToMap);
		String fileNameWithPath = "";
		try {
			fileNameWithPath = createCsvFile(data, reportFolderPath, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileNameWithPath;
	}

	private String createCsvFile(List<List<String>> dataToExport, String reportFolderPath, String reportfileName)
			throws IOException {
		String csvReportFileNameWithPath = new StringBuilder(reportFolderPath).append(File.separator)
				.append(reportfileName).append(".csv").toString();

		File localFile = new File(csvReportFileNameWithPath);
		Files.deleteIfExists(localFile.toPath());
		Files.createFile(localFile.toPath());
		System.out.println("File " + csvReportFileNameWithPath + " created");

		try (FileWriter fileWriter = new FileWriter(localFile);
				CSVWriter writer = new CSVWriter(fileWriter, ',', CSVWriter.NO_QUOTE_CHARACTER,
						CSVWriter.NO_ESCAPE_CHARACTER);) {
			String[] headers = fileHeader.split(";");

			writer.writeNext(headers);

			dataToExport.forEach(data -> {
				String[] arr = data.stream().toArray(String[]::new);
				for (int i = 0; i < arr.length; i++) {
					if (null == arr[i])
						arr[i] = "\"\"";
					else
						arr[i] = new StringBuilder().append("\"").append(arr[i]).append("\"").toString();
				}
				writer.writeNext(arr);
			});
			fileWriter.flush();
		}

		return csvReportFileNameWithPath;
	}

	private List<MessageTransitionData> fetchMessageTransitionData() {
		List<MessageTransitionData> findAll = repo.findAll();
		return findAll;
	}

	private List<List<String>> convertMessageTransitionToString(
			Map<String, Map<String, List<MessageTransitionData>>> data) {

		List<List<String>> result = new ArrayList<>();

		for (String acct : new TreeSet<String>(data.keySet())) {

			Map<String, List<MessageTransitionData>> stmt = data.get(acct);

			for (String statement : new TreeSet<String>(stmt.keySet())) {

				List<MessageTransitionData> list = stmt.get(statement);
				

				for (MessageTransitionData d : list) {

					List<String> l = new ArrayList<>();

					String status = d.getStatus();
					String receiverBic = d.getReceiverBic();
					String senderBic = d.getSenderBic();
					String msgType = d.getMsgType();
					String account = d.getAccount();
					String ccy = d.getCcy();
					String statementNumber = d.getStatementNumber();
					String sequenceNumber = d.getSequenceNumber();
					String isFinalSequenceNumber = d.getIsFinalSequenceNumber();
					String lengthMsg = d.getLengthMsg();
					Date receivedDate = d.getReceivedDate();
					String closingBalance = d.getClosingBalance();

					l.add(status);
					l.add(receiverBic);
					l.add(senderBic);
					l.add(msgType);
					l.add(account);
					l.add(ccy);
					l.add(statementNumber);
					l.add(sequenceNumber);
					l.add(isFinalSequenceNumber);
					l.add(lengthMsg);
					l.add(receivedDate.toString());
					l.add(closingBalance);

					result.add(l);
				}
			}
		}
		return result;
	}
	
	/*
	 * //
	 * -------------------------------------------------------------****************
	 * ***********************************
	 * 
	 * private String createCsvFile(Map<String, Map<String,
	 * List<MessageTransitionData>>> listToMap, String fileName) {
	 * List<List<String>> convertMessageTransitionToString =
	 * convertMessageTransitionToString(listToMap); return
	 * createCsvFile(convertMessageTransitionToString, reportPath, fileName); }
	 * 
	 * private void generateCsvFileMap(List<MessageTransitionData> data) {
	 * Map<String, Map<String, List<MessageTransitionData>>> listToMap =
	 * convertListToMap(data); String generateCsvFile_1 =
	 * generateCsvFile_1(listToMap); }
	 * 
	 * private String generateCsvFile_1(Map<String, Map<String,
	 * List<MessageTransitionData>>> data) { String fileName = new
	 * StringBuilder(reportPath).append(File.separator).append("SNI_Reject_Report")
	 * .append(".csv").toString(); String fileNameWithPath = createCsvFile_1(data,
	 * fileName); return fileNameWithPath; }
	 * 
	 * private String createCsvFile_1(Map<String, Map<String,
	 * List<MessageTransitionData>>> data, String fileName) {
	 * convertMessageTransitionToString(data); return null; }
	 * 
	 * //
	 * -----------------------------------------------------------------------------
	 * ---------------------------------------------
	 * 
	 * private void generateCsvFile(List<MessageTransitionData> list) throws
	 * IOException { String csvReportFileNameWithPath = new
	 * StringBuilder(reportFolderPath).append(File.separator)
	 * .append("SNI_Reject_Report").append(".csv").toString();
	 * 
	 * File localFile = new File(csvReportFileNameWithPath);
	 * Files.deleteIfExists(localFile.toPath());
	 * Files.createFile(localFile.toPath()); System.out.println("File " +
	 * csvReportFileNameWithPath + " created");
	 * 
	 * try (FileWriter fileWriter = new FileWriter(localFile); CSVWriter writer =
	 * new CSVWriter(fileWriter, ',', CSVWriter.NO_QUOTE_CHARACTER,
	 * CSVWriter.NO_ESCAPE_CHARACTER);) { String[] headers = fileHeader.split(";");
	 * 
	 * writer.writeNext(headers);
	 * 
	 * List<List<String>> list2 = convertMessageTransitionToString(list);
	 * 
	 * list2.forEach(data -> { String[] arr = data.stream().toArray(String[]::new);
	 * for (int i = 0; i < arr.length; i++) { if (null == arr[i]) arr[i] = "\"\"";
	 * else arr[i] = new
	 * StringBuilder().append("\"").append(arr[i]).append("\"").toString(); }
	 * writer.writeNext(arr); }); fileWriter.flush(); }
	 * 
	 * }
	 * 
	 * private List<List<String>>
	 * convertMessageTransitionToString(List<MessageTransitionData> list) {
	 * 
	 * List<List<String>> result = new ArrayList<>();
	 * 
	 * for (MessageTransitionData d : list) { List<String> l = new ArrayList<>();
	 * 
	 * String status = d.getStatus(); String receiverBic = d.getReceiverBic();
	 * String senderBic = d.getSenderBic(); String msgType = d.getMsgType(); String
	 * account = d.getAccount(); String ccy = d.getCcy(); String statementNumber =
	 * d.getStatementNumber(); String sequenceNumber = d.getSequenceNumber(); String
	 * isFinalSequenceNumber = d.getIsFinalSequenceNumber(); String lengthMsg =
	 * d.getLengthMsg(); Date receivedDate = d.getReceivedDate(); String
	 * closingBalance = d.getClosingBalance();
	 * 
	 * l.add(status); l.add(receiverBic); l.add(senderBic); l.add(msgType);
	 * l.add(account); l.add(ccy); l.add(statementNumber); l.add(sequenceNumber);
	 * l.add(isFinalSequenceNumber); l.add(lengthMsg);
	 * l.add(receivedDate.toString()); l.add(closingBalance); result.add(l); }
	 * return result; }
	 */
}
