package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.validation.CartesianProductValidator;
import com.gparser.validation.DuplicateLinesValidator;
import com.gparser.validation.ValidationResult;

/**
 * Complete action for KH.
 * Created by Gilad Ber on 4/5/14.
 */
public class KHCompleteAction implements ChannelAction
{
	private final SortAction sortAction = new SortAction(3, true);
	private final CompleteAction firstCompleteAction = new CompleteAction(1, 20);
	private final CompleteAction firstCompleteZeroAction = new CompleteAction(1, 0);
	private final CompleteAction secondCompleteNeg = new CompleteAction(2, -180);
	private final CompleteAction secondCompletePos = new CompleteAction(2, 180);
	private final CompleteAction thirdCompletePos = new CompleteAction(3, 180);
	private final CompleteAction thirdCompleteNeg = new CompleteAction(3, -180);
	private final DuplicateLinesValidator duplicateLinesValidator = new DuplicateLinesValidator(3);
	private final CartesianProductValidator cartesianProductValidator = new CartesianProductValidator(3);

	@Override
	public ChannelFileData perform(ChannelFileData data)
	{
		ValidationResult duplicateLinesResult = duplicateLinesValidator.validate(data);
		if (!duplicateLinesResult.isSucceeded())
		{
			throw new IllegalArgumentException(duplicateLinesResult.getMsg());
		}

		ValidationResult cartesianProductResult = cartesianProductValidator.validate(data);
		if (!cartesianProductResult.isSucceeded())
		{
			throw new IllegalArgumentException(cartesianProductResult.getMsg());
		}

		return sortAction.
			perform(firstCompleteAction.
				perform(firstCompleteZeroAction.
					perform(secondCompleteNeg.
						perform(secondCompletePos.
							perform(thirdCompletePos.
								perform(thirdCompleteNeg.
									perform(data)))))));
	}
}
