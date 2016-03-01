package edu.iris.dmc.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import edu.iris.dmc.validation.rule.Seed;

public class SeedAsciiValidator extends AbstractSeedValidator implements ConstraintValidator<Seed, String> {

	@Override
	public void initialize(Seed seed) {
		super.initialize(seed);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(seed.required()){
			if(value==null){
				return false;
			}
		}
		if (seed.expression() != null && !seed.expression().isEmpty()) {
			Pattern pattern = Pattern.compile(seed.expression());
			Matcher m = pattern.matcher(value);
			return m.matches();
		} else {
			/*if (value.chars().allMatch(c -> c < 128)) {

			} else {
				return false;
			}*/
			return (value.length() >= seed.length() && value.length() <= seed.length());
		}

	}

}
