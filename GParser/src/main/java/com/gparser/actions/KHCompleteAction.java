package com.gparser.actions;

import com.gparser.files.ChannelFileData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private final ChannelAction composedCompleteAction;
	private static final Logger logger = LoggerFactory.getLogger(KHCompleteAction.class);

	public KHCompleteAction(KHCompleteActionInput input)
	{
		int numIndependentChannels = input.getNumIndependentChannels();
		this.sortAction = new SortAction(numIndependentChannels, true);
		this.composedCompleteAction = createComposedCompleteAction(input);
		logger.info("Successfully created KHCompleteAction composed of sort action and {} complete actions", input.getNumIndependentChannels() * 2);
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
		ChannelFileData result = sortAction.apply(composedCompleteAction.apply(data));
		logger.info("Applied KHCompleteAction to result");
		return result;
	}
}
