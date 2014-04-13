package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import com.gparser.validation.CartesianProductValidator;
import com.gparser.validation.DuplicateLinesValidator;
import com.gparser.validation.ValidationResult;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Complete action for KH.
 * Created by Gilad Ber on 4/5/14.
 */
public class KHCompleteAction implements ChannelAction
{
	private final SortAction sortAction;
	private final DuplicateLinesValidator duplicateLinesValidator;
	private final CartesianProductValidator cartesianProductValidator;
	private final ChannelAction composedCompleteAction;

	public KHCompleteAction(KHCompleteActionInput input)
	{
		int numIndependentChannels = input.getNumIndependentChannels();
		this.cartesianProductValidator = new CartesianProductValidator(numIndependentChannels);
		this.duplicateLinesValidator = new DuplicateLinesValidator(numIndependentChannels);
		this.sortAction = new SortAction(numIndependentChannels, true);

		this.composedCompleteAction = createComposedCompleteAction(input);
	}

	private ChannelAction createComposedCompleteAction(KHCompleteActionInput input)
	{
		final AtomicInteger counter = new AtomicInteger(1);
		List<ChannelAction> completeActions = Arrays.
			stream(input.getChannelBounds()).
			sequential().
			flatMap(arr -> {
				CompleteAction action1 = new CompleteAction(counter.get(), arr[0]);
				CompleteAction action2 = new CompleteAction(counter.getAndIncrement(), arr[1]);
				return Arrays.stream(new CompleteAction[]{action1, action2});
			}).
			collect(Collectors.toList());

		/*
		 * we may safely assume get() does not return null here since KHCompleteActionInput checks that
		 * the number of independent channels is at least 1, hence the completeActions list will have at least
		 * 2 elements.
		 */
		return completeActions.stream().reduce(ChannelAction::compose).get();
	}

	@Override
	public ChannelFileData apply(ChannelFileData data)
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

		return sortAction.apply(composedCompleteAction.apply(data));
	}
}
