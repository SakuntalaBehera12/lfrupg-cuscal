package au.com.cuscal.cards.commons;

public class WebServiceFailureError extends Exception {

	@Override
	public String getMessage() {

		// TODO Auto-generated method stub

		String msg =
			"WebServiceFailureError - Default exception message is -- " +
				super.getMessage() +
					" and the status code from web service Failure  ";

		return msg;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 2476591861588398478L;

}