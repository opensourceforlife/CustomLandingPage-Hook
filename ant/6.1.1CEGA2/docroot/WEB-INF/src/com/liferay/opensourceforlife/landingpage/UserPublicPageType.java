/**
 * 
 */

package com.liferay.opensourceforlife.landingpage;

import javax.servlet.http.HttpServletRequest;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author tejas.kanani
 */
public class UserPublicPageType extends LandingPageType {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.opensourceforlife.landingpage.LandingPageType#getLandingPagePath (javax.portlet.PortletRequest)
	 */
	@Override
	public String getLandingPagePath(final HttpServletRequest request)
			throws PortalException, SystemException {

		String userPublicPagePath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);

		if (currentUser.hasPrivateLayouts()) {
			userPublicPagePath = CustomLandingPageUtil.getDisplayURL(request,
					false);
		}
		return userPublicPagePath;
	}

}
