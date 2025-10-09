package au.com.cuscal.cards.services.rest;

import static au.com.cuscal.cards.commons.Constants.AUDIT_REST_SERVICE;

import au.com.cuscal.cards.domain.AuditEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service(value = AUDIT_REST_SERVICE)
public class AuditRestService extends BaseCuscalRestService {

	public static final String EVENT_TYPE_CARD_CONTROL = "cardControl";

	public static final String EVENT_TYPE_CARD_LIMITS = "cardLimit";

	public static final String EVENT_TYPE_MCC_BLOCKING = "merchantCardControl";

	public List<AuditEvent> getAuditEvents(
			String cuscalToken, int pageIndex, int pageSize,
			List<String> eventTypes)
		throws Exception {

		final String eventTypesString = getListString(eventTypes);

		log.debug(
			"Retriving audit events for cuscal token " + cuscalToken +
				" page " + pageIndex + " of size " + pageSize +
					" and event types " + eventTypesString);

		final Map<String, String> queryParams = new HashMap<>();
		final Map<String, String> headers = new HashMap<>();

		queryParams.put(PARAM_ACTION_TYPE, ACTION_TYPE_CHANGE);
		queryParams.put(PARAM_EVENT_TYPE, eventTypesString);
		queryParams.put(
			PARAM_PAGE_INDEX,
			Integer.valueOf(
				pageIndex
			).toString());
		queryParams.put(
			PARAM_PAGE_SIZE,
			Integer.valueOf(
				pageSize
			).toString());

		final String svcUrl = String.format(
			auditSvcUrl + "/cards/%s", cuscalToken);

		return Arrays.asList(
			restService.doGet(
				svcUrl, queryParams, headers, AuditEvent[].class));
	}

	private String getListString(List<String> list) {
		StringBuilder sb = new StringBuilder();
		boolean isFirst = true;

		for (String item : list) {
			if (isFirst) {
				isFirst = false;
			}
			else {
				sb.append(",");
			}

			sb.append(item);
		}

		return sb.toString();
	}

	private static final String ACTION_TYPE_CHANGE = "change";

	private static final String PARAM_ACTION_TYPE = "actionType";

	private static final String PARAM_EVENT_TYPE = "eventType";

	private static final String PARAM_PAGE_INDEX = "pageIndex";

	private static final String PARAM_PAGE_SIZE = "pageSize";

	private static Logger log = LoggerFactory.getLogger(AuditRestService.class);

	@Autowired
	@Value("${webservice.audit.svc.url}")
	private String auditSvcUrl;

}