/**
 * Returns custom landing path for organization's Private default page which user belongs to
 */

package com.liferay.opensourceforlife.landingpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.liferay.opensourceforlife.util.CustomLandingPageUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.Organization;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * @author tejas.kanani
 */
public class OrganizationPrivatePageType extends LandingPageType {

	/*
	 * (non-Javadoc)
	 * @see com.liferay.opensourceforlife.landingpage.LandingPageType#getLandingPagePath (javax.portlet.PortletRequest)
	 */
	@Override
	public String getLandingPagePath(final HttpServletRequest request)
			throws PortalException, SystemException {

		String organizationPath = StringPool.BLANK;

		User currentUser = PortalUtil.getUser(request);

		List<Organization> userOrganizations = currentUser.getOrganizations();

		if (userOrganizations != null && !userOrganizations.isEmpty()) {

			// If user is member of more than one organization then it will take
			// first organization from list
			String organizationFriendlyURL = userOrganizations.get(0)
					.getGroup().getFriendlyURL();

			organizationPath = CustomLandingPageUtil.getLanguage(request)
					+ PortalUtil.getPathFriendlyURLPrivateGroup()
					+ organizationFriendlyURL;
		}

		return organizationPath;
	}

}
