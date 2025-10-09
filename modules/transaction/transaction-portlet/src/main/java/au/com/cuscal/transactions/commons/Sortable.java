package au.com.cuscal.transactions.commons;

import au.com.cuscal.transactions.domain.TransactionList;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class Sortable {

	@SuppressWarnings("unchecked")
	public static List<TransactionList> sort(
		List<TransactionList> list, String sortBy, String orderBy) {

		@SuppressWarnings("rawtypes")
		Comparator comp = getComparator(sortBy, orderBy);

		Collections.sort(list, comp);

		return list;
	}

	@SuppressWarnings("rawtypes")
	private static Comparator getComparator(String sortBy, String orderBy) {
		logger.debug(
			"Sort by : and Order by is   " + sortBy + " ::  " + orderBy);

		if ((null == sortBy) || Constants.DATE_TIME.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new DateComparator();
			}

			return new DateRevComparator();
		}

		if (Constants.CARD_ACCEPTOR_ID.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new CardAccptIdComparator();
			}

			return new CardAccptIdRevComparator();
		}

		if (Constants.TERMINAL_ID.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new TerminalIdComparator();
			}

			return new TerminalIdRevComparator();
		}

		if (Constants.RESPONSE_CODE.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new ResponseCodeComparator();
			}

			return new ResponseCodeRevComparator();
		}

		if (Constants.FUNCTION_CODE.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new FunctionCodeComparator();
			}

			return new FunctionCodeRevComparator();
		}

		if (Constants.MESSAGE_TYPE.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new MessageTypeComparator();
			}

			return new MessageTypeRevComparator();
		}

		if (Constants.SYSTEM_TRACE.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new SystemTraceComparator();
			}

			return new SystemTraceRevComparator();
		}

		if (Constants.DESCRIPTION.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new DescriptionComparator();
			}

			return new DescriptionRevComparator();
		}

		if (Constants.AMOUNT.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new AmountComparator();
			}

			return new AmountRevComparator();
		}

		if (Constants.PAN.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new PanComparator();
			}

			return new PanRevComparator();
		}

		if (Constants.TERMINAL_LOCATION.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new TerminalLocationComparator();
			}

			return new TerminalLocationRevComparator();
		}

		if ((null == sortBy) || Constants.CUD_TXN_DATE.equals(sortBy)) {
			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new CudTxnDateComparator();
			}

			return new CudTxnDateRevComparator();
		}

		if ((null == sortBy) ||
			Constants.EXTERNAL_TRANSACTION_ID.equals(sortBy)) {

			if (Constants.NATURAL.equalsIgnoreCase(orderBy)) {
				return new VisaIdComparator();
			}

			return new VisaIdRevComparator();
		}

		return null;
	}

	// natural==ascending; reverse==descending

	/**
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger("Sortable");

	@SuppressWarnings("rawtypes")
	private static class AmountComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			return Float.valueOf(
				txLst.getAmount()
			).compareTo(
				Float.valueOf(txLst2.getAmount())
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class AmountRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			return Float.valueOf(
				txLst2.getAmount()
			).compareTo(
				Float.valueOf(txLst.getAmount())
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class CardAccptIdComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(txLst.getCardAcceptorId());
			String str2 = StringUtils.trimToEmpty(txLst2.getCardAcceptorId());

			return str1.compareTo(str2);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class CardAccptIdRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(txLst.getCardAcceptorId());
			String str2 = StringUtils.trimToEmpty(txLst2.getCardAcceptorId());

			return str2.compareTo(str1);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class CudTxnDateComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			logger.debug("CudTxnDateComparator - start");
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			if ((null != txLst.getCudTransactionDate()) &&
				(null != txLst2.getCudTransactionDate())) {

				logger.debug("CudTxnDateComparator -  Dates are not null");

				if (txLst.getCudTransactionDate(
					).compareTo(
						txLst2.getCudTransactionDate()
					) == 0) {

					logger.debug("CudTxnDateComparator -  Dates are equals");

					if ((null != txLst2.getCudTranscationTime()) &&
						(null != txLst.getCudTranscationTime())) {

						logger.debug(
							"CudTxnDateComparator -  returning the time int " +
								txLst.getCudTranscationTime(
								).compareTo(
									txLst2.getCudTranscationTime()
								));

						return txLst.getCudTranscationTime(
						).compareTo(
							txLst2.getCudTranscationTime()
						);
					}

					return 0;
				}

				logger.debug("CudTxnDateComparator -  Dates are not equals");

				return txLst.getCudTransactionDate(
				).compareTo(
					txLst2.getCudTransactionDate()
				);
			}

			return 0;
		}

	}

	@SuppressWarnings("rawtypes")
	private static class CudTxnDateRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			logger.debug("CudTxnDateRevComparator - start");
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			if ((null != txLst.getCudTransactionDate()) &&
				(null != txLst2.getCudTransactionDate())) {

				logger.debug(
					"CudTxnDateRevComparator -  Dates are not null and 1st date is  " +
						txLst.getCudTransactionDate() + " 2nd date is " +
							txLst2.getCudTransactionDate());

				if (txLst2.getCudTransactionDate(
					).compareTo(
						txLst.getCudTransactionDate()
					) == 0) {

					logger.debug("CudTxnDateRevComparator -  Dates are equals");

					if ((null != txLst.getCudTranscationTime()) &&
						(null != txLst2.getCudTranscationTime())) {

						logger.debug(
							"CudTxnDateRevComparator -  Time are not null and 1st Time is  " +
								txLst.getCudTranscationTime() +
									" 2nd Time is " +
										txLst2.getCudTranscationTime());
						logger.debug(
							"CudTxnDateRevComparator -  Times are NOT null Time compare int " +
								txLst2.getCudTranscationTime(
								).compareTo(
									txLst.getCudTranscationTime()
								));

						return txLst2.getCudTranscationTime(
						).compareTo(
							txLst.getCudTranscationTime()
						);
					}

					return 0;
				}

				logger.debug("CudTxnDateRevComparator -  Dates are NOT equals");

				return txLst2.getCudTransactionDate(
				).compareTo(
					txLst.getCudTransactionDate()
				);
			}

			return 0;
		}

	}

	@SuppressWarnings("rawtypes")
	private static class DateComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			if ((null != txLst.getDateTime()) &&
				(null != txLst2.getDateTime())) {

				return txLst.getDateTime(
				).compareTo(
					txLst2.getDateTime()
				);
			}

			return 0;
		}

	}

	@SuppressWarnings("rawtypes")
	private static class DateRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			if ((null != txLst.getDateTime()) &&
				(null != txLst2.getDateTime())) {

				return txLst2.getDateTime(
				).compareTo(
					txLst.getDateTime()
				);
			}

			return 0;
		}

	}

	@SuppressWarnings("rawtypes")
	private static class DescriptionComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			String str1 = StringUtils.trimToEmpty(txLst.getDescription());
			String str2 = StringUtils.trimToEmpty(txLst2.getDescription());

			return str1.compareTo(str2);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class DescriptionRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(txLst.getDescription());
			String str2 = StringUtils.trimToEmpty(txLst2.getDescription());

			return str2.compareTo(str1);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class FunctionCodeComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToNull(txLst.getFunctionCode());
			String str2 = StringUtils.trimToNull(txLst2.getFunctionCode());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return 1;
			}

			if (!isStr2Num) {
				return -1;
			}

			return Integer.valueOf(
				str1
			).compareTo(
				Integer.valueOf(str2)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class FunctionCodeRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToNull(txLst.getFunctionCode());
			String str2 = StringUtils.trimToNull(txLst2.getFunctionCode());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return -1;
			}

			if (!isStr2Num) {
				return 1;
			}

			return Integer.valueOf(
				str2
			).compareTo(
				Integer.valueOf(str1)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class MessageTypeComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			String str1 = StringUtils.trimToNull(txLst.getSortableMsgType());
			String str2 = StringUtils.trimToNull(txLst2.getSortableMsgType());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return 1;
			}

			if (!isStr2Num) {
				return -1;
			}

			return Integer.valueOf(
				str1
			).compareTo(
				Integer.valueOf(str2)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class MessageTypeRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToNull(txLst.getSortableMsgType());
			String str2 = StringUtils.trimToNull(txLst2.getSortableMsgType());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return -1;
			}

			if (!isStr2Num) {
				return 1;
			}

			return Integer.valueOf(
				str2
			).compareTo(
				Integer.valueOf(str1)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class PanComparator implements Comparator {

		public static String getUnmaskedPartOfPanNumber(String maskedPan) {
			String maskedPartPan = new String();

			if (maskedPan.indexOf("*") != -1) {
				int start = maskedPan.indexOf("*");
				int end = maskedPan.lastIndexOf("*");
				maskedPartPan =
					maskedPan.substring(0, start) +
						maskedPan.substring(end + 1, maskedPan.length());
			}
			else {
				maskedPartPan = maskedPan;
			}

			return maskedPartPan;
		}

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			return getUnmaskedPartOfPanNumber(
				txLst.getPan()
			).compareTo(
				getUnmaskedPartOfPanNumber(txLst2.getPan())
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class PanRevComparator implements Comparator {

		public static String getUnmaskedPartOfPanNumber(String maskedPan) {
			String maskedPartPan = new String();

			if (maskedPan.indexOf("*") != -1) {
				int start = maskedPan.indexOf("*");
				int end = maskedPan.lastIndexOf("*");
				maskedPartPan =
					maskedPan.substring(0, start) +
						maskedPan.substring(end + 1, maskedPan.length());
			}
			else {
				maskedPartPan = maskedPan;
			}

			return maskedPartPan;
		}

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			return getUnmaskedPartOfPanNumber(
				txLst2.getPan()
			).compareTo(
				getUnmaskedPartOfPanNumber(txLst.getPan())
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class ResponseCodeComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToNull(txLst.getResponseCode());
			String str2 = StringUtils.trimToNull(txLst2.getResponseCode());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return 1;
			}

			if (!isStr2Num) {
				return -1;
			}

			return Integer.valueOf(
				str1
			).compareTo(
				Integer.valueOf(str2)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class ResponseCodeRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToNull(txLst.getResponseCode());
			String str2 = StringUtils.trimToNull(txLst2.getResponseCode());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return -1;
			}

			if (!isStr2Num) {
				return 1;
			}

			return Integer.valueOf(
				str2
			).compareTo(
				Integer.valueOf(str1)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class SystemTraceComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;

			String str1 = StringUtils.trimToNull(txLst.getSystemTrace());
			String str2 = StringUtils.trimToNull(txLst2.getSystemTrace());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return 1;
			}

			if (!isStr2Num) {
				return -1;
			}

			return Integer.valueOf(
				str1
			).compareTo(
				Integer.valueOf(str2)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class SystemTraceRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToNull(txLst.getSystemTrace());
			String str2 = StringUtils.trimToNull(txLst2.getSystemTrace());
			boolean isStr1Num = StringUtils.isNumeric(str1);
			boolean isStr2Num = StringUtils.isNumeric(str2);

			if (!isStr1Num && !isStr2Num) {
				return 0;
			}

			if (!isStr1Num) {
				return -1;
			}

			if (!isStr2Num) {
				return 1;
			}

			return Integer.valueOf(
				str2
			).compareTo(
				Integer.valueOf(str1)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class TerminalIdComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(txLst.getTerminalId());
			String str2 = StringUtils.trimToEmpty(txLst2.getTerminalId());

			return StringUtils.lowerCase(
				str1
			).compareTo(
				StringUtils.lowerCase(str2)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class TerminalIdRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(txLst.getTerminalId());
			String str2 = StringUtils.trimToEmpty(txLst2.getTerminalId());

			return StringUtils.lowerCase(
				str2
			).compareTo(
				StringUtils.lowerCase(str1)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class TerminalLocationComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(txLst.getTerminalLocation());
			String str2 = StringUtils.trimToEmpty(txLst2.getTerminalLocation());

			return str1.compareTo(str2);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class TerminalLocationRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(txLst.getTerminalLocation());
			String str2 = StringUtils.trimToEmpty(txLst2.getTerminalLocation());

			return str2.compareTo(str1);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class VisaIdComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(
				txLst.getExternalTransactionId());
			String str2 = StringUtils.trimToEmpty(
				txLst2.getExternalTransactionId());

			return StringUtils.lowerCase(
				str1
			).compareTo(
				StringUtils.lowerCase(str2)
			);
		}

	}

	@SuppressWarnings("rawtypes")
	private static class VisaIdRevComparator implements Comparator {

		public int compare(Object txLstObj, Object txLstObj2) {
			TransactionList txLst = (TransactionList)txLstObj;
			TransactionList txLst2 = (TransactionList)txLstObj2;
			String str1 = StringUtils.trimToEmpty(
				txLst.getExternalTransactionId());
			String str2 = StringUtils.trimToEmpty(
				txLst2.getExternalTransactionId());

			return StringUtils.lowerCase(
				str2
			).compareTo(
				StringUtils.lowerCase(str1)
			);
		}

	}

}