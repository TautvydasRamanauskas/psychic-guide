package psychic.guide.api.model;

import javax.persistence.*;

@Entity
@Table(name = "options")
public class Options {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private boolean useNeuralNetwork = true;
	private boolean useGoogle = true;
	private boolean useYandex = true;
	private boolean useCache = true;
	private boolean useTextRuleReview = true;
	private boolean useTextRuleNumber = true;
	private boolean useTextRuleParenthesis = true;
	private long minRating = 3;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isUseNeuralNetwork() {
		return useNeuralNetwork;
	}

	public void setUseNeuralNetwork(boolean useNeuralNetwork) {
		this.useNeuralNetwork = useNeuralNetwork;
	}

	public boolean isUseGoogle() {
		return useGoogle;
	}

	public void setUseGoogle(boolean useGoogle) {
		this.useGoogle = useGoogle;
	}

	public boolean isUseYandex() {
		return useYandex;
	}

	public void setUseYandex(boolean useYandex) {
		this.useYandex = useYandex;
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}

	public boolean isUseTextRuleReview() {
		return useTextRuleReview;
	}

	public void setUseTextRuleReview(boolean useTextRuleReview) {
		this.useTextRuleReview = useTextRuleReview;
	}

	public boolean isUseTextRuleNumber() {
		return useTextRuleNumber;
	}

	public void setUseTextRuleNumber(boolean useTextRuleNumber) {
		this.useTextRuleNumber = useTextRuleNumber;
	}

	public boolean isUseTextRuleParenthesis() {
		return useTextRuleParenthesis;
	}

	public void setUseTextRuleParenthesis(boolean useTextRuleParenthesis) {
		this.useTextRuleParenthesis = useTextRuleParenthesis;
	}

	public long getMinRating() {
		return minRating;
	}

	public void setMinRating(long minRating) {
		this.minRating = minRating;
	}
}

//    options: {
//			neuralNetwork: true,
//			searchEngine: {
//			google: true,
//			yandex: true,
//			cache: true,
//			},
//			textRules: {
//			parenthesis: true,
//			review: true,
//			numbers: true,
//			},
//			rating: {
//			minRating: 2,
//			ratingDialogOpen: false,
//			},
//			},