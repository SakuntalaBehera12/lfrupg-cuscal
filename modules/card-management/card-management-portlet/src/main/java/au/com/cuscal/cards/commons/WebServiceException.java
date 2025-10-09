package au.com.cuscal.cards.commons;

public class WebServiceException extends Exception {

	@Override
	public String getMessage() {

		// TODO Auto-generated method stub

		String msg =
			" WebServiceException - Default exception message is -- " +
				super.getMessage() +
					" and the status code from web service is Exception";

		return msg;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 4048876547300282244L;

}