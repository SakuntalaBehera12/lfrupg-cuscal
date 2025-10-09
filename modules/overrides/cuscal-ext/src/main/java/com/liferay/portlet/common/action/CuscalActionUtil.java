package com.liferay.portlet.common.action;

import com.liferay.message.boards.model.MBDiscussion;
import com.liferay.message.boards.model.MBMessage;
import com.liferay.message.boards.model.MBMessageDisplay;
import com.liferay.message.boards.model.MBThread;
import com.liferay.message.boards.service.MBDiscussionLocalServiceUtil;
import com.liferay.message.boards.service.MBMessageLocalServiceUtil;
import com.liferay.message.boards.service.MBThreadLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import javax.portlet.ActionRequest;

public class CuscalActionUtil {

	public static final String APPROVAL_NOTES = "approvalNotes";

	/**
	 * Adds a discussion message to the workflow
	 *
	 * @param actionRequest
	 * @throws Exception
	 */
	public static void addCommentToWorkflow(
			ActionRequest actionRequest, long resourcePrimaryKey,
			String resourceClassName)
		throws Exception {

		String subject = "";
		String body = ParamUtil.getString(actionRequest, APPROVAL_NOTES);

		/**
		 * Message fields
		 */
		long threadId = 0;
		long parentMessageId = 0;

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			MBMessage.class.getName(), actionRequest);

		long userId = Long.valueOf(actionRequest.getRemoteUser());

		MBMessageDisplay messageDisplay =
			MBMessageLocalServiceUtil.getDiscussionMessageDisplay(
				userId, serviceContext.getScopeGroupId(), resourceClassName,
				resourcePrimaryKey, WorkflowConstants.STATUS_ANY);

		MBThread messageDisplayThread = messageDisplay.getThread();

		// this will create a new thread for the workflow message

		MBMessageLocalServiceUtil.getMessage(
			messageDisplayThread.getRootMessageId());

		MBDiscussion discussion = MBDiscussionLocalServiceUtil.getDiscussion(
			resourceClassName, resourcePrimaryKey);

		MBThread thread = MBThreadLocalServiceUtil.getThread(
			discussion.getThreadId());

		threadId = thread.getThreadId();
		parentMessageId = thread.getRootMessageId();

		// Add message

		MBMessageLocalServiceUtil.addDiscussionMessage(
			null, serviceContext.getUserId(), null,
			serviceContext.getScopeGroupId(), resourceClassName,
			resourcePrimaryKey, threadId, parentMessageId, subject, body,
			serviceContext);
	}

}