package au.com.cuscal.transactions.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.io.Serializable;

/**
 * MVC Model class that represents code and description pair for Message ,
 * response codes.
 *
 * @author Rajni
 *
 */
@JsonAutoDetect
public class Codes implements Comparable<Codes>, Serializable {

	public int compareTo(Codes o) {

		// TODO Auto-generated method stub

		int retCount = 0;

		if (null != code) {
			retCount = code.compareTo(o.getCode());
		}

		return retCount;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)

			return true;

		if (obj == null)

			return false;

		if (getClass() != obj.getClass())

			return false;
		Codes other = (Codes)obj;

		if (code == null) {
			if (other.code != null)

				return false;
		}
		else if (!code.equals(other.code))

			return false;

		if (description == null) {
			if (other.description != null)

				return false;
		}
		else if (!description.equalsIgnoreCase(other.description))

			return false;

		return true;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((code == null) ? 0 : code.hashCode());
		result =
			(prime * result) +
				((description == null) ? 0 : description.hashCode());

		return result;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -6996289676295764852L;

	private String code;
	private String description;

}