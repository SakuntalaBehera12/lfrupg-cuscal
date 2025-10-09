package au.com.cuscal.cards.services.rest;

import static au.com.cuscal.cards.commons.Constants.CARD_SEARCH_APP_PROPERTIES;
import static au.com.cuscal.cards.commons.Constants.REST_SERVICE;

import au.com.cuscal.cards.commons.CardSearchAppProperties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class BaseCuscalRestService {

	@Autowired
	@Qualifier(CARD_SEARCH_APP_PROPERTIES)
	protected CardSearchAppProperties cardSearchAppProperties;

	@Autowired
	@Qualifier(REST_SERVICE)
	protected RestService restService;

}