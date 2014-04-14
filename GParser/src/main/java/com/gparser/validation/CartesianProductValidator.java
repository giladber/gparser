package com.gparser.validation;

import com.gparser.actions.SortAction;
import com.gparser.files.ChannelFileData;
import com.gparser.parsing.Line;
import com.gparser.parsing.LineFactory;
import com.gparser.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validator class for checking that a channeled file's independent channels data tuple set is a cartesian product.
 * <p>
 * For a m-tuple set M = {a_i}, (where each a_k={a_k_1, a_k_2,...,a_k_m}) to be a cartesian product, it must hold that
 * for any a_i_1 in a_i in M, a_j_2 in a_j in M, ..., a_k_m in a_k in M, (a_i_1, a_j_2, ..., a_k_m) is also in M.
 * Created by Gilad Ber on 4/5/14.
 */
public class CartesianProductValidator implements Validator
{

	private static final String NOT_CARTESIAN_PRODUCT_MSG = "Not a cartesian product: ";
	private static final int MAX_BAD_LINES = 50;
	private static final String EMPTY_LINE = "EMPTY";
	private static final Logger logger = LoggerFactory.getLogger(CartesianProductValidator.class);

	private final int numIndependentChannels;

	public CartesianProductValidator(int numIndependentChannels)
	{
		this.numIndependentChannels = numIndependentChannels;
	}

	@Override
	public ValidationResult validate(ChannelFileData data)
	{
		if (data.getRowData().size() == 0)
		{
			logger.info("Cartesian product validation successful: Empty input");
			return new ValidationResult(true, "empty data");
		}

		ChannelFileData sorted = new SortAction(numIndependentChannels, true).apply(data);
		DuplicateLinesValidator duplicateLinesValidator = new DuplicateLinesValidator(numIndependentChannels);
		ValidationResult duplicateValidationResult = duplicateLinesValidator.validate(sorted);
		if (!duplicateValidationResult.isSucceeded())
		{
			ValidationResult result = new ValidationResult(false, NOT_CARTESIAN_PRODUCT_MSG + duplicateValidationResult.getMsg());
			logger.info("Cartesian product validation failed - input has duplicate lines");
			return result;
		}

		//The nth element in this list is the set of all values for the nth channel in the input file.
		List<List<String>> independentChannelsData = getIndependentChannelsDataSorted(sorted);
		List<ComparedLine> badLines = getBadLines(sorted, independentChannelsData);

		ValidationResult validationResult = getValidationResult(badLines);
		logger.info("Cartesian product validation result: {}", validationResult.isSucceeded() ? "success" : "failure");
		return validationResult;
	}

	private ValidationResult getValidationResult(List<ComparedLine> badLines)
	{
		if (badLines.size() > 0)
		{
			String validationData = StringUtils.join(badLines.stream().map(cl -> "Expected @ Line " + cl.line.getIndex() + ": " + cl.compared + " != " + cl.line.getData()), "\n");
			return new ValidationResult(false, "Unable to verify cartesian product: " + validationData);
		}
		else
		{
			return new ValidationResult(true, "success");
		}
	}

	private List<ComparedLine> getBadLines(ChannelFileData sorted, List<List<String>> independentChannelsData)
	{
		//we can safely assume get() != null since we checked for empty data.
		List<String> cartesianProduct = independentChannelsData.stream().reduce(StringUtils::product).get();
		LineFactory lineFactory = new LineFactory();
		List<Line> cartesianLines = cartesianProduct.stream().map(lineFactory::next).collect(Collectors.toList());

		Iterator<Line> it = sorted.getRowData().iterator();
		return cartesianLines.stream().sequential().
			map(line -> new ComparedLine(it.hasNext() ? it.next() : new Line(line.getIndex(), EMPTY_LINE), line.getData())).
			filter(cl -> !StringUtils.compareRowData(cl.line.getData(), cl.compared, numIndependentChannels)).
			limit(MAX_BAD_LINES).
			collect(Collectors.toList());
	}

	private List<List<String>> getIndependentChannelsDataSorted(ChannelFileData sorted)
	{

		return sorted.getChannels().stream().
			sequential().
			limit(numIndependentChannels).
			map(ch -> new HashSet<String>(ch.getData())). //<String> is necessary because type inference is not good enough :(
			map(set -> set.stream().map(Double::parseDouble).collect(Collectors.toList())).
			map(doubleList -> {
				doubleList.sort((x, y) -> (int) (x - y));
				return doubleList.stream().sequential().map(d -> Double.toString(d)).collect(Collectors.toList());
			}).
			collect(Collectors.toList());
	}

	private static final class ComparedLine
	{
		private final Line line;
		private final String compared;

		private ComparedLine(Line line, String compared)
		{
			this.line = line;
			this.compared = compared;
		}
	}
}
