package com.liferay.opensourceforlife.events;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.liferay.portal.kernel.cluster.ClusterExecutorUtil;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.events.Action;
import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * @author Brian Wing Shun Chan
 * @author vrf495
 */
public class LoginPostAction extends Action {

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
		throws ActionException {

		try {
			if (_log.isDebugEnabled()) {
				_log.debug("Running " + request.getRemoteUser());
			}

			HttpSession session = request.getSession();

			long companyId = PortalUtil.getCompanyId(request);
			long userId = 0;

			// Language
			session.removeAttribute( "org.apache.struts.action.LOCALE");

			// Live users			
			if ( GetterUtil.getBoolean(PropsUtil.get(PropsKeys.LIVE_USERS_ENABLED))) {
				JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

				ClusterNode clusterNode =
					ClusterExecutorUtil.getLocalClusterNode();

				if (clusterNode != null) {
					jsonObject.put(
						"clusterNodeId", clusterNode.getClusterNodeId());
				}

				jsonObject.put("command", "signIn");
				jsonObject.put("companyId", companyId);
				jsonObject.put("remoteAddr", request.getRemoteAddr());
				jsonObject.put("remoteHost", request.getRemoteHost());
				jsonObject.put("sessionId", session.getId());

				String userAgent = request.getHeader(HttpHeaders.USER_AGENT);

				jsonObject.put("userAgent", userAgent);

				userId = PortalUtil.getUserId(request);

				jsonObject.put("userId", userId);

				MessageBusUtil.sendMessage(
					DestinationNames.LIVE_USERS, jsonObject.toString());
			}

			if (PrefsPropsUtil.getBoolean(
					companyId, PropsKeys.ADMIN_SYNC_DEFAULT_ASSOCIATIONS)) {

				if (userId == 0) {
					userId = PortalUtil.getUserId(request);
				}

				UserLocalServiceUtil.addDefaultGroups(userId);
				UserLocalServiceUtil.addDefaultRoles(userId);
				UserLocalServiceUtil.addDefaultUserGroups(userId);
			}
		}
		catch (Exception e) {
			throw new ActionException(e);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(LoginPostAction.class);

}