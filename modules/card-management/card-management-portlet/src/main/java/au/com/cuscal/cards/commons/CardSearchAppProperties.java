package au.com.cuscal.cards.commons;

import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class CardSearchAppProperties {

	public Properties getCardsProps() {
		return cardsProps;
	}

	public void setCardsProps(Properties cardsProps) {
		this.cardsProps = cardsProps;
	}

	private Properties cardsProps;

}