/**
 * Returns custom landing path for first site which user belongs to
 */

package com.liferay.opensourceforlife.landingpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author tejas.kanani
 */
public class SitePublicPageType extends LandingPageType {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.opensourceforlife.landingpage.LandingPageType#getLandingPagePath (javax.portlet.PortletRequest)
	 */
	@Override
	public String getLandingPagePath(final HttpServletRequest request)
			throws PortalException, SystemException {

		String sitePath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);

		List<Group> userSites = CustomLandingPageUtil.getSites(currentUser
				.getUserId());

		if (userSites != null && !userSites.isEmpty()) {
			// If user is member of more than one sites then it will take
			// first site from list
			String siteFriendlyURL = userSites.get(0).getFriendlyURL();
			sitePath = CustomLandingPageUtil.getLanguage(request)
					+ PortalUtil.getPathFriendlyURLPublic() + siteFriendlyURL;
		}
		return sitePath;
	}
}
