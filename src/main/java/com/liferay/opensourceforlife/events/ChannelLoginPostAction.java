package com.liferay.opensourceforlife.events;

import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.notifications.ChannelHubManagerUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ChannelLoginPostAction extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession();

			User user = (User)session.getAttribute(WebKeys.USER);

			if (!user.isDefaultUser()) {
				ChannelHubManagerUtil.getChannel(
					user.getCompanyId(), user.getUserId(), true);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		ChannelLoginPostAction.class);

}